/* Bowling Example, Copyright 2003 Kyle Cordes
 * http://kylecordes.com
 *
 * Based on Ron Jeffries's "solution 2" simple bowling scorer,
 * converted to Java, then extended greatly to support knowing what
 * frame we are on, what frame has been scored, whether the game is
 * over, the score of each frame, and how many pins are standing
 */
package bowling;

public class Game {

	private static final int NUM_FRAMES = 10;
	public static final int NUM_PINS = 10;
	private static final int MAX_ROLLS_IN_GAME = NUM_FRAMES * 2 + 1;

	// Input state:
	private int[] rolls = new int[MAX_ROLLS_IN_GAME];
	private int rollSoFar = 0;

	// Output state:
	private int[] frameScores = new int[NUM_FRAMES];
	private int[] firstRollInFrame = new int[NUM_FRAMES + 1];
	private int scoredFrame;
	private int finishedFrame;
	private int standing = NUM_PINS;

	// Processing variables; these would be locals, but
	// this class essentially is a "method object", so we
	// use instance variables instead of param passing
	private int scoringFrame;
	private int scoringRoll; // always the first roll in a frame

	public void roll(int roll) throws InvalidPinsException {
		if (roll < 0 || roll > standing)
			throw new InvalidPinsException("Roll out of range");

		if (gameOver())
			throw new InvalidPinsException(
					"The game is over, no more rolls allowed.");

		rolls[rollSoFar] = roll;
		rollSoFar++;
		calculate();
	}

	private void calculate() throws InvalidPinsException {
		scoredFrame = 0;
		finishedFrame = 0;
		scoringRoll = 0;
		standing = NUM_PINS;
		for (scoringFrame = 1; scoringFrame <= NUM_FRAMES; scoringFrame++) {
			rememberFirstRollInFrame();
			if (isStrike()) {
				scoreStrike();
			} else if (isSpare()) {
				scoreSpare();
			} else {
				scoreNormal();
			}
		}
		rememberFirstRollInFrame();
	}

	private void rememberFirstRollInFrame() {
		firstRollInFrame[scoringFrame - 1] = Math.min(scoringRoll, rollSoFar);
	}

	private boolean isStrike() {
		return rolls[scoringRoll] == NUM_PINS;
	}

	private void scoreStrike() throws InvalidPinsException {
		storeFrameScore(NUM_PINS + rolls[scoringRoll + 1]
				+ rolls[scoringRoll + 2]);
		frameIsScoredIfThereAreMoreRolls(2);

		if (scoringTheLastFrame()) {
			calcPinsStandingForThirdRoll();
			frameIsDoneIfThereAreMoreRolls(2);
		} else
			frameIsDoneIfThereAreMoreRolls(0);
	}

	private boolean isSpare() {
		return rolls[scoringRoll] + rolls[scoringRoll + 1] == NUM_PINS;
	}

	private void scoreSpare() {
		storeFrameScore(NUM_PINS + rolls[scoringRoll + 2]);
		calcPinsStandingForSecondRoll();
		frameIsScoredIfThereAreMoreRolls(2);

		if (scoringTheLastFrame())
			frameIsDoneIfThereAreMoreRolls(2);
		else
			frameIsDoneIfThereAreMoreRolls(1);
	}

	private void scoreNormal() throws InvalidPinsException {
		checkForTooManyPins(scoringRoll);

		storeFrameScore(rolls[scoringRoll] + rolls[scoringRoll + 1]);
		calcPinsStandingForSecondRoll();
		frameIsScoredIfThereAreMoreRolls(1);
		frameIsDoneIfThereAreMoreRolls(1);
	}

	private void checkForTooManyPins(int firstRollIndex)
			throws InvalidPinsException {
		if (rolls[firstRollIndex] + rolls[firstRollIndex + 1] > NUM_PINS)
			throw new InvalidPinsException("Too many pins");
	}

	private void calcPinsStandingForSecondRoll() {
		if (scoringRoll + 1 == rollSoFar) {
			standing = NUM_PINS - rolls[scoringRoll];
		}
	}

	private void calcPinsStandingForThirdRoll() {
		if (scoringRoll + 2 == rollSoFar) {
			int middleRoll = rolls[scoringRoll + 1];
			if (middleRoll == NUM_PINS) {
				standing = NUM_PINS; // bowled a strike again
			} else {
				standing = NUM_PINS - middleRoll;
			}
		}
	}

	private boolean scoringTheLastFrame() {
		return scoringFrame == NUM_FRAMES;
	}

	private void storeFrameScore(int frameScore) {
		frameScores[scoringFrame - 1] = frameScore;
	}

	private void frameIsDoneIfThereAreMoreRolls(int additionalRolls) {
		if (scoringRoll + additionalRolls < rollSoFar) {
			finishedFrame = scoringFrame;
		}
		// Continue scoring at the roll after the last one
		// on this frame:
		scoringRoll += additionalRolls + 1;
	}

	private void frameIsScoredIfThereAreMoreRolls(int additionalRolls) {
		if (scoringRoll + additionalRolls < rollSoFar) {
			scoredFrame = scoringFrame;
		}
	}

	// The public interface has a few more methods for the new features:

	public int score() throws ScoringException {
		return scoreForFrame(NUM_FRAMES);
	}

	public int scoredFrames() {
		return scoredFrame;
	}

	public int finishedFrames() {
		return finishedFrame;
	}

	public int scoreForFrame(int frame) throws ScoringException {
		if (frame > scoredFrame) {
			throw new ScoringException("This frame has not yet been scored");
		}
		int score = 0;
		for (int i = 0; i < frame; i++) {
			score += frameScores[i];
		}
		return score;
	}

	public boolean gameOver() {
		return finishedFrame == NUM_FRAMES;
	}

	public int pinsStanding() {
		return standing;
	}

	public int[] rollsForFrame(int frame) {
		int firstRoll = firstRollInFrame[frame - 1];
		int lastRoll = firstRollInFrame[frame];
		int[] frameRolls = new int[lastRoll - firstRoll];
		for (int i = 0; i < frameRolls.length; i++) {
			frameRolls[i] = rolls[i + firstRoll];
		}
		return frameRolls;
	}
}
