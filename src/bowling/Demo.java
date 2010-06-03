/* Bowling Example, Copyright 2003 Kyle Cordes
 * http://kylecordes.com
 *
 * Try out the bowling code with a random game,
 * printing the results in HTML to a file
 */
package bowling;

import java.util.Random;

public class Demo {

	public static void main(String[] args) {
		try {
			Game game = new Game();
			Random rnd = new Random();
			HtmlGameRenderer renderer = new HtmlGameRenderer("bowling.html");
			try {
				renderer.printGame(game);
				while (!game.gameOver()) {
					// Bias our guy toward good rolls:
					int n = rnd.nextInt(game.pinsStanding() + 4);
					int pins = Math.min(n, game.pinsStanding());
					renderer.tellAboutPins(pins);
					game.roll(pins);
					renderer.printGame(game);
				}
			} finally {
				renderer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
