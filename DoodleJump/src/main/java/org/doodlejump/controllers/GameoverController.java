package org.doodlejump.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.doodlejump.service.PlayerInfo;

import java.io.IOException;
import java.util.Objects;

import static org.doodlejump.service.VarConstants.PATH_TO_GAME_FORM;
import static org.doodlejump.service.VarConstants.PATH_TO_MAIN_MENU_FORM;

public class GameoverController {
    @FXML private Label score;
    @FXML private Label bestScore;
    @FXML private Label name;

    public void initialize() {
        score.setText(PlayerInfo.getScore());
        bestScore.setText(PlayerInfo.getBestScore());
        name.setText(PlayerInfo.getName());
    }

    @FXML
    private void handleOpenGameAction(ActionEvent event) throws IOException {
        goToNewScene(event, PATH_TO_GAME_FORM);
    }

    @FXML
    private void handleOpenMenuAction(ActionEvent event) throws IOException {
        goToNewScene(event, PATH_TO_MAIN_MENU_FORM);
    }

    private void goToNewScene(ActionEvent event, String path) throws IOException {
        Node source = (Node) event.getSource();
        Scene thisScene = source.getScene();
        Stage stage = (Stage) thisScene.getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(path)));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }
}
