package org.thoughtworks;

import java.io.PrintStream;
import java.util.Scanner;

import static org.thoughtworks.GameUtil.*;

public enum GameState {
    START {
        @Override
        public GameState execute() {
            scanner = new Scanner(System.in);
            resetConfigurations();
            sysOut.println("Welcome to the game!");
            sysOut.println("What is your target score?");
            return TARGET_SCORE;
        }
    },
    TARGET_SCORE {
        @Override
        public GameState execute() {
            String input = scanner.nextLine();
            if (isValidTargetScore(input)) {
                targetScore = Integer.parseInt(input);
                sysOut.println(String.format("Target score set. Predict correctly %d times to win the game.", targetScore));
                return PLAY;
            }
            sysOut.println("Bad input: target score should be in the range of 1-5.");
            return TARGET_SCORE;
        }
    },
    PLAY {
        @Override
        public GameState execute() {
            if (isUserPredictor) sysOut.println("You are the predictor, what is your input?");
            else sysOut.println("AI is the predictor, what is your input?");
            return AWAIT_INPUT;
        }
    },
    AWAIT_INPUT {
        @Override
        public GameState execute() {
            userResponse = scanner.nextLine();
            return VALIDATE_INPUT;
        }
    },
    VALIDATE_INPUT {
        @Override
        public GameState execute() {
            if (isUserPredictor) {
                if (isValidHands(userResponse) && isValidPrediction(userResponse)) return AI_RESPONDS;
                if (isValidHands(userResponse) && !isValidPrediction(userResponse)) sysOut.println("Bad input: prediction should be in the range of 0-4.");
                else sysOut.println("Bad input: correct input should be of the form CC3, where the first two letters indicate [O]pen or [C]losed state for each hand, followed by the prediction (0-4).");
            } else {
                if (isValidHands(userResponse) && isEmptyPrediction(userResponse)) return AI_RESPONDS;
                if (isValidHands(userResponse) && !isEmptyPrediction(userResponse)) sysOut.println("Bad input: no prediction expected, you are not the predictor.");
                else sysOut.println("Bad input: correct input should be of the form CC, where the letters indicate [O]pen or [C]losed state for each hand.");
            }
            return AWAIT_INPUT;
        }
    },
    AI_RESPONDS {
        @Override
        public GameState execute() {
            String hands = getAiHands();
            aiResponse = isUserPredictor ? hands : hands + getAiPrediction();
            sysOut.println("AI: " + aiResponse);
            return GET_RESULT;
        }
    },
    GET_RESULT {
        @Override
        public GameState execute() {
            if (isPredictionGood(userResponse, aiResponse)) {
                if (isUserPredictor) {
                    userScore++;
                    if (userScore == targetScore) {
                        sysOut.println("You WIN!!");
                        return GAME_OVER;
                    } else {
                        sysOut.println("You win this round!!");
                        isUserPredictor = true;
                    }
                } else {
                    aiScore++;
                    if (aiScore == targetScore) {
                        sysOut.println("You LOSE!!");
                        return GAME_OVER;
                    } else {
                        sysOut.println("You lose this round!!");
                        isUserPredictor = false;
                    }
                }
            } else {
                sysOut.println("No winner.");
                isUserPredictor = !isUserPredictor;
            }
            return PLAY;
        }
    },
    GAME_OVER {
        @Override
        public GameState execute() {
            sysOut.println("Do you want to play again?");
            char input = scanner.nextLine().charAt(0);
            if (input == 'N' || input == 'n') {
                sysOut.println("Ok, bye!");
                return END;
            }
            else return START;
        }
    },
    END {
        @Override
        public GameState execute() {
            return null;
        }
    };

    static Scanner scanner;
    static PrintStream sysOut = new PrintStream(System.out);
    static boolean isUserPredictor;
    static int targetScore;
    static int userScore;
    static int aiScore;
    static String userResponse;
    static String aiResponse;

    abstract GameState execute();

    void resetConfigurations() {
        isUserPredictor = true;
        userScore = 0;
        aiScore = 0;
    }
}
