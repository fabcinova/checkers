package model.history;
import model.basis.Position;
import model.exceptions.IllegalTurnException;

/** Class representing one game turn.
 * 
 * @author bakman
 *
 */
public class Turn {
	
	// I'm not using Position because this should be independent - network !
	int row, row2;
	char column, column2;
	
		//character for capture
	public static final char CAPTURE_CHAR = 'x';
	
	//character for non-capture turn
	public static final char NON_CAPTURE_CHAR = '-';
	
	// Attribute determines if there was figure capture during
	// turn execution
	boolean was_captured = false;
	
	/** Method creates turn based on two positions.
	 * 
	 * @param p1 Position
	 * @param p2 Position
	 * @param capture Determines if there was figure capture during turn.
	 */
	public Turn(Position p1, Position p2, boolean capture)
	{
		row = p1.getRow();
		column = p1.getColumn();
		
		row2 = p2.getRow();
		column2 = p2.getColumn();
		was_captured = capture;
	}
	
	/** Method creates turn from string representation */
	public Turn(String desc) throws IllegalTurnException
	{

		//characters must be in allowed ranges
		if(!desc.matches("^[a-h][1-8][" + CAPTURE_CHAR + NON_CAPTURE_CHAR + "][a-h][1-8]$"))
			throw(new IllegalTurnException("Bad turn string size"));
		
		//cr-cr
		column = desc.charAt(0);
		//row is char number ! ord is needed
		row = desc.charAt(1) - '0';
		
		column2 = desc.charAt(3);
		row2 = desc.charAt(4) - '0';
		
	}
	
	@Override
	/** Creates turn string in standart notation*/
	public String toString()
	{
		return column + (row + "") + (was_captured ? CAPTURE_CHAR : NON_CAPTURE_CHAR ) + column2 + (row2 + "");
	}
	
	
	/** Method returns starting position of turn as Position object.
	 * 
	 * @warning Desk attribute of returned object is null !!
	 */
	public Position getStartPos()
	{
		return new Position(null,column,row);
	}
	
	/** Method returns end position of turn as Position object.
	 * 
	 * @warning Desk attribute of returned object is null !!
	 */
	public Position getEndPos()
	{
		return new Position(null,column2,row2);
	}

	public boolean isCaptured() {
		return was_captured;
	}

	public void setCaptured(boolean was_captured) {
		this.was_captured = was_captured;
	}
}
