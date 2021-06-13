package org.thoughtworks;

import java.io.PrintStream;
import java.util.Scanner;

import static org.thoughtworks.GameUtil.*;

public enum GameState {
    START {
        @Override
        public GameState execute() {
            scanner = new Scanner(System.in);
            isUserPredictor = true;
            sysOut.println("Welcome to the game!");
            return PLAY;
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
                if (isUserPredictor) sysOut.println("You WIN!!");
                else sysOut.println("You LOSE!!");
                return GAME_OVER;
            } else {
                sysOut.println("No winner.");
                return NEXT_ROUND;
            }
        }
    },
    NEXT_ROUND {
        @Override
        public GameState execute() {
            isUserPredictor = !isUserPredictor;
            return PLAY;
        }
    },
    GAME_OVER {
        @Override
        public GameState execute() {
            sysOut.println("Do you want to play again?");
            char input = scanner.next().charAt(0);
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
    static String userResponse;
    static String aiResponse;
    public abstract GameState execute();
}
