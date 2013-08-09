package controller.player;

import java.util.List;
import java.util.Random;

import model.basis.Figure.Color;
import model.history.Turn;

/** Class for computer player
 * 
 */
public class ComputerPlayer extends Player implements view.interfaces.TurnListener {

	Random generator = new Random();
	
	public ComputerPlayer(Color c)
	{
		super(c);
	}
	
	//No gui events
	public void eventTurn(Turn t){}
	
	public void turnNotify(Turn t)
	{
		//get possible turn list
		List<Turn> moves = ctrl.getMoves(col);
		
		//if there are no moves -> do nothing
		if(moves.size() == 0)
			return;
				
		//make turn
		ctrl.sendTurn( moves.get(Math.abs(generator.nextInt() % moves.size())));
	}
}
