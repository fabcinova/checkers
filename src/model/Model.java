package model;
import java.io.ByteArrayInputStream;
import java.util.List;

import java.util.Iterator;
import java.util.ArrayList;
import model.basis.Desk;
import model.basis.Figure;
import model.basis.Figure.Color;
import model.basis.Position;
import model.exceptions.IllegalTurnException;
import model.history.*;


import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

/** Class representing game data model.
 * 
 * @author Stanislav Smatana, Dominika Fabcinova
 *
 */
public class Model {

	//List of desks in current game 
	List<Desk> desks = new ArrayList<Desk>();
	
	//index pointing to actual board
	int actual_index = 0; 
	//History of turns
	History hist;
	
	/** Creates new game model with one desk and blank history.
	 * 
	 * Desk has figures on as in started game.
	 */
	public Model()
	{
		//default -> create blank history and default start desk
		hist = new History();
		desks.add(new Desk());
						
	}
	
	/** Constructs model from given xml string.
	 * 
	 * @param xml_str Valid xml string
	 */
	public Model(String xml_str) throws IllegalTurnException, DocumentException
	{
		this();
		SAXReader reader = new SAXReader();
        Document document = reader.read(new ByteArrayInputStream(xml_str.getBytes()));
        
        Element root = document.getRootElement();
        
        //Iterate through all <turn> elements
        for ( Iterator i = root.elementIterator( "turn" ); i.hasNext(); ) 
        {
            Element foo = (Element) i.next();
            
            //execute turn -> in case of illegal notation Turn constructor
            //also throws IllegalTurnException
            if(!makeTurn( new Turn(foo.getStringValue())) )
            	throw new IllegalTurnException("Illegal turn semantics");
            
        }
		
	}
	
	/** Constructor constructs model from existing history of turns.
	 * 
	 * @param hist History to create model from.
	 * @throws IllegalTurnException If history contains illegal turn.
	 */
	public Model(History hist) throws IllegalTurnException
	{
		
		this();
		for(Turn t : hist.getTurns())
		{
			if(!makeTurn(t))
            	throw new IllegalTurnException("Illegal turn semantics");
		}
		
		
		
	}
	
	
	
	/** Method returns xml description of this model.
	 * 
	 */
	public String toXML()
	{
		String xml = "<game>";
				
		//write each turn to
		for(Turn t : hist.getTurns())
		{
			xml += "<turn>" + t.toString() + "</turn>";
		}
		
		xml += "</game>";
		return xml;
	}
	
	/** Method makes turn based on corresponding Turn object.
	 * 
	 * @note Makes added desk active
	 * @param t Turn object.
	 * @return False on illegal move, true otherwise
	 */
	public boolean makeTurn(Turn t)
	{
		Position start,end;
		
		//Is this turn jump ? 
		//Some turns may not have correct jump flag
		if(isJump(t))
			t.setCaptured(true);
		else
			t.setCaptured(false);
		
		//These position object has no reference to actual desk !
		start = t.getStartPos();
		end = t.getEndPos();
		
		//always from last desk
		Desk new_desk = desks.get(desks.size() - 1).getCopy();
		
		Figure fig = new_desk.getFigureAt(start.getColumn(), start.getRow());
		Position dest = new_desk.getPositionAt(end.getColumn(), end.getRow());
		
		//there must be figure moving
		if(fig == null)
			return false;
		
		//it must be possible to move into dest
		if(!fig.canMove(dest))
			return false;
		
	
		//move figure on COPIED desk, add turn to history, add new desk to list
		//and actualize actual desk reference
		fig.move(dest);
		hist.addTurn(t);
		desks.add(new_desk);
		actual_index = desks.size() - 1;
		
		return true;		
		
	}
	
	/** Method determines if there are any jumps for given color on actual desk.
	 * 
	 * @param col Color to check for
	 * @return True if there are, false otherwise
	 */
	public boolean areJumps(Color col) {
		
		for(Position[] pos_arr : getActualDesk().getPositionsArray())
		{
			for(Position pos : pos_arr)
			{
				if(pos.getFigure() != null)
					if(pos.getFigure().getColor() == col)
						if(pos.getFigure().getJumps().size() != 0)
							return true;
			}
		}
		
		return false;
	}

	/** Method determines if given move is jump.
	 * 
	 * @param t Move to be checked
	 * @return True if it is jump, false otherwise.
	 */
	public boolean isJump(Turn t) 
	{
		Figure fig;
		if( ( fig = getActualDesk().getFigureAt(t.getStartPos().getColumn(), t.getStartPos().getRow())) != null)
		{
			//should work position has equals
			if(fig.getJumps().contains(t.getEndPos()))
				return true;
		}
		
		return false;
	}

	/** Method determines if there is figure of given color on position
	 *  @param col Column
	 *  @param row Row
	 */
	public boolean isColorOnPosition(char col, int row, Color color)
	{
		Position p;
		if((p = getActualDesk().getPositionAt(col, row)) != null)
		{
			if(p.getFigure() != null && p.getFigure().getColor() == color )
				return true;
		}
		
		return false;
	}
	
	
	/** Method activates previous desk */
	public void previous()
	{
		actual_index --;
		
		if(actual_index < 0)
			actual_index = 0;
	}
	
	/** Method activates next desk */
	public void next()
	{
		actual_index ++;
		
		if(actual_index >= desks.size())
			actual_index = desks.size() - 1;
	}
	
	/** Method returns actual Desk */
	public Desk getActualDesk()
	{
		return desks.get(actual_index);
	}

	/** Method gets possible moves for given color.
	 * @param col Color of figures for move inspection
	 */
	public List<Turn> getPossibleMoves(Color col)
	{
		List<Turn> moves = new ArrayList<Turn>();
		
		//first we inspect only jump lists -> they have priority
		for(Position[] pos_arr : getActualDesk().getPositionsArray())
		{
			for(Position pos : pos_arr)
			{
				Figure fig;
				
				if((fig = pos.getFigure())  != null && fig.getColor() == col)
				{
					for(Position p : fig.getJumps())
						moves.add( new Turn(pos,p,true) );
				}
			}
		}
		
		if(!moves.isEmpty())
			return moves;
		
		//In case of no jump moves -> we can consider regular moves	
		for(Position[] pos_arr : getActualDesk().getPositionsArray())
		{
			for(Position pos : pos_arr)
			{
				Figure fig;
				
				if((fig = pos.getFigure())  != null && fig.getColor() == col)
				{
					for(Position p : fig.getMoves())
						moves.add( new Turn(pos,p,false) );
				}
			}
		}
		
		return moves;		
				
	}

	public History getHist() {
		return hist;
	}
	
	/** Method determines if actual desk is last.
	 * 
	 * @return True if actual desk is last desk, false otherwise
	 */
	public boolean isLast()
	{
		return actual_index == desks.size() - 1;
	}
	
	public void setFirstDesk()
	{
		actual_index = 0;
	}
	
	/** Method returns winner of current game.
	 * 
	 * @return Color of winner or null in case of no winner
	 */
	public Color getWinner()
	{
		//Player with no moves is loser
		if(getPossibleMoves(Color.BLACK).size() == 0)
			return Color.WHITE;
		else if(getPossibleMoves(Color.WHITE).size() == 0)
			return Color.WHITE;
		else
			return null;
			
	}
}
