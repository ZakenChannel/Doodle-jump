package org.doodlejump.audiocontrol;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

import java.io.File;

import static org.doodlejump.service.VarConstants.*;

public class AudioController {
    private static AudioClip mediaPlayer;

    public static void playBackgroundMusic() {
        if (mediaPlayer == null) {
            Media sound = new Media(new File(PATH_TO_BACKGROUND_MUSIC).toURI().toString());
            mediaPlayer = new AudioClip(sound.getSource());
            mediaPlayer.setCycleCount(AudioClip.INDEFINITE);
        }
        else if (mediaPlayer.isPlaying()) return;

        mediaPlayer.play();
    }

    public static void stopBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}
