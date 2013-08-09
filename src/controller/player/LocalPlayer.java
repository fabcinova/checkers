package controller.player;

import view.View;
import model.basis.Figure.Color;
import model.history.Turn;

/** Class for local player. Implements interface for receiving turn events from GUI
 * 
 */
public class LocalPlayer extends Player implements view.interfaces.TurnListener {

	public LocalPlayer(Color c)
	{
		super(c);
	}
	
	public LocalPlayer(Color c,View v)
	{
		super(c);
		v.setTurnListener(this);
	}
	

	public void eventTurn(Turn t)
	{
		//nothing special ... just send through to controller
		ctrl.sendTurn(t);
	}
	
	public void turnNotify(Turn t)
	{
		
	}
}
