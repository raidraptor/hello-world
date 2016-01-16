import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main Frame for a the Rai Net Game.
 * Sets up the menus and places the Board in the Frame
 * 
 * @author Marvin Li and James Bai
 * @version December 25th 2014
 */
public class RaiNetMain extends JFrame implements ActionListener
{
	private Board board;
	private JMenuItem newMenuItem, quitMenuItem, aboutMenuItem;

	/** 
	 * Creates a RaiNetMain from object
	 */
	public RaiNetMain()
	{
		super("Rai Net"); // Double Check This
		setResizable(true);

		// Add in an Icon.
		setIconImage(new ImageIcon("notAnImage.png").getImage()); // James, the icon.

		// Add in a Simple Menu
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		gameMenu.setMnemonic('G');
		newMenuItem = new JMenuItem("New Game");
		newMenuItem.addActionListener(this);

		quitMenuItem = new JMenuItem("Exit");
		quitMenuItem.addActionListener(this);

		gameMenu.add(newMenuItem);
		gameMenu.addSeparator();
		gameMenu.add(quitMenuItem);
		menuBar.add(gameMenu);

		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');
		aboutMenuItem = new JMenuItem("About...");
		aboutMenuItem.addActionListener(this);
		helpMenu.add(aboutMenuItem);
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);

		// Set up the layout and add in the Board
		setLayout(new BorderLayout());
		board = new Board(this);
		add(board, BorderLayout.CENTER);
		JScrollPane scrollPane = new JScrollPane(board);
		add(scrollPane, BorderLayout.CENTER);
		
		// (Try to) Centre the frame in the middle of the screen
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		this.setVisible(true);
		setLocation((screen.width - Board.WIDTH) / 2 - this.getWidth(),
				(screen.height - Board.HEIGHT) / 2 - this.getHeight());
	}

	/**
	 * Method that deals with the menu options
	 * 
	 * @param event the event that triggered this method
	 */
	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == newMenuItem)
		{
			board.newGame();
		}
		else if (event.getSource() == quitMenuItem)
		{
			System.exit(0);
		}
		else if (event.getSource() == aboutMenuItem)
		{
			JOptionPane.showMessageDialog(board,
					"Rai Net by:\n Marvin Li and James Bai\n\u00a9 2014\n\nIdea from Rai-Net Access Battlers\nSteins; Gate",
					"About Rai Net", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public static void main(String[] args)
	{
		RaiNetMain frame = new RaiNetMain();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
