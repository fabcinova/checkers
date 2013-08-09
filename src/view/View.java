package view;

import model.Model;
import view.interfaces.*;
import model.basis.Figure.Color;

/** Abstract class representing game view.
 * 
 * @author Stanislav Smatana, Dominika Fabcinova
 *
 */
public abstract class View {
	
	//Game model
	protected Model model;
	// Listener to recieve gui turn notifications
	protected TurnListener turn_listener;
	// Listener to recieve gui controls (play, replay, etc...) notifications
	protected ControlsListener ctrl_listener;
	//Player on the turn
	protected  Color cur_player;
	
	/** Constructor sets model and event listeners.
	 * 
	 * @param m Model of game to use.
	 * @param tl Listener for turn events.
	 * @param cl Listener for other gui-releated events. See ControlsListener
	 * 			 interface for more info.
	 */
	public View(Model m, TurnListener tl, ControlsListener cl)
	{
		model = m;
		turn_listener = tl;
		ctrl_listener = cl;
	}
	
	public View(Model m)
	{
		this(m,null,null);
	}
	
	/** Method sets turn listener.
	 * 
	 * @param tl Object implementing TurnListener interface
	 */
	public void setTurnListener(TurnListener tl)
	{
		turn_listener = tl;
	}
	
	/** Method sets controls listener.
	 * 
	 * @param tl Object implementing ControlsListener interface
	 */
	public void setControlsListener
	(ControlsListener cl)
	{
		ctrl_listener = cl;
	}
	
	/** Method causes view to redraw.
	 * 
	 */
	public abstract void update();

	
	public void setActualPlayer(Color col)
	{
		cur_player = col;
	}
	
	/** Method should display error message
	 * 
	 * @param msg Error message contents.
	 */
	public abstract void errorMsg(String msg);
	
	/** Method should display info message
	 * 
	 * @param msg Error message contents.
	 */
	public abstract void infoMsg(String msg);
	
	
}
