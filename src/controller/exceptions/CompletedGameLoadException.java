/** Implements exception when user tries to load game, that has no more available moves
*
* @author Stanislav Smatana, Dominika Fabcinova
*/

package controller.exceptions;

/** Exception fired when user tries to load completed game and continue playing.
 * 
 * @author Stanislav Smatana
 *
 */
public class CompletedGameLoadException extends Exception {

	private static final long serialVersionUID = 1L;

	public CompletedGameLoadException(String msg) {
		super(msg);
	}
}
