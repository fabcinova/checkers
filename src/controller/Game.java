/** Creates game of 2 players from saved file or XML description
*
* @author Stanislav Smatana, Dominika Fabcinova
* @throws CompletedGameLoadException
* @throws IllegalTurnException
* @throws DocumentException
* @throws IOException
*/

package controller;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.dom4j.DocumentException;

import model.history.Turn;
import model.basis.Figure.Color;
import model.exceptions.*;
import controller.exceptions.CompletedGameLoadException;
import controller.player.Player;
import model.Model;
import view.View;
import view.gui.GUIView;
import view.interfaces.TurnListener;

import java.util.List;
import java.util.Scanner;

/** Basic controller class for games.
 * 
 * @author Stanislav Smatana, Dominika Fabcinova
 *
 */
public class Game implements controller.interfaces.ControllerInterface {
	
	//first player in array is always white
	Player[] players = new Player[2];
	
	// Model for this controller
	Model model;
	//View for this controller
	View view;
	int current_player = 0;
	
	//Who won the game ? 
	Color winner = null;
	
	//is Game finished?
	boolean finished = false;
	
	/** Constructs game from given model and view.
	 * 
	 * @param m Model object.
	 * @param v View derived object.
	 */
	public Game(Model m, View v)
	{
		model = m;
		view = v;
		
		//v.setTurnListener(this);
		//v.setControlsListener(this);
		
		//update view
		view.setActualPlayer(Color.WHITE);
		view.update();
		
	}
	
	/** Constructor creates game with given players. 
	 * 
	 * @param m Model to use.
	 * @param v View to use.
	 * @param white Player derived object for white player.
	 * @param black Player derived object for black player.
	 */
	public Game(Model m, View v, Player white, Player black)
	{
		this(m,v);
		players[0] = white;
		white.setController(this);
		v.setTurnListener(white);
		
		players[1] = black;
		black.setController(this);
		view.update();
		view.setActualPlayer(Color.WHITE);
	}
	
	/** Constructor creates game with given players.
	 * 
	 * Model and view are created implicitly. GUIView is used as view.
	 * @param white Player derived object for white player.
	 * @param black Player derived object for black player.
	 */
	public Game(Player white, Player black)
	{
		model = new Model();
		view = new GUIView(model);
		
		players[0] = white;
		white.setController(this);
		view.setTurnListener(white);
		
		players[1] = black;
		black.setController(this);
		view.setActualPlayer(Color.WHITE);	
		view.update();
	}
	


	

	/** Method creates game from xml description.
	 * 
	 * @param xml_str Xml string in format <game><turn></turn>...</game>
	 * @param white White player.
	 * @param black Black player.
	 * @throws IllegalTurnException If string contains illegal turn.
	 * @throws DocumentException If string is not in valid format.
	 * @throws CompletedGameLoadException If game in string is finished game. It can't be played.
	 */
	public Game(String xml_str, Player white, Player black) throws IllegalTurnException, DocumentException, CompletedGameLoadException
	{
	model = new Model(xml_str);
	
	//already won game is not playable
	if(model.getWinner() != null)
		throw new CompletedGameLoadException("");
	
	view = new GUIView(model);
	
	//set players
	players[0] = white;
	white.setController(this);
	view.setTurnListener(white);
	
	players[1] = black;
	black.setController(this);
	
	//view.setControlsListener(this);
	view.setActualPlayer(Color.WHITE);
	view.update();		
	
	}
	
	/** Method creates game from xml description.
	 * 
	 * @param file Xml file path.
	 * @param white White player.
	 * @param black Black player.
	 * @throws IllegalTurnException If file contains illegal turn.
	 * @throws DocumentException If file is not in valid format.
	 * @throws CompletedGameLoadException If game saved in file is finished game. It can't be played.
	 */
	public Game(File file, Player white, Player black) throws IOException, IllegalTurnException, DocumentException, CompletedGameLoadException
	{
		//read file in
		Scanner scan = new Scanner(new FileReader(file.getAbsolutePath()) );  
		scan.useDelimiter("\\Z");  
		String content = scan.next(); 	
		
		model = new Model(content);
		view = new GUIView(model);
	
		//already won game is not playable
		if(model.getWinner() != null) {
			scan.close();
			throw new CompletedGameLoadException("");
		}
		
		//set players
		players[0] = white;
		white.setController(this);
		view.setTurnListener(white);
		
		players[1] = black;
		black.setController(this);
		
		//view.setControlsListener(this);
		view.setActualPlayer(Color.WHITE);
		view.update();		
		scan.close();
		
	}
	
	
	@Override
	public boolean sendTurn(Turn t) {
		
		//If there is winner -> no point in sending turns
		if(finished)
			return false;
		
		if(!model.isColorOnPosition(t.getStartPos().getColumn(), t.getStartPos().getRow(), players[current_player].getCol()))
			return false;
		
		//If there are jumps for this color and turn is not jump -> can't do, must jump
		if(model.areJumps(players[current_player].getCol()) && !model.isJump(t))
			return false;
		
		if(model.makeTurn(t))
		{
			//TODO: Check for victory
			current_player = (current_player + 1) % 2;	
			
			
			//swap current gui listener and send new player color to gui
			view.setTurnListener((TurnListener) players[current_player]);
			view.setActualPlayer(players[current_player].getCol());
			players[current_player].turnNotify(t);
			view.update();
			
			//Is there winner after turn ?
			winner = model.getWinner();
			if(winner != null)
			{
				finished = true;
				end(false,winner.toString() + " won the game!");
			}
			
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
	/** Starts new game = notifies first player */
	public void start()
	{
		players[0].turnNotify(null);
	}
	
	/** Method saves game to given file.
	 * @param file Filename of output file.
	 */
	public void save(File file) throws IOException
	{
		String xml_str = model.toXML();
		PrintWriter out = new PrintWriter(file);
		out.print(xml_str);
		out.close();
	}

	public List<Turn> getMoves(Color col) {
		return model.getPossibleMoves(col);
	}

	@Override
	public void end(boolean is_error, String Msg) {
		finished = true;
		
		if(is_error)
			view.errorMsg(Msg);
		else 
			view.infoMsg(Msg);
		
		
		//disable turn listening on view
		view.setTurnListener(null);
		
		//Notify players that game has ended
		players[0].end();
		players[1].end();
		
	}
	
	public Model getModel() {
		return model;
	}
	public View getView() {
		return view;
	}
	
	
}
