package model.notation;

import model.Model;
import model.exceptions.IllegalTurnException;

/** Interface for custom game notation decoders-encoders.
 * 
 * @author Stanislav Smatana
 *
 */
public interface Notation {

	/** Method encodes model in notation.
	 * 
	 * @param mod Model to encode.
	 * @return Encoded String.
	 */
	String encode(Model mod);
	
	/** Class builds model from notation string.
	 * 
	 * @param str String in notation.
	 * @return Model object.
	 * @throws IllegalTurnException If notation String contains illegal turn.
	 */
	Model decode(String str) throws IllegalTurnException;
		
}
