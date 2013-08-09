package controller.player;
import view.interfaces.TurnListener;
import model.basis.Figure.Color;
import model.history.Turn;
import controller.interfaces.ControllerInterface;

/** Abstract class representing players.
 * 
 * @author Stanislav Smatana
 *
 */
public abstract class Player implements TurnListener{
	
	Color col;
	ControllerInterface ctrl;
	
	/** Constructor creates player with given color on given controller.
	 * 
	 * @param ctrl Controler of game.
	 * @param c Color of this player.
	 */
	public Player(Color c)
	{
		col = c;
	}
	
	public void setController(ControllerInterface ctrl)
	{
		this.ctrl = ctrl;
	}
	
	public Color getCol() {
		return col;
	}
	
	/** Method to react to turnEvent from GUI. 
	 * 
	 * Defined as blank in abstract class because not all players need to talk with gui. 
	 */
	public void eventTurn(Turn t)
	{
		
	}
	
	/** Method notifies player that game has ended.
	 * 
	 *  In most cases should be empty. Only network player needs additional cleanup.
	 */
	public void end(){}

	/** Method notifies player, that it is his turn */
	public abstract void turnNotify(Turn t);
}
