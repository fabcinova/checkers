package model.notation;

import java.util.Scanner;

import model.Model;
import model.exceptions.IllegalTurnException;
import model.history.History;
import model.history.Turn;

/** Decoding and encoding class for standart notation.
 * 
 * @author Stanislav Smatana
 *
 */
public class StandartNotation implements Notation {

	//states for parsing
	private enum states{TURN_NUM,TURN_ONE,TURN_TWO};
	
	@Override
	public String encode(Model mod) {
		String str = "";
		History hist = mod.getHist();
		
		int num = 1;
		int cnt = 0;
		for(Turn t: hist.getTurns())
		{
			if((cnt % 2) == 0)
			{
				str += num + ".";
				num++;
			}
			
			str += " " + t.toString();
			
			if((cnt % 2) == 1)
				str+="\n";
			
			cnt++;
			
		}
						
		return str;
	}

	@Override
	public Model decode(String str) throws IllegalTurnException {
		Scanner sc = new Scanner(str);
		
		String next;
		states parser_state = states.TURN_NUM;
		History hist = new History();
		
		while(sc.hasNext())
		{
			next = sc.next();
			
			switch(parser_state)
			{
			case TURN_NUM:
				parser_state = states.TURN_ONE;
				break;
			case TURN_ONE:
				parser_state = states.TURN_TWO;
				hist.addTurn(new Turn(next));
				break;
			case TURN_TWO:
				parser_state = states.TURN_NUM;
				hist.addTurn(new Turn(next));
				break;
			}
			
		}
		
		sc.close();
		return new Model(hist);
		
	}

}
