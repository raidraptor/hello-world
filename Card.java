import java.awt.Graphics;
import java.awt.Point;

/**
 * Stores data on all cards.
 * 
 * @author Marvin Li and James Bai
 * @version January 4th, 2015
 */
public abstract class Card {

	// Care for a better name? not really important but descriptive names are
	// nice.
	protected Point position;
	int owner;

	/**
	 * Creates a Card object
	 * 
	 * @param owner
	 *            The player who is in control of the card.
	 * @param position
	 *            The position of the card on the board.
	 */
	public Card(int owner, Point position) {
		this.owner = owner;
		this.position = position;
	}

	/**
	 * Determines if the selected point is within the card.
	 * 
	 * @param selectedPoint
	 *            the selected point
	 * @return true if the selected point is within the card. false otherwise.
	 */
	public boolean contains(Point selectedPoint) {
		// Add code here; pull it in from Card class from Freecell or something.
		return false;
	}

	// May or may not be in the wrong place.
	/**
	 * Draws the card.
	 * 
	 * @param g
	 */
	public abstract void draw(Graphics g);

	/**
	 * Moves the piece
	 * 
	 * @param initialPos
	 *            The original position of the card.
	 * @param finalPos
	 *            The final position of the card.
	 */
	public void move(Point initialPos, Point finalPos) {
		position.x += finalPos.x - initialPos.x;
		position.y += finalPos.y - initialPos.y;
	}

	/**
	 * Sets the card's position to the designated point.
	 * 
	 * @param newPos
	 *            the new position of the card.
	 */
	public void setPosition(Point newPos) {
		this.position = newPos;
	}
}
