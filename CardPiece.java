import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;

/**
 * Stores the data on all Link Cards and Virus Cards.
 * 
 * @author Marvin Li and James Bai
 * @version January 4th, 2015
 */

// Note: row and col are reversed; it is not very intuitive.
public class CardPiece extends Card implements Movable {
	// public final static Image LINK_IMAGE1 = new ImageIcon(
	// "images\\RN Link B1.jpg").getImage();
	// public final static Image Virus_IMAGE1 = new ImageIcon(
	// "images\\RN Virus B1.jpg").getImage();
	// public final static Image LINK_IMAGE2 = new ImageIcon(
	// "images\\RN Link B2.jpg").getImage();
	// public final static Image Virus_IMAGE2 = new ImageIcon(
	// "images\\RN Virus B2.jpg").getImage();
	public final static Image BACK_IMAGE1 = new ImageIcon(
			"images\\RN Back 1.jpg").getImage();
	public final static Image BACK_IMAGE2 = new ImageIcon(
			"images\\RN Back 2.jpg").getImage();

	Tile fromTile;
	boolean isVirus;
	private Image image;
	// boolean lineBoost; // Deal with this later.
	boolean isFaceUp;

	/**
	 * Creates a new cardPiece object.
	 * 
	 * @param position
	 *            the position of the cardPiece object.
	 * @param isVirus
	 *            true if this cardPiece is a virus, false if it is a link.
	 * @param owner
	 *            the player who is in possession of the cardPiece.
	 */
	public CardPiece(Point position, boolean isVirus, int owner) {
		super(owner, position);
		this.isVirus = isVirus;
		isFaceUp = true;
		String fileName = "images//RN ";
		if (isVirus)
			fileName += "Virus B";
		else
			fileName += "Link B";
		if (owner == 1)
			fileName += "2.jpg";
		else
			fileName += "1.jpg";
		image = new ImageIcon(fileName).getImage();
	}

	/**
	 * Checks if a certain move is valid.
	 * 
	 * @param otherTile
	 *            the destination tile.
	 * @return true if the move is valid, false otherwise.
	 */
	public boolean canMoveTo(Tile otherTile) {
		if (otherTile.hasPiece() && otherTile.getPiece().owner == this.owner)
			return false;
		if ((Math.abs(fromTile.row - otherTile.row) == 1 && fromTile.col == otherTile.col)
				|| (Math.abs(fromTile.col - otherTile.col) == 1 && fromTile.row == otherTile.row))
			return true;
		return false;
	}

	/**
	 * Checks if the card can leave to the specified slot in the Stack Area
	 * 
	 * @param otherTile
	 *            the specified slot in the Stack Area.
	 * @return true if the card can leave to the specified slot in the Stack
	 *         Area; false otherwise.
	 */
	public boolean canLeaveTo(Tile otherTile) {
		if (Board.playerTurn == 1) {
			if (!otherTile.hasPiece() && (fromTile.row == 7)
					&& (fromTile.col == 3 || fromTile.col == 4))
				return true;
		} else {
			if (!otherTile.hasPiece() && (fromTile.row == 0)
					&& ((fromTile.col == 3 || fromTile.col == 4)))
				return true;
		}
		return false;
	}

	// Captured moved to Tile class along with 'leave'

	/**
	 * Returns the piece to the tile it came from.
	 * 
	 * @param fromTile
	 *            The tile the piece came from.
	 */
	public void back() {

		fromTile.addPiece(this);
		;
	}

	/**
	 * Draws the cardPiece object.
	 * 
	 * @param g
	 */
	public void draw(Graphics g) {
		if (isFaceUp) {
			g.drawImage(image, position.x + 13, position.y + 3, null);
		} else {
			if (owner == 1)
				g.drawImage(BACK_IMAGE2, position.x + 13, position.y + 3, null);
			else
				g.drawImage(BACK_IMAGE1, position.x + 13, position.y + 3, null);
		}
	}

	// Comments~
	public void flip() {
		if (isFaceUp)
			isFaceUp = false;
		else
			isFaceUp = true;
		return;
	}

	/**
	 * Removes the CardPiece from the tile and adds 1 to the owner's link or
	 * virus cards.
	 */
	public void leave() {
		// Modify variables first.
		if (owner == 1) {
			if (isVirus)
				Board.player2VirusCards++;
			else {
				Board.player2LinkCards++;
			}
		} else {
			if (isVirus)
				Board.player1VirusCards++;
			else {
				Board.player1LinkCards++;
			}
			
		}

		System.out.println(Board.player1VirusCards + " : "
				+ Board.player1LinkCards + " - " + Board.player2VirusCards
				+ " : " + Board.player2LinkCards);
	}

	/**
	 * Adds the cardPiece to the tile
	 * 
	 * @param otherTile
	 *            the destination tile.
	 */
	public void moveTo(Tile otherTile) {
		// Also, pieces with line boost may not jump.

		// Piece is leaving to Stack Area.
		if (!otherTile.hasPiece() && (otherTile.row > 7 || otherTile.row < 0))
			leave();
		// Piece is moving over opponent's piece.
		else if (otherTile.hasPiece()
				&& otherTile.getPiece().owner != this.owner)
			otherTile.captured();
		otherTile.addPiece(this);
	}

	// intro
	public boolean canSwap(Tile toTile) {
		return (toTile.hasPiece() && toTile.getPiece().owner == this.owner);
	}

	public void swap(Tile toTile) {

		fromTile.addPiece(toTile.removePiece());
		toTile.addPiece(this);

	}
}
