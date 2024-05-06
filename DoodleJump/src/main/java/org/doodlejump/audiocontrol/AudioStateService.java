package org.doodlejump.audiocontrol;

public class AudioStateService {
    private static boolean isMusicPlaying = true;

    public static boolean isMusicPlaying() {
        return isMusicPlaying;
    }

    public static void setMusicPlaying(boolean musicPlaying) {
        isMusicPlaying = musicPlaying;
        if (isMusicPlaying) {
            AudioController.playBackgroundMusic();
        } else {
            AudioController.stopBackgroundMusic();
        }
    }
}
