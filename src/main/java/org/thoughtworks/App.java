package org.thoughtworks;

public class App {
    public static void main( String[] args ) {
        GameState state = GameState.START;
        while (state != null) state = state.execute();
    }
}
