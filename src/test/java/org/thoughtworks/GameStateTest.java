package org.thoughtworks;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GameStateTest {
    OutputStream out;
    GameState state;

    @Before
    public void setUp() {
        out = new ByteArrayOutputStream();
        GameState.sysOut = new PrintStream(out);
    }

    @Test
    public void shouldPrintWelcomeMessageForStartState() {
        state = GameState.START;
        state.execute();

        assertThat(out.toString(), containsString("Welcome to the game!"));
        assertThat(out.toString(), containsString("What is your target score?"));
        assertThat(GameState.isUserPredictor, is(true));
    }

    @Test
    public void shouldPrintCorrectValidationMessageForValidateTargetScoreState() {
        state = GameState.VALIDATE_TARGET_SCORE;

        GameState.targetScore = 3;
        state.execute();
        assertThat(out.toString(), containsString("Target score set. Predict correctly 3 times to win the game."));

        GameState.targetScore = -1;
        state.execute();
        assertThat(out.toString(), containsString("Bad input: target score should be in the range of 1-5."));
    }

    @Test
    public void shouldPrintCorrectPromptMessageForPlayState() {
        state = GameState.PLAY;

        GameState.isUserPredictor = true;
        state.execute();
        assertThat(out.toString(), containsString("You are the predictor, what is your input?"));

        GameState.isUserPredictor = false;
        state.execute();
        assertThat(out.toString(), containsString("AI is the predictor, what is your input?"));
    }

    @Test
    public void shouldPrintCorrectInvalidInputMessageForValidateInputState() {
        state = GameState.VALIDATE_INPUT;
        String validHandsInvalidPrediction = "Bad input: prediction should be in the range of 0-4.";

        GameState.isUserPredictor = true;
        GameState.userResponse = "CC8";
        state.execute();
        assertThat(out.toString(), containsString(validHandsInvalidPrediction));
        GameState.userResponse = "CCe";
        state.execute();
        assertThat(out.toString(), containsString(validHandsInvalidPrediction));
        GameState.userResponse = "CC";
        state.execute();
        assertThat(out.toString(), containsString(validHandsInvalidPrediction));
        GameState.userResponse = "chicken";
        state.execute();
        assertThat(out.toString(), containsString("Bad input: correct input should be of the form CC3, where the first two letters indicate [O]pen or [C]losed state for each hand, followed by the prediction (0-4)."));

        GameState.isUserPredictor = false;
        GameState.userResponse = "CC3";
        state.execute();
        assertThat(out.toString(), containsString("Bad input: no prediction expected, you are not the predictor."));
        GameState.userResponse = "chicken";
        state.execute();
        assertThat(out.toString(), containsString("Bad input: correct input should be of the form CC, where the letters indicate [O]pen or [C]losed state for each hand."));
    }

    @Test
    public void shouldPrintAiResponseForAiRespondsState() {
        state = GameState.AI_RESPONDS;

        state.execute();

        assertThat(out.toString(), containsString("AI: "));
    }

    @Test
    public void shouldPrintCorrectResultMessageForGetResultState() {
        state = GameState.GET_RESULT;
        GameState.targetScore = 2;

        GameState.isUserPredictor = true;
        GameState.userResponse = "OO4";
        GameState.aiResponse = "CO";
        state.execute();
        assertThat(out.toString(), containsString("No winner."));
        assertThat(GameState.isUserPredictor, is(false));

        GameState.isUserPredictor = true;

        GameState.userResponse = "CO3";
        GameState.aiResponse = "OO";

        state.execute();
        assertThat(out.toString(), containsString("You win this round!!"));
        assertThat(GameState.isUserPredictor, is(true));

        state.execute();
        assertThat(out.toString(), containsString("You WIN!!"));

        GameState.isUserPredictor = false;

        GameState.userResponse = "OC";
        GameState.aiResponse = "CO2";

        state.execute();
        assertThat(out.toString(), containsString("You lose this round!!"));
        assertThat(GameState.isUserPredictor, is(false));

        state.execute();
        assertThat(out.toString(), containsString("You LOSE!!"));
    }

    @Test
    public void shouldPrintPlayAgainMessageForGameOverState() {
        state = GameState.GAME_OVER;

        String input = "N";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        GameState.scanner = new Scanner(System.in);

        state.execute();

        assertThat(out.toString(), containsString("Do you want to play again?"));
        assertThat(out.toString(), containsString("Ok, bye!"));
    }

    @Test
    public void shouldHaveCorrectStateTransitions() {
        state = GameState.START;

        String input = "1";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        GameState.scanner = new Scanner(System.in);

        state = state.execute();
        assertThat(state, is(GameState.AWAIT_TARGET_SCORE));

        state = state.execute();
        assertThat(state, is(GameState.VALIDATE_TARGET_SCORE));

        state = state.execute();
        assertThat(state, is(GameState.PLAY));

        input = "CO3";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        GameState.scanner = new Scanner(System.in);

        state = state.execute();
        assertThat(state, is(GameState.AWAIT_INPUT));

        state = state.execute();
        assertThat(state, is(GameState.VALIDATE_INPUT));

        state = state.execute();
        assertThat(state, is(GameState.AI_RESPONDS));

        state = state.execute();
        assertThat(state, is(GameState.GET_RESULT));

        GameState.aiResponse = "OO";

        state = state.execute();
        assertThat(state, is(GameState.GAME_OVER));

        input = "N";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        GameState.scanner = new Scanner(System.in);

        state = state.execute();
        assertThat(state, is(GameState.END));
    }
}
