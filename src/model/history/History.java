/*
 * TODO: History xml constructor
 */
package model.history;
import java.util.List;
import java.util.ArrayList;

/** Class representing history of turns.
 * 
 * @author Stanislav Smatana
 *
 */
public class History {
	List<Turn> turns = new ArrayList<Turn>();
	
	/** Empty constructor */
	public History(){}
	
	
	/** Adds turn to turn history.
	 * 
	 * @param t Turn object.
	 */
	public void addTurn(Turn t)
	{
		turns.add(t);
	}

	public List<Turn> getTurns() {
		return turns;
	}
	
	
	/** Constructor creates string representation of history */
	public String toString()
	{
		String str = "";
		
		for(int i = turns.size() - 1; i >= 0; i--)
			str += turns.get(i).toString() + "\n";
		
		return str;
	}
		
	
}
