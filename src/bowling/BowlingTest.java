/*
 * Bowling Example, Copyright 2003 Kyle Cordes http://kylecordes.com
 */
package bowling;

import junit.framework.TestCase;

public class BowlingTest extends TestCase {

	private Game game = new Game();

	public void testGutterBalls() throws Exception {
		rollMany(20, 0);
		assertEquals(0, game.score());
	}

	public void testThrees() throws Exception {
		rollMany(20, 3);
		assertEquals(60, game.score());
	}

	private void rollMany(int count, int roll) throws InvalidPinsException {
		for (int i = 0; i < count; i++)
			game.roll(roll);
	}

	public void testPerfect() throws Exception {
		rollMany(12, 10);
		assertEquals(300, game.score());
	}

	public void testAlternatiing() throws Exception {
		for (int i = 0; i < 5; i++) {
			game.roll(10);
			game.roll(4);
			game.roll(6);
		}
		game.roll(10);
		assertEquals(200, game.score());
	}

	public void testAlternatiingWithPinsStanding() throws Exception {
		for (int i = 0; i < 5; i++) {
			game.roll(10);
			assertEquals(10, game.pinsStanding());
			game.roll(4);
			assertEquals(6, game.pinsStanding());
			game.roll(6);
			assertEquals(10, game.pinsStanding());
		}
		game.roll(10);
		assertEquals(200, game.score());
	}

	public void testThreesWithFrameCounting() throws Exception {
		assertEquals(0, game.finishedFrames());

		for (int frame = 1; frame <= 10; frame++) {
			game.roll(3);
			game.roll(3);
			assertEquals(frame, game.finishedFrames());
			assertEquals(frame, game.scoredFrames());
		}
		assertEquals(60, game.score());
	}

	public void testThreesWithFrameCountingPinsStanding() throws Exception {
		assertEquals(0, game.finishedFrames());

		for (int frame = 1; frame <= 10; frame++) {
			assertEquals(10, game.pinsStanding());
			game.roll(3);
			assertEquals(7, game.pinsStanding());
			game.roll(3);
			assertEquals(10, game.pinsStanding());
			assertEquals(frame, game.finishedFrames());
			assertEquals(frame, game.scoredFrames());
		}
		assertEquals(60, game.score());
	}

	public void testSpareWithFrameCounting() throws Exception {
		assertEquals(0, game.scoredFrames());
		game.roll(4);
		assertEquals("4", arrayAsString(game.rollsForFrame(1)));
		game.roll(6);
		assertEquals(1, game.finishedFrames());
		assertEquals(0, game.scoredFrames()); // cant score it yet
		assertEquals("4 6", arrayAsString(game.rollsForFrame(1)));
		game.roll(5);
		assertEquals(1, game.scoredFrames()); // can score it now
		game.roll(3);
		assertEquals(2, game.finishedFrames());
		assertEquals(2, game.scoredFrames()); // score right away
		rollMany(16, 0);
		assertEquals(10, game.finishedFrames());
		assertTrue(game.gameOver());
		assertEquals(10, game.scoredFrames());
		assertEquals(23, game.score());
	}

	public void testSpareWithPinsStanding() throws Exception {
		assertEquals(10, game.pinsStanding());
		game.roll(4);
		assertEquals(6, game.pinsStanding());
		game.roll(6);
		assertEquals(10, game.pinsStanding());
		game.roll(5);
		assertEquals(5, game.pinsStanding());
	}

	private String arrayAsString(int[] intArray) {
		String arrayRepr = "";
		for (int i = 0; i < intArray.length; i++) {
			arrayRepr = arrayRepr + intArray[i] + " ";
		}
		return arrayRepr.trim();
	}

	public void testSpareAtEndWithFrameCounting() throws Exception {
		rollMany(18, 4);
		assertEquals(9, game.finishedFrames());
		assertEquals(9, game.scoredFrames());
		assertEquals(72, game.scoreForFrame(9));

		game.roll(7);
		assertEquals(9, game.finishedFrames());
		assertEquals(9, game.scoredFrames());

		assertEquals(3, game.pinsStanding());
		game.roll(3); // complete the spare
		assertEquals(9, game.finishedFrames());
		assertEquals(9, game.scoredFrames());

		assertEquals(10, game.pinsStanding());
		game.roll(6); // get an extra 6 pins, finishing the frame and game
		assertEquals(10, game.finishedFrames());
		assertEquals(10, game.scoredFrames());

		assertEquals(72 + 10 + 6, game.score());
	}

	public void testStrikeWithFrameCounting() throws Exception {
		game.roll(10);
		assertEquals(1, game.finishedFrames());
		assertEquals(0, game.scoredFrames());
		assertEquals(10, game.pinsStanding());
		game.roll(5);
		assertEquals(1, game.finishedFrames());
		assertEquals(0, game.scoredFrames());
		game.roll(3);
		assertEquals(2, game.finishedFrames());
		assertEquals(2, game.scoredFrames());
		// that roll scored frames 1 and 2
		game.roll(2);
		assertEquals(2, game.finishedFrames());
		game.roll(1);
		assertEquals(3, game.finishedFrames());
		assertEquals(3, game.scoredFrames());
		rollMany(14, 0);
		assertEquals(10, game.finishedFrames());
		assertEquals(10, game.scoredFrames());
		assertEquals(29, game.score());
		assertEquals("10", arrayAsString(game.rollsForFrame(1)));
		assertEquals("5 3", arrayAsString(game.rollsForFrame(2)));
	}

	public void testPerfectWithFrameCounting() throws Exception {
		for (int frame = 1; frame <= 9; frame++) {
			game.roll(10);
			assertEquals(frame, game.finishedFrames());
		}

		assertEquals(7, game.scoredFrames());
		game.roll(10);
		assertEquals(9, game.finishedFrames()); // done with 9
		assertEquals(8, game.scoredFrames());
		assertEquals("10", arrayAsString(game.rollsForFrame(10)));
		game.roll(10); // still done with 9
		assertEquals(9, game.finishedFrames());
		assertEquals(9, game.scoredFrames());
		assertEquals("10 10", arrayAsString(game.rollsForFrame(10)));
		game.roll(10); // finally, done with 9
		assertEquals(10, game.finishedFrames());
		assertEquals(10, game.scoredFrames());
		assertEquals(300, game.score());
		assertEquals("10 10 10", arrayAsString(game.rollsForFrame(10)));
	}

	public void testPerfectWithPinsStanding() throws Exception {
		for (int frame = 1; frame <= 9; frame++) {
			assertEquals(10, game.pinsStanding());
			game.roll(10);
		}

		assertEquals(10, game.pinsStanding());
		game.roll(10);
		assertEquals(10, game.pinsStanding());
		game.roll(10);
		assertEquals(10, game.pinsStanding());
		game.roll(10);
		assertEquals(300, game.score());
	}

	public void testKnockDownMoreThanThereAre() throws InvalidPinsException {
		game.roll(5);
		try {
			game.roll(6);
			fail("Should not have been able to roll 5,6");
		} catch (InvalidPinsException expected) {
		}
	}

	public void testStrikeWithInvalidBonusInLastFrame() throws Exception {
		rollMany(10, 10);
		game.roll(6);
		assertEquals(4, game.pinsStanding());
		try {
			game.roll(6);
			fail("Rolled 12 in bonus frame");
		} catch (InvalidPinsException expected) {
		}
	}
}
