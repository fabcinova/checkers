/** Replays game from saved file or XML description, provides automatic replay, or manual stepping from turn to turn
*
* @author Stanislav Smatana, Dominika Fabcinova
* @throws FileNotFoundException 
* @throws IllegalTurnException
* @throws DocumentException
* @throws IOException
*/

package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import org.dom4j.DocumentException;

import controller.interfaces.ControllerInterface;

import model.Model;
import model.basis.Figure.Color;
import model.exceptions.IllegalTurnException;
import model.history.Turn;
import view.View;
import view.gui.GUIView;
import view.interfaces.ControlsListener;

/** Replay controller class.
 * 
 * @author Stanislav Smatana
 *
 */
public class Replay implements ControlsListener, ControllerInterface, Runnable {

	
	Model model;
	View view;
	
	//Thread for automatically playing replay
	Thread playThread;
	
	//Wait time in replay
	//500 ms default
	static int replay_time = 500; 
	
	static public void setReplayTime(int new_time)
	{
		replay_time = new_time;
	}
	
	/** Construct replay from given file -> could be either finished or unfinished game
	 * 
	 * @param file
	 * @throws FileNotFoundException 
	 * @throws DocumentException 
	 * @throws IllegalTurnException 
	 */
	public Replay(File file) throws FileNotFoundException, IllegalTurnException, DocumentException
	{
		//read file in
		Scanner scan = new Scanner(new FileReader(file.getAbsolutePath()) );  
		scan.useDelimiter("\\Z");  
		String content = scan.next(); 	
				
		model = new Model(content);
		view = new GUIView(model);
		view.setControlsListener(this);
		
		//replay starts on first desk
		model.setFirstDesk();
		
		scan.close();
		view.update();
	}
	
	/** Construct Replay from given model.
	 * 
	 * @param m Model object.
	 */
	public Replay(Model m)
	{
		model = m;
		view = new GUIView(model);
		view.setControlsListener(this);
		
		//replay starts on first desk
		model.setFirstDesk();
		
		view.update();
	}
	
	@Override
	public void eventPrevDesk() {
		//if there is pending replay -> ignore
		if(playThread != null)
			return;
		
		model.previous();
		view.update();
	}

	@Override
	public void eventNextDesk() {
		//if there is pending replay -> ignore
			if(playThread != null)
				return;
				
		//if there is pending replay -> ignore
		model.next();
		view.update();
	}

	@Override
	public void eventPlay() {
		//if there is pending replay -> ignore
		if(playThread != null)
			return;
		
		playThread = new Thread(this);
		playThread.start();

	}

	@Override
	public void eventStop() {
		if (playThread != null)
		{
			playThread.stop();
			playThread = null;
		}
	}

	@Override
	public boolean sendTurn(Turn t) {return true;}

	@Override
	public List<Turn> getMoves(Color col) {	return null;}

	@Override
	public View getView() {	return view;}

	@Override
	public Model getModel() {return model;}

	@Override
	public void start() {}

	@Override
	public void save(File file) throws IOException {
		String xml_str = model.toXML();
		PrintWriter out = new PrintWriter(file);
		out.print(xml_str);
		out.close();
	}

	@Override
	/** Method for automatic replay -> runs as thread */
	public void run() {
		
		//iterate through all desks
		while(!model.isLast())
		{
			//move to next desk
			model.next();
			
			//refresh view
			view.update();
			
			try {
				Thread.sleep(replay_time);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		
		//indicate that replay ended
		playThread = null;
	}

	@Override
	//Nothing special to do on end
	public void end(boolean is_error, String Msg) {}

}
