package org.doodlejump.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.doodlejump.MainMenu;
import org.doodlejump.audiocontrol.AudioStateService;

import java.io.IOException;

import static org.doodlejump.service.VarConstants.PATH_TO_MAIN_MENU_FORM;

public class SettingsController {
    @FXML private Label labelOn;
    @FXML private Label labelOff;

    public void initialize() {
        updateLabels(AudioStateService.isMusicPlaying());
    }

    /**
     * update labels color by switching color
     * @param isMusicPlaying verification parameter
     */
    private void updateLabels(boolean isMusicPlaying) {
        if (isMusicPlaying) {
            labelOn.setStyle("-fx-text-fill: rgb(35, 198, 43);");
            labelOff.setStyle("-fx-text-fill: rgb(170, 167, 162);");
        } else {
            labelOn.setStyle("-fx-text-fill: rgb(170, 167, 162);");
            labelOff.setStyle("-fx-text-fill: rgb(35, 198, 43);");
        }
    }

    /**
     * turning on the music and updating the status
     */
    @FXML
    private void handleOnAction() {
        AudioStateService.setMusicPlaying(true);
        updateLabels(true);
    }

    /**
     * turning off the music and updating the status
     */
    @FXML
    private void handleOffAction() {
        AudioStateService.setMusicPlaying(false);
        updateLabels(false);
    }

    /**
     * close application by button
     */
    @FXML
    private void handleQuitAction(){
        System.exit(0);
    }

    /**
     * open main-menu form by button
     * @param event button click event
     * @throws IOException
     */
    @FXML
    protected void returnToMainMenu(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        Scene thisScene = source.getScene();
        Stage stage = (Stage) thisScene.getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(MainMenu.class.getResource(PATH_TO_MAIN_MENU_FORM));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }
}
