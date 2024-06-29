package org.doodlejump;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.doodlejump.audiocontrol.AudioController;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static org.doodlejump.service.VarConstants.*;

public class MainMenu extends Application implements Initializable {
    @FXML
    private ImageView doodler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        jumpDoodler(doodler);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainMenu.class.getResource(PATH_TO_MAIN_MENU_FORM));
        Scene scene = new Scene(fxmlLoader.load());
        doodler = (ImageView) fxmlLoader.getNamespace().get("doodler");
        AudioController.playBackgroundMusic();
        stage.setTitle("Doodle Jump");
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream(PATH_TO_RIGHT_DOODLE)));
        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * doodler jumps in the main menu
     *
     * @param doodler ImageView object from form
     */
    public static void jumpDoodler(ImageView doodler) {
        Timeline timeline = new Timeline();

        KeyFrame jumpUp = new KeyFrame(Duration.seconds(0.7),
                new KeyValue(doodler.translateYProperty(), -250, Interpolator.EASE_OUT));

        KeyFrame fallDown = new KeyFrame(Duration.seconds(1.4),
                new KeyValue(doodler.translateYProperty(), 0, Interpolator.EASE_IN));

        timeline.getKeyFrames().addAll(jumpUp, fallDown);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);

        timeline.play();
    }

    /**
     * open settings form by button
     * @param event button click event
     * @throws IOException
     */
    @FXML
    private void handleOpenSettingsAction(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        Scene thisScene = source.getScene();
        Stage stage = (Stage) thisScene.getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(MainMenu.class.getResource(PATH_TO_SETTINGS_FORM));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

    @FXML
    private void handleOpenGameAction(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        Scene thisScene = source.getScene();
        Stage stage = (Stage) thisScene.getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(MainMenu.class.getResource(PATH_TO_GAME_FORM));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }


    /**
     * close application by button
     */
    @FXML
    private void handleQuitAction(){
        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }
}