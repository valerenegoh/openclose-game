# The Open-Closed Game

## Rules of the game

This game is played between two players.
One player will be the predictor.
To play the game, after a count of three, the players will need to simultaneously show their hands with each hand either open or closed, and the predictor need to shout out how many hands they think will be open on total.
If the predictor is correct, they win, otherwise the other player becomes the predictor and they go again. This continues until the game is won.

## The challenge

You need to create a program to play this game against the computer. This can just be a simple console application.
You will always be the predictor first.
The “AI” player will just do things randomly.

For each round, the computer will expect player input in the following format if you are the predictor:
```OC2```

Or if you are not:
```CO```

That is, the first two characters will show how you will play your hands, O for open or C for closed. If you are the predictor, you also need to enter a third character which is your prediction for how many open hands in total.

The program should then reveal the “AI” players input and indicate if the game was won, or move to the next round.

### Example of what game play could look like

```shell
Welcome to the game!
You are the predictor, what is your input?
OO4
AI: CO
No winner.
AI is the predictor, what is your input?
CC
AI: OO0
No winner.
You are the predictor, what is your input?
CO3
AI: OO
You WIN!!
Do you want to play again?
N
Ok, bye!
```

## Tips

Remember to validate the input and give the user useful messages if their input is not valid.
Valid input should be either a C or O for the first two character and a number between 0 and 4 for the prediction.
For example, if the user enters “chicken” on their turn, you could display a message such as:
```
Bad input: correct input should be of the form CC3, where the first two letters indicate [O]pen or [C]losed state for each hand, followed by the prediction (0-4).
```
If the user enters “CC8” the error could be:
```
Bad input: prediction should be in the range of 0-4.
```
Or, if they are not the predictor:
```
Bad input: no prediction expected, you are not the predictor.
```
Try and write unit tests for your code, if you can, use TDD (test driven development) when writing the solution.

Think about the design of your objects and cleanliness of your code. Will it be easy for you to make changes to your code to implement some of the extensions? How about for someone else?


The take-home assignment question ends here. Extensions will be shared with candidates during the event itself to implement.


## Extensions

If you complete the main challenge, you may attempt some of these extensions. They can be done in any order.

### Scoring
When you start a game ask the player for target score (for example, 3 points).
When playing you get one point for each correct prediction, and only win the game when you reach the target score. If you predicted correctly, you stay as the predictor in the next round. It is up to you to choose the validation rules for this. (Do you want to allow them to make a target score of 1000000 or -1?)

### Difficulty setting
When you start a game ask the player if they want the AI to be Easy or Hard.
Easy AI will guess any number between 0 and 4 randomly when predicting.
Hard AI will only guess values that are possible based on it’s own hand state.  For example, if the AI is going to show one open and one closed hand, it would not predict 0 or 4 as they would be impossible.

### Two player
When starting a game, allow the player to choose if they want to play against AI or another human.
If playing with two humans, remember, you need to hide player 1 input when player 2 in entering their values.
A further extension to this would be to allow the user to choose if each player is AI or human. This would enable you to be player 2 and the AI be player 1 or even have the AI play itself!

### N players
When starting a game, allow the user to choose how many players are in the game. (2, 3, 4, more?) How would you adjust the rules to cater for this?

### All Predictors
When starting a game, allow the user to choose if the games uses “turn based predictors” or “all predictors”.
Turn based is as described in the regular rules.
All predictors means that every player is a predictor in every round. With this rule, tie games are possible so it is up to you if there should be a tie-breaker or not.

#### Anything else?
Do you have any great ideas for how to extend this game? Feel free to implement your own ideas!
