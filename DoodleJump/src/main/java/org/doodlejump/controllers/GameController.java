package org.doodlejump.controllers;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.doodlejump.service.PlayerInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static org.doodlejump.service.VarConstants.*;

public class GameController {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label scoreLabel;
    private final int STAGE_WIDTH = 430;
    private final static int STAGE_HEIGHT = 569;
    private final List<ImageView> platforms = new ArrayList<>();
    private ImageView player;
    private double dy = 20; // Вертикальная скорость
    private final double baseGravity = 0.6; // Базовая гравитация
    private final double baseJumpPower = -15; // Базовая сила прыжка
    private double gravity = baseGravity; // Текущая гравитация
    private double jumpPower = baseJumpPower; // Текущая сила прыжка
    private boolean isJumping = false;
    private int score = 0;
    private AnimationTimer gameTimer;
    private double difficultyFactor = 1.0; // Коэффициент сложности
    private int remainingPlatforms = 15; // Начальное количество платформ
    private final int minPlatforms = 5; // Минимальное количество платформ

    private final Random random = new Random();

    public void initialize() {
        player = createPlayer();
        anchorPane.getChildren().add(player);
        addPlatforms();
        startGameLoop();

        Platform.runLater(() -> anchorPane.requestFocus());
        anchorPane.setFocusTraversable(true);
        Platform.runLater(this::setupKeyBindings);
    }

    private void setupKeyBindings() {
        anchorPane.getScene().setOnKeyPressed(event -> {
            double newX;
            switch (event.getCode()) {
                case LEFT:
                case A:
                    player.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(PATH_TO_LEFT_DOODLE))));
                    newX = player.getX() - 20;
                    if (newX < 0) newX = STAGE_WIDTH - player.getFitWidth();
                    player.setX(newX);
                    break;
                case RIGHT:
                case D:
                    player.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(PATH_TO_RIGHT_DOODLE))));
                    newX = player.getX() + 20;
                    if (newX > STAGE_WIDTH - player.getFitWidth()) newX = 0;
                    player.setX(newX);
                    break;
            }
        });
    }

    private ImageView createPlayer() {
        Image playerImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(PATH_TO_RIGHT_DOODLE)));
        ImageView player = new ImageView(playerImage);
        player.setFitWidth(70);
        player.setFitHeight(70);
        return player;
    }

    private void addPlatforms() {
        double y = STAGE_HEIGHT;
        for (int i = 0; i < remainingPlatforms; i++) {
            double x = random.nextDouble() * (STAGE_WIDTH - 68);
            ImageView platform = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(PATH_TO_PLATFORM))));
            platform.setX(x);
            platform.setY(y);
            platform.setFitWidth(60);
            platform.setFitHeight(10);
            platforms.add(platform);
            anchorPane.getChildren().add(platform);
            y -= 70;
        }

        player.setX(platforms.get(0).getX() + platforms.get(0).getFitWidth() / 2 - player.getFitWidth() / 2);
        player.setY(platforms.get(0).getY() - player.getFitHeight());
    }

    private void startGameLoop() {
        startJump();
        gameTimer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime != 0) {
                    long elapsedTime = now - lastTime;
                    if (elapsedTime > 1_000_000_000) { // Увеличивать сложность каждую секунду
                        difficultyFactor += 0.1;
                        gravity = baseGravity * difficultyFactor;
                        jumpPower = baseJumpPower * difficultyFactor;
                        lastTime = now;

                        // Уменьшение количества платформ со временем
                        if (remainingPlatforms > minPlatforms) {
                            remainingPlatforms--;
                        }
                    }
                } else {
                    lastTime = now;
                }

                try {
                    updateGame();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        gameTimer.start();
    }

    @FXML
    public void stopGame() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }

    private void updateGame() throws IOException {
        if (isJumping) {
            dy += gravity;
            player.setY(player.getY() + dy);
            if (player.getY() > STAGE_HEIGHT) {
                Stage stage = (Stage) anchorPane.getScene().getWindow();
                if (Integer.parseInt(PlayerInfo.getBestScore()) < score) PlayerInfo.setBestScore(String.valueOf(score));
                PlayerInfo.setScore(String.valueOf(score));
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(PATH_TO_GAMEOVER_FORM));
                Scene scene = new Scene(fxmlLoader.load());
                if (stage != null) stage.setScene(scene);
                isJumping = false;
                gameTimer.stop();

                return;
            }

            checkPlatformCollisions();
            regeneratePlatforms();
        }

        Iterator<ImageView> iter = platforms.iterator();
        while (iter.hasNext()) {
            ImageView platform = iter.next();
            if (platform.getY() > STAGE_HEIGHT) {
                anchorPane.getChildren().remove(platform);
                iter.remove();
            }
        }
    }

    private void regeneratePlatforms() {
        if (player.getY() < 200) {
            double shiftAmount = 200 - player.getY();
            for (ImageView platform : platforms) {
                double newY = platform.getY() + shiftAmount;
                platform.setY(newY);

                if (newY > STAGE_HEIGHT && platforms.size() < remainingPlatforms) {
                    double newX = random.nextDouble() * (STAGE_WIDTH - platform.getFitWidth());
                    platform.setY(-10);
                    platform.setX(newX);
                }
            }

            while (platforms.size() < remainingPlatforms) {
                double newX = random.nextDouble() * (STAGE_WIDTH - 68);
                double newY = (random.nextDouble() * 68);
                ImageView newPlatform = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(PATH_TO_PLATFORM))));
                newPlatform.setX(newX);
                newPlatform.setY(newY);
                newPlatform.setFitWidth(60);
                newPlatform.setFitHeight(10);
                platforms.add(newPlatform);
                anchorPane.getChildren().add(newPlatform);
            }

            player.setY(200);
            score += (int) (shiftAmount / 3);
            updateScore();
        }
    }

    private void updateScore() {
        scoreLabel.setText(String.valueOf(score));
    }

    private void checkPlatformCollisions() {
        for (ImageView platform : platforms) {
            if (player.getBoundsInParent().intersects(platform.getBoundsInParent()) && dy > 0) {
                dy = jumpPower;
                break;
            }
        }
    }

    private void startJump() {
        if (!isJumping) {
            isJumping = true;
            dy = jumpPower;
        }
    }
}
