package view.interfaces;
import model.history.Turn;

/** Interface for objects listening for Turns from gui.
 * 
 * @author Stanislav Smatana
 *
 */
public interface TurnListener {
	
	
	public void eventTurn(Turn t);
	

}
