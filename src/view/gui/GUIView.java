/**
 * TODO: Obsluha buttonov
 * TODO: Prosim uprav tie nechutne sracky v update
 * TODO: Moznost skrytia tlacidiel replayu/zasednutia
 * TODO: Tlacidla na commandy
 */

package view.gui;


import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;

import javax.swing.SwingConstants;
import javax.swing.JPanel;

import javax.swing.border.MatteBorder;

import view.View;
import view.gui.BoardCell;
import view.interfaces.ControlsListener;
import view.interfaces.TurnListener;
import model.basis.*;
import model.history.Turn;
import model.notation.Notation;
import model.notation.StandartNotation;
import model.Model;

/** GUI type of game view.
 * 
 * @author Stanislav Smatana, Dominika Fabcinova
 *
 */
public class GUIView extends View implements ActionListener {

	//This panel
	JPanel tab;
	//PLAY Replay button
	JButton btnPlay;
	
	//Cell focused by click
	public BoardCell focusedCell = null;

	//Graphic representation of game desk
	BoardCell[][] desk = new BoardCell[8][8];
	
	//game notation encoder for notation window
	Notation notation = new StandartNotation();

	/** Constructor builds gui and sets action listeners */
	public GUIView(Model m, TurnListener tl, ControlsListener cl)
	{
		super(m,tl,cl);
		initializeGUI();
	}
	
	/**Constructs view with given model*/
	public GUIView(Model m)
	{
		super(m);
		initializeGUI();
	}
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeGUI()
	{	
		tab = new JPanel();
		tab.setLayout(null);
				
		JButton button = new JButton("?");
		button.setBounds(38, 135, 117, 111);
		button.setActionCommand("help");
		tab.add(button);
		button.addActionListener(this);
		
		button.setFont(new Font("Dialog", Font.BOLD, 60));
		
		btnPlay = new JButton("PLAY");
		btnPlay.setBounds(38, 98, 117, 25);
		btnPlay.addActionListener(this);
		btnPlay.setActionCommand("PLAY");
		tab.add(btnPlay);
		
		JButton button_1 = new JButton("<");
		button_1.setBounds(38, 41, 53, 45);
		button_1.addActionListener(this);
		tab.add(button_1);
		
		JButton button_2 = new JButton(">");
		button_2.setBounds(103, 41, 52, 45);
		button_2.addActionListener(this);
		tab.add(button_2);
		
		JTextArea txtrPreviousMoves = new JTextArea();
		txtrPreviousMoves.setEditable(false);
		txtrPreviousMoves.setBounds(441, 38, 132, 208);
		
		JScrollPane sp = new JScrollPane(txtrPreviousMoves); 
		sp.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		sp.setBounds(441, 38, 132, 208);
		tab.add(sp);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(180, 15, 239, 15);
		tab.add(lblNewLabel);
		
		JLabel label = new JLabel("");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(192, 247, 219, 15);
		tab.add(label);
		
		JLabel lblPreviousmoves = new JLabel("PREVIOUS MOVES");
		lblPreviousmoves.setBounds(441, 15, 132, 15);
		tab.add(lblPreviousmoves);
		lblPreviousmoves.setHorizontalAlignment(SwingConstants.CENTER);
		
		JPanel panel = new JPanel(new GridLayout(8,8));
	    panel.setSize(200, 200);
	    panel.setBounds(192, 28, 219, 218);
	    
	    //Initialisation of graphic represenatation of desk based on model's actual desk
		Desk game_desk = model.getActualDesk();
	    
		System.err.print(model.getActualDesk());
	    for (int i = 0; i < 8; i++)
	    {
	        for (int j = 0; j < 8; j++)
	        {
	            if ((i + j) % 2 == 0)
	            {
	            	desk[i][j] = new BoardCell(i, j, game_desk.getPositionAt((char) ('a' + j), 8 - i), this, Color.white);
	            }
	            else
	            {
	            	desk[i][j] = new BoardCell(i, j, game_desk.getPositionAt((char) ('a' + j), 8 - i), this, Color.black);
	            }
	            panel.add(desk[i][j]);
	        }
	    }
	    
		tab.add(panel);
	}

	
	
	
	/** Method reacts to click on boardcell.
	 * 
	 * @param bc Boardcell that was clicked.
	 */
	public void clicked(BoardCell bc)
	{
		//if there is no turn listener -> there is no point in doing turns -> do nothing
		if(turn_listener == null)
			return;
		
		if(focusedCell == null)
		{
			//focusable means there is figure on it and it is of same color as current player
			if(isFocusable(bc))
			{
				unfocusAll();
				bc.toggleFocus();
				focusedCell = bc;
				
				//We also focus all awalible turns
				Figure fig = bc.getPosition().getFigure();
				
				//If there are jumps -> they have priority, otherwise we use moves
				//and also if there are jumps for player we must use only jumps
				List<Position> moves = null;
				if(model.areJumps(cur_player)) 
					moves = fig.getJumps();
				else
					moves = fig.getMoves();
								
				//focus all positions available for jump
				for(Position p : moves)
					getCellOnPosition(p).toggleFocus();
				
			}	
			//incorrect click -> nothing
		}
		else
		{
			//there is already focused position
			if(isFocusable(bc))
			{
				//if we clicked on another figure of this player -> go to previous state
				unfocusAll();
				focusedCell = null;
				clicked(bc);
			}
			else
			{
				//if it is click on different than focusable cell -> send turn
				unfocusAll();
				//it may happen that eventTurn does not return immediately
				BoardCell focused = focusedCell;
				focusedCell = null;
				turn_listener.eventTurn(new Turn(focused.getPosition(),bc.getPosition(),false));
				
				
			}
			
			//TODO: Unfocus all
		}
	}
	
	/** Get cell representing given position.
	 * @param pos Position object to check
	 * 
	 */
	protected BoardCell getCellOnPosition(Position pos)
	{
		int row = (8 - pos.getRow());
		int col = (pos.getColumn() - 'a');
		
		if(row > 7 || row < 0 || col > 7 || col < 0)
			return null;
		else
			return desk[row][col];
	}
	
	/** Method unfocuses all cells */
	protected void unfocusAll()
	{
		for(BoardCell[] bc_arr : desk)
			for(BoardCell bc: bc_arr)
				bc.unfocus();
	}
	
	
	@Override
	public void update()
	{
		Desk game_desk = model.getActualDesk();
		
	    for (int i = 0; i < 8; i++)
	    {
	        for (int j = 0; j < 8; j++)
	        {
	        	desk[i][j].setPosition(game_desk.getPositionsArray()[j][i]);
	        	desk[i][j].setState();
	        }
	    }
	    
	    
	    JLabel turnCol = (JLabel)this.tab.getComponent(5); // magic constant :(
	    
	    if(cur_player != null)
	    	turnCol.setText(cur_player.toString() + " PLAYER'S MOVE");
	    
	    JScrollPane history = (JScrollPane)this.tab.getComponent(4); // magic constant :(
	    JTextArea hist = (JTextArea)history.getViewport().getView();
	    hist.setText(notation.encode(model));
	    
	}

	
	public JPanel getPanel()
	{
		return tab;
	}

	@Override
	/**Method handles actions of gui buttons */
	public void actionPerformed(ActionEvent event) {

		//Only view commands
		if(event.getActionCommand().equals("help"))
		{
			focusAllPossibleMoves();
			return ;
		}
		
		//if no listener is set do nothing
		if(ctrl_listener == null)
			return;
		
		switch(event.getActionCommand())
		{
			case "<":
				ctrl_listener.eventPrevDesk();
				break;
			case ">":
				ctrl_listener.eventNextDesk();
				break;
			case "PLAY":
				if(btnPlay.getText().equals("PLAY"))
				{
					btnPlay.setText("STOP");
					ctrl_listener.eventPlay();
				}
				else
				{
					btnPlay.setText("PLAY");
					ctrl_listener.eventStop();
				}
				break;			
		}
	}
	
	/** Method focuses all possible moves for given player.
	 * 
	 */
	public void focusAllPossibleMoves()
	{
		List<Turn> moves= model.getPossibleMoves(cur_player);
		
		for(Turn t : moves)
		{
			getCellOnPosition(t.getEndPos()).focus();
		}
	}

	/** Method determines if given boardcel can be focused.
	 * 
	 * @param bc Board cell
	 * @return True or false
	 */
	public boolean isFocusable(BoardCell bc)
	{
		Position pos = bc.getPosition();
		
		if(pos.getFigure() != null)
		{
			if(pos.getFigure().getColor().equals(cur_player))
				return true;
		}
		
		return false;
	}
	
	@Override
	public void errorMsg(String msg) {
		// TODO Auto-generated method stub
		JLabel err = (JLabel)this.tab.getComponent(6); // magic constant :(
	    err.setText("<html><font color=\"red\">" + msg + "</font></html>");
	}

	@Override
	public void infoMsg(String msg) {
		// TODO Auto-generated method stub
		JLabel err = (JLabel)this.tab.getComponent(6); // magic constant :(
		err.setText("<html><font color=\"green\">" + msg + "</font></html>");		
	}
	
}
