/** Builds application window and creates tabs with games
*
* @author Stanislav Smatana, Dominika Fabcinova
*/


import model.exceptions.IllegalTurnException;
import model.notation.StandartNotation;
import model.basis.Figure.Color;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SwingConstants;

import org.dom4j.DocumentException;

import view.gui.GUIView;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import controller.Game;
import controller.Replay;
import controller.exceptions.CompletedGameLoadException;
import controller.interfaces.ControllerInterface;
import controller.player.ComputerPlayer;
import controller.player.LocalPlayer;
import controller.player.NetworkPlayer;
import controller.network.*;
import controller.network.GamePacket.PacketType;


/** Main class of application
 * 
 * @author Stanislav Smatana, Dominika Fabcinova
 *
 */
public class MainApp implements ActionListener{

	
	/**************************************** CONSTANTS **********************************************/
	static final int SERVER_PORT = 50000;
	
	
	
	/**************************************** ATRIBUTES *********************************************/
	private static JFrame frmDama;
	private static JMenuBar menuBar;
	private static JTabbedPane tabbedPane;
	
	//List of currently played games
	List<ControllerInterface> games = new ArrayList<ControllerInterface>();
	
	//Socket for listening for games
	ServerSocket serv_sock;
	
	/**************************************** MAIN **************************************************/
	public static void main(String[] args) 
	{
		MainApp app = new MainApp();
		app.startInvitationListener(); //Start listener for invitations
		app.buildWindow();
		

	}
	
	/**************************************** Utility methods ***************************************/
	
	/** Method pops-up error dialog with given message */
	public static void error(String msg)
	{
		JOptionPane.showMessageDialog(null,msg, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	/** Method pop-ups info message*/
	public static void info(String msg)
	{
		JOptionPane.showMessageDialog(null,msg, "Info", JOptionPane.INFORMATION_MESSAGE);
	}
	
	
	/** Method builds basic app window.  * */
	public void buildWindow()
	{
		frmDama = new JFrame();
		frmDama.getContentPane().setFont(new Font("Dialog", Font.PLAIN, 15));
		frmDama.setIconImage(Toolkit.getDefaultToolkit().getImage("media/mainFrameIcon.png"));
		frmDama.setResizable(false);
		frmDama.setTitle("Checkers");
		frmDama.setBounds(100, 100, 608, 349);
		frmDama.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		menuBar = new JMenuBar();
		frmDama.setJMenuBar(menuBar);
		
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 606, 298);
		frmDama.getContentPane().add(tabbedPane);
		
		JMenu mnGame = new JMenu("Game");
		menuBar.add(mnGame);
		
		JMenu mnNew = new JMenu("New");
		mnGame.add(mnNew);
		
		JMenuItem mntmLocal = new JMenuItem("Local");
		mnNew.add(mntmLocal);
		mntmLocal.setActionCommand("newLocal");
		mntmLocal.addActionListener(this);
		
		JMenuItem mntmComputer = new JMenuItem("Computer");
		mnNew.add(mntmComputer);
		mntmComputer.setActionCommand("newComputer");
		mntmComputer.addActionListener(this);
		
		JMenuItem mntmNetwork = new JMenuItem("Network");
		mnNew.add(mntmNetwork);
		mntmNetwork.setActionCommand("newNetwork");
		mntmNetwork.addActionListener(this);
		
		//4 fun
		JMenuItem mntmGenerate = new JMenuItem("Generate");
		mnNew.add(mntmGenerate);
		mntmGenerate.addActionListener(this);
		
		JMenuItem mntmNotation = new JMenuItem("Notation");
		mnNew.add(mntmNotation);
		mntmNotation.addActionListener(this);
		
		JMenu mnLoad = new JMenu("Load");
		mnGame.add(mnLoad);
		
		JMenuItem mntmReplay = new JMenuItem("Replay");
		mnLoad.add(mntmReplay);
		mntmReplay.addActionListener(this);
		
		JMenu mnContinue = new JMenu("Continue playing");
		mnLoad.add(mnContinue);
		JMenuItem loadLocal = new JMenuItem("Local");
		mnContinue.add(loadLocal);
		loadLocal.addActionListener(this);
		loadLocal.setActionCommand("loadLocal");
		
		JMenuItem loadComputer = new JMenuItem("Computer");
		mnContinue.add(loadComputer);
		loadComputer.addActionListener(this);
		loadComputer.setActionCommand("loadComputer");
		
		JMenuItem loadNetwork = new JMenuItem("Network");
		mnContinue.add(loadNetwork);
		loadNetwork.addActionListener(this);
		loadNetwork.setActionCommand("loadNetwork");
		
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mnGame.add(mntmSave);
		mntmSave.addActionListener(this);
		
		JMenu mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);
		
		
		JMenuItem mntmReplayDelay = new JMenuItem("Set replay delay");
		mnOptions.add(mntmReplayDelay);
		mntmReplayDelay.addActionListener(this);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmGameRules = new JMenuItem("Game rules");
		mnHelp.add(mntmGameRules);
		mntmGameRules.addActionListener(this);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		frmDama.getContentPane().setLayout(null);
		mntmAbout.addActionListener(this);
		
		frmDama.setVisible(true);
		
		//adding tab
	}
	
	

	/**************************************** MENU ***************************************/
	@Override
	/** Method handles events from menu. 
	 * 
	 */
	public void actionPerformed(ActionEvent event) {
		
		ControllerInterface controller=null;
		String description = "";
		
		switch(event.getActionCommand())
		{
			//Closing of tab
			case "x":
				JButton button = (JButton) event.getSource();
				JPanel tab = (JPanel) button.getParent();
				int index = tabbedPane.indexOfTabComponent(tab);
				games.get(index).end(false, "User ended game");
				games.remove(index);
				tabbedPane.remove(index);
				return;
			//New local game
			case "newLocal":
				controller = new Game( new LocalPlayer(Color.WHITE), new LocalPlayer(Color.BLACK) );
				description = "Local Game";
				break;
			//New computer game
			case "newComputer":
				controller = new Game( new LocalPlayer(Color.WHITE), new ComputerPlayer(Color.BLACK) );
				description = "Computer Game";
				break;
			//New simulated PC vs PC game
			case "Generate":
				Game simulation = new Game( new ComputerPlayer(Color.WHITE), new ComputerPlayer(Color.BLACK) );
				simulation.start();
				controller = new Replay(simulation.getModel());
				description = "Generated game";
				break;
			//Load game and continue with PC
			case "loadComputer":
				description = "Local Game";
				controller = startSavedGame(true);
				break;
			//Load game and continue with human player
			case "loadLocal":
				description = "Computer Game";
				controller = startSavedGame(false);
				break;
			//Load replay
			case "Replay":
				description = "Replay";
				controller = startReplay();
				break;
			
			//Start network game => return needed, runs in thread
			case "newNetwork":
				startNetworkGame(false);
				return;
			//Load network game 
			case "loadNetwork":
				startNetworkGame(true);
				return;
			//Save game 
			case "Save":
				saveActiveGame();
				return;
			//Return -> no game is going to be played
			case "Set replay delay":
				showDialogSetReplayDelay();
				return;
			//Start new game based on typed notation
			case "Notation":
				showDialogNotation();
				return;
				
			default:
				return;
		}
		
		if(controller == null)
			return;

		//add game to list of games
		games.add(controller);
		
		//add as tab
		addTab(controller,description);
		
		//start game
		controller.start();
	}

	/*************************************** GUI UTILITIES ******************************************/
	
	/** Method shows notation dialogue and starts game based on user entered notation
	 *  
	 */
	private void showDialogNotation() {
		JPanel p = new JPanel(); 
		
		JTextArea txtrMoves = new JTextArea();
		txtrMoves.setPreferredSize(new Dimension(200, 200));
		
		JScrollPane sp = new JScrollPane(txtrMoves);
		sp.setPreferredSize(new Dimension(200, 200));
		p.add(sp);
		
		JOptionPane.showMessageDialog (frmDama, p, "Setup game from notation", JOptionPane.PLAIN_MESSAGE);
		
		String s = txtrMoves.getText(); // HERE IS THE NOTATION FROM USER
		
		try {
			Replay game = new Replay( (new StandartNotation()).decode(s)); 
			games.add(game);
			addTab(game, "Replay");
		} catch (IllegalTurnException e) {
			error("You entered illegal notation description");
		}
		
	}

	/** Method adds new tab to tabbed pane and game list.
	 * 
	 * @param controller Game to add to list.
	 * @param description Heading of new tab.
	 */
	private void addTab(ControllerInterface controller,String description)
	{
		tabbedPane.add(((GUIView)controller.getView()).getPanel(), description);

		int index = tabbedPane.indexOfComponent(((GUIView)controller.getView()).getPanel());
		
		JPanel tabHead = new JPanel(new GridBagLayout());
		tabHead.setBackground(null);
		
		JLabel lblTitle = new JLabel(description + "  ");
		
		JButton btnClose = new JButton("x");
		btnClose.setContentAreaFilled(false);
		btnClose.setBorder(null);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		
		tabHead.add(lblTitle, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0;
		tabHead.add(btnClose, gbc);
		
		tabbedPane.setTabComponentAt(index, tabHead);
		
		btnClose.addActionListener(this);
	}

	/** Method shows dialog for setting replay delay time. */
	private void showDialogSetReplayDelay() {
		// TODO Auto-generated method stub
		int new_delay = 500;
		
		String input = (String) JOptionPane.showInputDialog(
                	   frmDama,
                	   "Choose delay between moves when replaying a game:",
                	   "Set replay delay",
                	   JOptionPane.PLAIN_MESSAGE,
                	   null,
                	   null,
                	   null);
		
		try {
			new_delay = Integer.parseInt(input);
		} catch (NumberFormatException e1) { 
			error("Delay has to be a number"); // creating new game should be terminated
		}
		
		Replay.setReplayTime(new_delay);
	}
	
	
	/**  Method saves game on currently selected tab */
	public void saveActiveGame()
	{
		//no active games -> nothing to do
		if(games.size() == 0)
			return;
		
		final JFileChooser fc = new JFileChooser();
		
		//TODO: Platform independent separator
		fc.setCurrentDirectory(new File("saves"));
		
		fc.showOpenDialog(null);
		File file = fc.getSelectedFile();
		
		if(file == null)
			return;
		
		try
		{
			games.get(tabbedPane.getSelectedIndex()).save(file);
		}
		catch (IOException e)
		{
			error("Unable to open file during save");
		}
	}
	
	/*******************************HELPERS FOR STARTING GAMES *****************************/

	/** Helper method to start saved game
	 * 
	 * Method also brings up file chooser dialog.
	 *  
	 * @param is_computer If true game will be played against computer. If false against human.
	 * @return controller for this game
	 */
	public ControllerInterface startSavedGame(boolean is_computer) 
	{
		final JFileChooser fc = new JFileChooser();
		
		//TODO: Platform independent separator
		fc.setCurrentDirectory(new File("./saves"));
		fc.showOpenDialog(null);
		File file = fc.getSelectedFile();
		
		//no file selected
		if(file == null)
			return null;
			
		try{
			if(is_computer)
				return new Game(file,new LocalPlayer(Color.WHITE), new ComputerPlayer(Color.BLACK));
			else
				return new Game(file,new LocalPlayer(Color.WHITE), new LocalPlayer(Color.BLACK));
	
		}
		catch(IllegalTurnException e)
		{
			error("Illegal turn in savefile");
			return null;
		}
		catch (IOException e)
		{
			error("Can't open saved game");
			return null;
		}
		catch (DocumentException e)
		{
			error("Malformed saved game file format");
			return null;
			
		}
		catch (CompletedGameLoadException e) {
			// TODO Auto-generated catch block
			error("Game you specified is already completed");
			return null;
		}


	}
	
	/** Helper method to start replay
	 * 
	 * Method also brings up file chooser dialog.
	 *
	 * @return Replay controller
	 */
	public Replay startReplay() 
	{
		final JFileChooser fc = new JFileChooser();
		
		//TODO: Platform independent separator
		fc.setCurrentDirectory(new File("./saves"));
		fc.showOpenDialog(null);
		File file = fc.getSelectedFile();
		
		if(file == null)
			return null;
			
		try{
			return new Replay(file);
		}
		catch(IllegalTurnException e)
		{
			error("Nespravny tah v savefile");
		}
		catch (IOException e)
		{
			error("Nemozno otvorit subor");			
		}
		catch (DocumentException e)
		{
			error("Subor ma nespravny format");
		}

		return null;
	}
	

	
	/** Helper method to start either saved or new network game
	 * 
	 * Method also brings up file chooser dialog if necessary.
	 *  
	 * @param from_file If true, game will be loaded from file.
	 * @return controller for this game
	 */
	private ControllerInterface startNetworkGame(boolean from_file) {
		
				
		class netThread extends Thread
		{
			
		//implicit values, changed after calling method setNetworkProperties
		private String IP = "localhost";
		private int port = 50000;
		private Color color = Color.WHITE;
		private boolean from_file = false;
		private File file;
		
		public netThread(boolean from_file)
		{
			this.from_file = from_file;
		}
		
		public void run(){
			
		//TODO: Dialog here	
		String game_xml = "<game></game>";
		if(from_file)
		{
			final JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File("saves"));
			fc.showOpenDialog(null);
			file = fc.getSelectedFile();
			
			try{
				game_xml = (new Replay(file)).getModel().toXML();
			}
			catch(IllegalTurnException e)
			{
				error("Illegal turn in savefile");
				return ;
			}
			catch (IOException e)
			{
				error("Can't open saved game");
				return ;
			}
			catch (DocumentException e)
			{
				error("Malformed saved game file format");
				return ;
				
			}
		
		}
			
		setNetworkGameProperties(IP, port, color);
			
		Socket clientSocket=null;
		
		try {
			clientSocket = new Socket(IP,port); 

		} catch (UnknownHostException e) {
	
			error("Unable to resolve given hostname: ");
			return;
		} catch (IOException e) {
		
			error("Unable to connect to server");
			return;
		}
		
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
	
			error("Error during network game establishment");
			return;
		}
		
		try {
			
			oos.writeObject((new GamePacket(PacketType.INVITE,"null",game_xml,color)).toString());
			
				
				
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			error("Unable to send game invitation to");
			return;
		}
		
		ObjectInputStream ios = null;
		try {
			ios = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException e2) {
			
			e2.printStackTrace();
		}
		
		
		String response;
		try {
			response = (String) ios.readObject();
				
		} catch (IOException | ClassNotFoundException e1) {

			e1.printStackTrace();
			return;
		}
		
		GamePacket resp = null; 
		try {
			resp = new GamePacket(response);
		} catch (DocumentException e1) {

			error("Opponent sent malformed network message, unable to continue");
		}
	
		//game was rejected
		if(resp.getType() == GamePacket.PacketType.END)
		{
			error("Opponent rejected your game invitation");
			return;
		}
			
		//if it was not rejection it SHOULD be only accept
		if(resp.getType() != GamePacket.PacketType.ACCEPT)
		{
			error("Wrong packet type recieved");
			return;
		}
		
		
		//accepted
		info("Your invitation was accepted");
		
		Game network_game;
		if(from_file)
		{
			try
			{
				if(color == Color.BLACK)
					network_game = new Game(file,new NetworkPlayer(clientSocket,ios, oos, Color.WHITE),new LocalPlayer(Color.BLACK) );
				else
					network_game = new Game(file,new LocalPlayer(Color.WHITE),new NetworkPlayer(clientSocket,ios, oos, Color.BLACK) );
			}
			catch(IllegalTurnException e)
			{
				error("Illegal turn in savefile");
				return ;
			}
			catch (IOException e)
			{
				error("Can't open saved game");
				return ;
			}
			catch (DocumentException e)
			{
				error("Malformed saved game file format");
				return ;
				
			}
			catch (CompletedGameLoadException e) {
				// TODO Auto-generated catch block
				error("Game you specified is already completed");
				return ;
			}
			
		}
		else
		{
			if(color == Color.BLACK)
				 network_game = new Game(new NetworkPlayer(clientSocket,ios, oos, Color.WHITE),new LocalPlayer(Color.BLACK) );
			else
				network_game = new Game(new LocalPlayer(Color.WHITE),new NetworkPlayer(clientSocket,ios, oos, Color.BLACK) );
			
		}
		
		network_game.start();
		//add game
		games.add(network_game);
		addTab(network_game,"Network Game");
		
		
		}/*end run()*/

		//THIS IS INSIDE CLASS !
		private void setNetworkGameProperties(String IP, int port, Color color)
		{
			// TODO Auto-generated method stub
			JPanel p = new JPanel(new BorderLayout(5,5)); 
			
			JPanel labels = new JPanel(new GridLayout(0,1,2,2));
			labels.add(new JLabel("IP address:", SwingConstants.RIGHT));
			labels.add(new JLabel("Port:", SwingConstants.RIGHT));
			labels.add(new JLabel("Your color:", SwingConstants.RIGHT));
			labels.add(new JLabel("Start game:", SwingConstants.RIGHT));
			p.add(labels, BorderLayout.WEST);
			
			JPanel controls = new JPanel(new GridLayout(0,1,2,2));
		
			JTextField hostname = new JTextField("localhost"); // hostaname
			controls.add(hostname);
			JTextField portNum = new JTextField("50000"); // port
			controls.add(portNum);
			JSpinner plColor = new JSpinner(); // player color ? black : white
			plColor.setModel(new SpinnerListModel(new Color[] {Color.WHITE, Color.BLACK}));
			controls.add(plColor);

		
			p.add(controls, BorderLayout.CENTER);
			
			JOptionPane.showMessageDialog (frmDama, p, "Setup network game", JOptionPane.PLAIN_MESSAGE);
			
			try {
				this.port = Integer.parseInt(portNum.getText());
			} catch (NumberFormatException e1) { 
				error("Port has to be a number"); // creating new game should be terminated
			}
			this.color = (Color) plColor.getValue();
			this.IP = hostname.getText();
	
		}
		
		}; /*end Thread*/
		(new netThread(from_file)).start();
		
		return null;
	}
	
	/************************************ GAME INVITATION LISTENER ************************************/
	
	/** Method starts thread listening for game invitations.
	 * 
	 */
	public void startInvitationListener()
	{
		//listener thread
		Thread thread = new Thread()
		{
			public void run()
			{
			Socket new_sock;
			
			//bind server socket
			try {
				serv_sock = new ServerSocket(SERVER_PORT);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				error("Unable to start invitation listening thread, listening for game invitations is disabled." +
						"\nIs port number " + SERVER_PORT + " free ?");
				return;
			}
			
			//endlessly accept connections
			while(true)
			{
				try
				{
					new_sock  = serv_sock.accept();
					onConnect(new_sock);
				}
				catch(IOException e)
				{
					System.err.println("Error: Unable to accept on " + SERVER_PORT);
				}
			}
			} /* end run()*/
		};
		
		thread.start();
	}
	
	/** Method to react on connection of external client
	 *  
	 * @param sock Socket of accepted connection 
	 */
	public void onConnect(Socket sock)
	{
		class InvThread extends Thread
		{
			Socket socket;
			
			public InvThread(Socket s) {
				socket = s;
			}
			
			public void run()
			{
				
				ObjectOutputStream oos;
				ObjectInputStream ios = null;
				
				//create streams
				try {
					oos = new ObjectOutputStream(socket.getOutputStream());
					ios = new ObjectInputStream(socket.getInputStream());
				} catch (IOException e) {
					System.err.println("Error: Unable to create streams after accept ");
					return;
				}
				
				//read out invitation
				String request;
				try {
					request = (String) ios.readObject();
						
				} catch (IOException | ClassNotFoundException e1) {
					System.err.println("Error: Unable to read invitation");
					return;
				}
				
				GamePacket req = null;
				try {
					req = new GamePacket(request);
				} catch (DocumentException e) {
					System.err.println("Error: Malformed packet from other client");
					return;
				}
				
				//Could be only invitation
				if(req.getType() != GamePacket.PacketType.INVITE)
				{
					System.err.println("Error: Wrong packet type recieved, invitation expected");					
				}
				
				int n = JOptionPane.showOptionDialog(null,
						"You were invited to join network game with \n"
						+ socket.getRemoteSocketAddress() 
						+ " as " + req.getCol().toString() + " player.\nDo you accept?",
						"Game invitation recieved",
					    JOptionPane.YES_NO_OPTION,
					    JOptionPane.QUESTION_MESSAGE,
					    null,
					    null,
					    null);

				GamePacket accept;
				//0 -> YES
				//1 -> NO
				if(n == 0)
					accept = new GamePacket(GamePacket.PacketType.ACCEPT,req.getId(),null,null);
				else
					accept = new GamePacket(GamePacket.PacketType.END,null,null,null);
					
				try {
					oos.writeObject(accept.toString());
				} catch (IOException e) {
					// This should be seen by user
					error("Unable to send acceptance packet for game invitation");
				}
				
				//NO button pressed -> do not open game
				if(n == 1)
					return;
				
				//Create new game based on which color opponent choosed in invitation
				Game network_game = null;
			
				if(req.getCol() == Color.WHITE)
				{
					//we are BLACK
					try {
						network_game = new Game(req.getData(),new NetworkPlayer(socket,ios,oos, Color.WHITE), new LocalPlayer(Color.BLACK));
					} catch (IllegalTurnException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (CompletedGameLoadException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					//we are WHITE
					try {
						network_game = new Game(req.getData(),new LocalPlayer(Color.WHITE), new NetworkPlayer(socket,ios,oos, Color.BLACK));
					} catch (IllegalTurnException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (CompletedGameLoadException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
				}
							
				//Network game was established -> add it to list of games and tabbed  panel
				games.add(network_game);
				addTab(network_game,"Network game");
				network_game.start();
				
				
			} /* end run() */
			
		}; /*end Thread() */
		
		(new InvThread(sock)).start();
	}
	
}
