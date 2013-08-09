package model.exceptions;

/** Exception to be thrown on illegal turn semantics.
 * 
 * @author Stanislav Smatana
 *
 */
public class IllegalTurnException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public IllegalTurnException(String msg)
	{
		super(msg);
	}
	
}
