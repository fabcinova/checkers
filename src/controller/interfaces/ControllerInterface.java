/** Defines methods implemented by controller
*
* @author Stanislav Smatana, Dominika Fabcinova
*/

package controller.interfaces;
import java.io.File;
import java.io.IOException;
import java.util.List;

import view.View;

import model.Model;
import model.basis.Figure.Color;
import model.history.Turn;

/** Interface for game controller.
 * 
 * @author Stanislav Smatana
 *
 */
public interface ControllerInterface {
	
	/** Method to notify controller from player about new turn.
	 * 
	 * @param t Turn to be made
	 * @return True if turn was valid, false otherwise.
	 */
	public boolean sendTurn(Turn t);
	
	/** Method returns list of possible moves for given palyer.
	 * @param col Color for which to determine
	 */
	public List<Turn> getMoves(Color col);
	
	public View getView();
	public Model getModel();
	
	/** Method notifies controller, that it should start game**/
	public void start();
	
	/** Method saves game state.
	 * 
	 * @param file File to save state into.
	 * @throws IOException If there was problem during save.
	 */
	public void save(File file) throws IOException;
	
	/** Method to end game. 
	 * 
	 * @param is_error If true, game was ended due to error.
	 * @param Msg Desription of reason why game ended.
	 */
	public void end(boolean is_error,String Msg);
}
