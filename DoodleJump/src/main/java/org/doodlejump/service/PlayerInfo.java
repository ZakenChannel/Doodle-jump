package org.doodlejump.service;

import org.doodlejump.audiocontrol.AudioStateService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.doodlejump.service.VarConstants.PATH_TO_SETTINGS_FILE;
public class PlayerInfo {
    private static final String name = "doodler";
    private static String score = "0";
    private static String bestScore = "0";

    public static String getName() {
        return name;
    }
    public static String getScore() {
        return score;
    }
    public static String getBestScore() {
        return bestScore;
    }

    public static void setScore(String score) {
        PlayerInfo.score = score;
        updateScore(name, score, bestScore);
    }

    public static void setBestScore(String bestScore) {
        PlayerInfo.bestScore = bestScore;
        updateScore(name, score, bestScore);
    }

    public static void updateScore(String name, String score, String bestScore) {
        File file = new File(PATH_TO_SETTINGS_FILE);
        List<String> fileContent = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = name + ":" + score + ":" + bestScore + ":" + AudioStateService.isMusicPlaying();
                fileContent.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : fileContent) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
