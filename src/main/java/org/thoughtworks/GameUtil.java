package org.thoughtworks;

import java.util.Random;

public class GameUtil {

    private static GameState context;

    static String getAiHands() {
        Random random = new Random();
        String left = random.nextBoolean() ? "O" : "C";
        String right = random.nextBoolean() ? "O" : "C";
        return left + right;
    }

    static String getAiPrediction() {
        Random random = new Random();
        return String.valueOf(random.nextInt(5));
    }

    static int getTotalOpenHands(String user, String ai) {
        int userLeft = user.charAt(0) == 'O' ? 1 : 0;
        int userRight = user.charAt(1) == 'O' ? 1 : 0;
        int aiLeft = ai.charAt(0) == 'O' ? 1 : 0;
        int aiRight = ai.charAt(1) == 'O' ? 1 : 0;
        return userLeft + userRight + aiLeft + aiRight;
    }

    static int getPrediction(String user, String ai) {
        if (context.isUserPredictor) return Integer.parseInt(user.substring(2));
        else return Integer.parseInt(ai.substring(2));
    }

    static boolean isPredictionGood(String user, String ai) {
        return getTotalOpenHands(user, ai) == getPrediction(user, ai);
    }

    static boolean isValidHands(String response) {
        return response.substring(0,2).matches("(O|C){2}");
    }

    static boolean isValidPrediction(String response) {
        return response.substring(2).matches("^[0-4]$");
    }

    static boolean isEmptyPrediction(String response) {
        return response.substring(2).isEmpty();
    }

    public static boolean isValidTargetScore(String score) {
        return score.matches("^[1-5]$");
    }
}