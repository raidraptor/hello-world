import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

// Change all player 2 to player -1

/**
 * A class to keep track of the board of the game
 * 
 * @author James Bai Marvin Li
 * @version December 25th, 2014
 */

// Give James Background and button to convert to jpg!

public class Board extends JPanel implements MouseListener,
		MouseMotionListener, ActionListener {
	// Constants for the overall layout.
	public static final int WIDTH = 960;
	public static final int HEIGHT = 960;
	public final static Image BACKGROUND = new ImageIcon(
			"images\\RN Background.jpg").getImage();
	public final static Image READY_BUTTON = new ImageIcon("images\\RN Ready.jpg").getImage();
	private final int ANIMATION_FRAMES = 6;

	// Constants for layout of the board.
	private final int NO_OF_ROWS = 8;
	private final int NO_OF_COLUMNS = 8;
	private final int STACK_AREA_CAPACITY = 7;
	private final int BOARD_X = 30;
	private final int BOARD_Y = 150;
	private final int STACK_AREA_X = 70;
	private final int PLAYER_1_STACK_AREA_Y = 10;
	private final int PLAYER_2_STACK_AREA_Y = 850;
	private final int READY_BUTTON_X = 730;
	private final int READY_BUTTON_Y = 430;
	private final Rectangle REKT = new Rectangle(READY_BUTTON_X,
			READY_BUTTON_Y, 160, 80);

	// Variables for the Rai Net Game
	private RaiNetMain parentFrame;
	private Tile[][] board;
	static Tile[] player1StackArea;
	static Tile[] player2StackArea;
	static int player2VirusCards;
	static int player2LinkCards;
	static int player1VirusCards;
	static int player1LinkCards;
	private Hand player1Hand;
	private Hand player2Hand;
	private Tile selectedTile;
	private Card selectedCard;
	static int playerTurn;
	private CardPiece movingPiece;
	private Point lastPoint;
	private boolean won;
	private boolean setUp;
	private boolean animate = false; // Dont forget to switch this to 'true'
										// later
	private JButton setButton;

	/**
	 * Constructs the Board, the player's Hands, and puts in the listeners.
	 * 
	 * @param parentFrame
	 *            the main Frame that holds this panel.
	 */
	public Board(RaiNetMain parentFrame) {
		// Set up the size and background colour
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.parentFrame = parentFrame; // We'll experiment with the
										// consequences of removing this later
										// to find out what this does.

		// Add mouse listeners to the Board
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		// Sets up the Board.
		board = new Tile[NO_OF_ROWS][NO_OF_COLUMNS];
		// Does the Board.
		int yPos = BOARD_Y;
		for (int row = 0; row < NO_OF_ROWS; row++) {
			int xPos = BOARD_X;
			for (int col = 0; col < NO_OF_COLUMNS; col++) {
				board[row][col] = new Tile(xPos, yPos, row, col);
				xPos += Tile.TILE_WIDTH;
			}
			yPos += Tile.TILE_HEIGHT;
		}

		// Then the Stack Areas
		// For Player 1.
		player1StackArea = new Tile[STACK_AREA_CAPACITY];
		yPos = PLAYER_1_STACK_AREA_Y;
		int xPos = STACK_AREA_X;
		for (int col = 0; col < STACK_AREA_CAPACITY; col++) {
			// -2 is used to indicate the Stack Area is 'above' the board.
			player1StackArea[col] = new Tile(xPos, yPos, -2, col);
			xPos += Tile.TILE_WIDTH;
		}
		// For Player 2
		player2StackArea = new Tile[STACK_AREA_CAPACITY];
		yPos = PLAYER_2_STACK_AREA_Y;
		xPos = STACK_AREA_X;
		for (int col = 0; col < STACK_AREA_CAPACITY; col++) {
			// 9 is used to indicate the Stack Area is 'below' the board.
			player2StackArea[col] = new Tile(xPos, yPos, 9, col);
			xPos += Tile.TILE_WIDTH;
		}
		// The Hands.
		// Add code.
		// Hands will be located right of the board.

		// We will need a menu screen.

		repaint();
	}

	/**
	 * Checks to see if the player has won by having 4 Link Cards in their Stack
	 * Area or 4 Virus Cards in their opponent's Stack Area.
	 * 
	 * @return 0 if noone has won; 1 if player 1 has won; 2 if player 2 has won.
	 */
	private int checkForWinner() {
		if (player2LinkCards == 4 || player1VirusCards == 4)
			return 2;
		if (player1LinkCards == 4 || player2VirusCards == 4)
			return 1;

		// No player has won yet.
		return 0;
	}

	/**
	 * Delays the given number of milliseconds
	 * 
	 * @param milliSec
	 *            number of milliseconds to delay
	 */
	private void delay(int milliSec) {
		try {
			Thread.sleep(milliSec);
		} catch (Exception e) {
		}
	}

	/**
	 * Handles the mouse moved events to show which Cards may be selected.
	 * 
	 * @param event
	 *            event information for mouse moved
	 */
	public void mouseMoved(MouseEvent event) {
		// Set the cursor to the hand if we are on a card that we can pick up
		Point currentPoint = event.getPoint();

		if (setUp) {
			// Searches through the board.
			for (Tile[] nextRow : board)
				for (Tile nextTile : nextRow)
					if (nextTile.hasPiece()
							&& nextTile.tile.get(0).contains(currentPoint)) // Make
																			// it
																			// work
																			// for
																			// same
																			// column?
						selectedCard = nextTile.tile.get(0);
		}

		// The mouse is hovering over a Card.
		for (Tile[] nextRow : board)
			for (Tile nextTile : nextRow)
				if (nextTile.hasPiece() && nextTile.contains(currentPoint)
						&& nextTile.canPickUp()) {
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					return;
				}

		// Otherwise we just use the default cursor.
		setCursor(Cursor.getDefaultCursor());
	}

	/**
	 * Handles the mouse pressed events to select a card.
	 * 
	 * @param event
	 *            event information for mouse pressed
	 */
	public void mousePressed(MouseEvent event) {

		// Add code so that the selected card has a marker or something on it.
		// Similar to what happens in dn when you click a card.
		Point selectedPoint = event.getPoint();

		lastPoint = selectedPoint;

		// To pick up a piece.
		if (selectedCard == null) {
			for (Tile[] nextRow : board)
				for (Tile nextTile : nextRow) {
					if (nextTile.contains(selectedPoint) && nextTile.hasPiece()
							&& nextTile.canPickUp()) {
						// If right click is held down.
						if (!setUp && event.getButton() == MouseEvent.BUTTON3) {
							nextTile.getPiece().flip();
						} else {
							selectedCard = nextTile.removePiece();
							lastPoint = selectedPoint;
						}

						repaint();
						return;
					}
				}

		}
		repaint();
	}

	/**
	 * Moves a Piece during the animation
	 * 
	 * @param PieceToMove
	 *            the CardPiece that you want to move
	 * @param fromPos
	 *            the original position of the Piece
	 * @param toPos
	 *            the destination position of the piece
	 */
	public void moveAPiece(final CardPiece PieceToMove, Point fromPos,
			Point toPos) {
		int dx = (toPos.x - fromPos.x) / ANIMATION_FRAMES; // We might want to
															// refractor this
															// variable.
		int dy = (toPos.y - fromPos.y) / ANIMATION_FRAMES; // As well as this
															// one.

		for (int times = 1; times <= ANIMATION_FRAMES; times++) // And this one.
		{
			fromPos.x += dx;
			fromPos.y += dy;
			PieceToMove.setPosition(fromPos);

			// Update the drawing area.
			paintImmediately(0, 0, getWidth(), getHeight());
			delay(30); // Maybe change the number in here.

		}
		PieceToMove.setPosition(toPos);
		repaint();
	}

	/**
	 * Makes a new card by resetting the board and the hands.
	 */
	public void newGame() {

		won = false;

		// Resets the Board.
		for (Tile[] nextRow : board)
			for (Tile nextTile : nextRow)
				if (nextTile.hasPiece())
					nextTile.removePiece();
		board[0][0].addPiece(new CardPiece(new Point(0, 0), false, 1));
		board[0][1].addPiece(new CardPiece(new Point(0, 0), false, 1));
		board[0][2].addPiece(new CardPiece(new Point(0, 0), false, 1));
		board[1][3].addPiece(new CardPiece(new Point(0, 0), false, 1));
		board[1][4].addPiece(new CardPiece(new Point(0, 0), true, 1));
		board[0][5].addPiece(new CardPiece(new Point(0, 0), true, 1));
		board[0][6].addPiece(new CardPiece(new Point(0, 0), true, 1));
		board[0][7].addPiece(new CardPiece(new Point(0, 0), true, 1));
		board[7][0].addPiece(new CardPiece(new Point(0, 0), false, -1));
		board[7][1].addPiece(new CardPiece(new Point(0, 0), false, -1));
		board[7][2].addPiece(new CardPiece(new Point(0, 0), false, -1));
		board[6][3].addPiece(new CardPiece(new Point(0, 0), false, -1));
		board[6][4].addPiece(new CardPiece(new Point(0, 0), true, -1));
		board[7][5].addPiece(new CardPiece(new Point(0, 0), true, -1));
		board[7][6].addPiece(new CardPiece(new Point(0, 0), true, -1));
		board[7][7].addPiece(new CardPiece(new Point(0, 0), true, -1));

		// Clears the Stack Area.
		// Player 1 First.
		for (Tile nextTile : player1StackArea)
			if (nextTile.hasPiece())
				nextTile.removePiece();

		// Then Player 2
		for (Tile nextTile : player2StackArea)
			if (nextTile.hasPiece())
				nextTile.removePiece();

		player2LinkCards = 0;
		player2VirusCards = 0;
		player1LinkCards = 0;
		player1VirusCards = 0;

		// Resets the hands.
		// We need to add in the code for this after we finish the Terminal
		// Cards

		playerTurn = -1;
		setUp = true;
		setButton = new JButton("Ready");
		setButton.setBounds(270, 430, 160, 80);
		this.add(setButton);
		setButton.addActionListener(this);
		repaint();
	}

	/**
	 * Draws out the pieces, the cards, and the animations.
	 * 
	 * @param g
	 *            the Graphics context to do the drawing
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// The background.
		g.drawImage(BACKGROUND, 0, 0, null);
		
		// Draws the setUp button when necessary.
		if (setUp)
			g.drawImage(READY_BUTTON, READY_BUTTON_X, READY_BUTTON_Y, null);
			
		// Draws out the pieces.
		for (Tile[] nextRow : board)
			for (Tile nextTile : nextRow)
				nextTile.draw(g);
		if (selectedCard != null)
			selectedCard.draw(g);

		// Draws out the Stack Areas.
		// Player 1.
		for (Tile nextTile : player1StackArea)
			nextTile.draw(g);
		for (Tile nextTile : player2StackArea)
			nextTile.draw(g);

		// Draws out the player's hands.
		// for (Card nextCard: player1Hand.hand)
		// nextCard.draw(g);
		// for (Card nextCard: player2Hand.hand)
		// nextCard.draw(g);
	}

	@Override
	public void mouseDragged(MouseEvent event) {

		Point currentPoint = event.getPoint();

		if (selectedCard != null) {
			selectedCard.move(lastPoint, currentPoint);
			lastPoint = currentPoint;
			repaint();
		}

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent event) {

		Point currentPoint = event.getPoint();

		if (selectedCard != null) {
			if (setUp) {
				// Searches through the board.
				for (Tile[] nextRow : board)
					for (Tile nextTile : nextRow)
						if (nextTile.hasPiece()
								&& nextTile.contains(currentPoint)
								&& ((CardPiece) selectedCard).canSwap(nextTile)) {
							((CardPiece) selectedCard).swap(nextTile);
							selectedCard = null;
							repaint();
							return;
						}

				// if (setUp && selectedCard != null) {
				// // Moves the card.
				// // Searches through the board.
				// for (Tile[] nextRow : board)
				// for (Tile nextTile : nextRow)
				// if (nextTile.hasPiece() && nextTile.contains(currentPoint)
				// && ((CardPiece) selectedCard).canSwap(nextTile)) {
				// ((CardPiece) selectedCard).swap(nextTile);
				// repaint();
				// selectedCard = null;
				// // Swap selected card with card on tile if overlapping.
				// // Card tempCard = nextTile.tile.remove(0);
				// // nextTile.tile.add(selectedCard);
				// // selectedCard = tempCard;
				// }
				// }
				if (selectedCard != null) {
					selectedCard.move(lastPoint, currentPoint);
					lastPoint = currentPoint;
					repaint();
				}
			} else {
				// Searches the stack area.
				if (playerTurn == 1) {
					for (Tile nextTile : player1StackArea) {
						if (nextTile.contains(currentPoint)
								&& ((CardPiece) selectedCard)
										.canLeaveTo(nextTile)) {
							((CardPiece) selectedCard).moveTo(nextTile);
							selectedCard = null;
							playerTurn *= -1;
							repaint();
							if (!won && checkForWinner() != 0) {
								JOptionPane
										.showMessageDialog(parentFrame,
												"Player " + checkForWinner()
														+ " wins!",
												"Congratulations!",
												JOptionPane.INFORMATION_MESSAGE);
								// To prevent the message from popping up more
								// than once
								won = true;
							}
							return;
						}
					}
				} else {
					for (Tile nextTile : player2StackArea) {
						if (nextTile.contains(currentPoint)
								&& ((CardPiece) selectedCard)
										.canLeaveTo(nextTile)) {
							((CardPiece) selectedCard).moveTo(nextTile);
							selectedCard = null;
							playerTurn *= -1;
							repaint();
							if (!won && checkForWinner() != 0) {
								JOptionPane
										.showMessageDialog(parentFrame,
												"Player " + checkForWinner()
														+ " wins!",
												"Congratulations!",
												JOptionPane.INFORMATION_MESSAGE);
								// To prevent the message from popping up more
								// than once
								won = true;
							}
							return;
						}
					}
				}
				// Searches the board.
				for (Tile[] nextRow : board)
					for (Tile nextTile : nextRow) {
						if (nextTile.contains(currentPoint)
								&& ((CardPiece) selectedCard)
										.canMoveTo(nextTile)) {

							((CardPiece) selectedCard).moveTo(nextTile);
							selectedCard = null;
							playerTurn *= -1;
							repaint();
							if (!won && checkForWinner() != 0) {
								JOptionPane
										.showMessageDialog(parentFrame,
												"Player " + checkForWinner()
														+ " wins!",
												"Congratulations!",
												JOptionPane.INFORMATION_MESSAGE);
								// To prevent the message from popping up more
								// than once
								won = true;
							}
							return;
						}
					}
			}

			((CardPiece) selectedCard).back();
			selectedCard = null;

		}

		repaint();
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == setButton) {
			if (playerTurn == 1) {
				setUp = false;
				this.remove(setButton);

			}
			playerTurn *= -1;
			for (Tile[] nextRow : board)
				for (Tile nextTile : nextRow) {
					if (!nextTile.tile.isEmpty()) {
						if (nextTile.getPiece().owner != playerTurn)
							nextTile.getPiece().flip();
					}
					repaint();
				}
		}
	}
}
