package org.thoughtworks;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class GameUtilTest {

    @Test
    public void shouldGetValidRandomHandsForAiResponse() {
        String hands = GameUtil.getAiHands();
        assertThat(hands, anyOf(is("OO"), is("CC"), is("OC"), is("CO")));
    }

    @Test
    public void shouldGetValidPredictionForAiResponse() {
        String prediction = GameUtil.getAiPrediction();
        assertTrue(prediction.matches("^[0-4]$"));
    }

    @Test
    public void shouldGetCorrectTotalOpenHands() {
        assertThat(GameUtil.getTotalOpenHands("OO4", "CO"), is(3));
        assertThat(GameUtil.getTotalOpenHands("CC", "OO0"), is(2));
    }

    @Test
    public void shouldGetCorrectPrediction() {
        GameState.isUserPredictor = true;
        assertThat(GameUtil.getPrediction("OO4", "CO"), is(4));
        GameState.isUserPredictor = false;
        assertThat(GameUtil.getPrediction("CC", "OO0"), is(0));
    }

    @Test
    public void shouldReturnIfPredictionIsGood() {
        GameState.isUserPredictor = true;
        assertThat(GameUtil.isPredictionGood("OO4", "CO"), is(false));
        assertThat(GameUtil.isPredictionGood("CO3", "OO"), is(true));
        GameState.isUserPredictor = false;
        assertThat(GameUtil.isPredictionGood("OO", "CO4"), is(false));
        assertThat(GameUtil.isPredictionGood("OC", "CO2"), is(true));
    }

    @Test
    public void shouldReturnIfUserGivesValidHandsInput() {
        assertThat(GameUtil.isValidHands("OO4"), is(true));
        assertThat(GameUtil.isValidHands("chicken"), is(false));
    }

    @Test
    public void shouldReturnIfUserGivesValidPredictionInputWhenHandsInputIsValid() {
        assertThat(GameUtil.isValidPrediction("CC3"), is(true));
        assertThat(GameUtil.isValidPrediction("CC8"), is(false));
        assertThat(GameUtil.isValidPrediction("CCe"), is(false));
        assertThat(GameUtil.isValidPrediction("CC"), is(false));
    }

    @Test
    public void shouldReturnIfUserGivesNoPredictionInputWhenHandsInputIsValid() {
        assertThat(GameUtil.isEmptyPrediction("CC"), is(true));
        assertThat(GameUtil.isEmptyPrediction("CC3"), is(false));
    }

    @Test
    public void shouldReturnIfUserGivesValidTargetScoreInput() {
        assertThat(GameUtil.isValidTargetScore("3"), is(true));
        assertThat(GameUtil.isValidTargetScore("-1"), is(false));
        assertThat(GameUtil.isValidTargetScore("1000000"), is(false));
        assertThat(GameUtil.isValidTargetScore("ten"), is(false));
    }
}