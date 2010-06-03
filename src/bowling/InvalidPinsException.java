/* Bowling Example, Copyright 2003 Kyle Cordes
 * http://kylecordes.com
 */
package bowling;

public class InvalidPinsException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidPinsException(String message) {
		super(message);
	}
}
