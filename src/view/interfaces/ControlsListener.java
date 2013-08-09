package view.interfaces;

/** Interface for objects listening for replay control events from GUI.
 * 
 * @author Stanislav Smatana
 *
 */
public interface ControlsListener {
	public void eventPrevDesk();
	public void eventNextDesk();
	public void eventPlay();
	public void eventStop();
}
