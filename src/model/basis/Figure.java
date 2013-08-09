/**
 * TODO: If there is spare time -> think of something smarter than recalculating for whole desk
 */
package model.basis;

import java.util.List;
import java.util.ArrayList;

import model.basis.Position.Direction;

/** Abstract class representing figure.
 * 
 * @author bakman
 *
 */
public abstract class Figure {
	
	public static enum Color{WHITE,BLACK};
	
	
	protected Position p;
	protected Color col;
	
	//list of avalible moves
	protected List<Position> moves = new ArrayList<Position>();
	//list of avalible jumps
	protected List<Position> jumps = new ArrayList<Position>();
	
	/** Constructor for   figure of given color on given position. 
	 * 
	 * @param p Position of figure.
	 * @param col Color of figure.
	 */
	public Figure(Position p,Color col)
	{
		this.p = p;
		this.col = col;
		p.putFigure(this);
	}
	
	/** Method to obtain figure copy*/
	public abstract Figure getCopy();
	
	/** Function determines if this figure is on given position.
	 * 
	 * @param p Position to check.
	 * @return True if figure is on given position.
	 */
	public boolean isAtPosition(Position p)
	{
		return this.p.sameRow(p) && this.p.sameColumn(p);
	}

	/** Method moves figure to given position, if move is possible.
	 * 
	 * @param p Position to move to.
	 * @return True if move was valid, false otherwise.
	 */
	public boolean move(Position p)
	{
		if(canMove(p))
		{
			//Go in diagonal to target location and throw out any enemy figures
			//Rules are checked in canMove
			Direction dir = this.p.getDirectionTo(p);
			for(Position tmp = this.p; tmp != p; tmp = tmp.nextPosition(dir))
				if(tmp.getFigure() != null && tmp.getFigure().getColor() != col)
					tmp.removeFigure();
			
			this.p.removeFigure();
			this.p = p;
			this.p.transPutFigure(this);
			
			//we must recalculate possible moves for all figures
			this.p.getD().recalculateAllMoves();
			
			return true;
		}
		
		return false;
	}
	
	/** Method gets position of this figure.
	 * 
	 * @return Position.
	 */
	public Position getPosition()
	{
		return p;
	}
	
	/** Method sets position of this figure.
	 * 
	 */
	public void setPosition(Position p)
	{
		this.p = p;
	}
	
	/** Method gets color of this figure.
	 * 
	 * @return Color.
	 */
	public Color getColor()
	{
		return this.col;
	}
	
	/** Method determines if figure can move towards given position.
	 * 
	 * @param p Position to move to
	 * @return True if move is legal
	 */
	public boolean canMove(Position p)
	{
		//Jumping has higher priority
		if(!jumps.isEmpty())
		{
			if(jumps.contains(p))
				return true;
		}
		else
		{
			if(moves.contains(p))
				return true;
		}
		
		return false;
	}
	
	public abstract boolean equals(Object o);
	
	/** Method refreshes list of moves and jumps based
	 * 	on current position. 
	 */
	public abstract void recalculateMoves();
	
	/** Method returns all figures capturable figures around.
	 * 
	 * @return List of capturable Figure objects.
	 */
	public abstract List<Figure> getCapturableEnemies();

	public List<Position> getMoves() {
		return moves;
	}

	public List<Position> getJumps() {
		return jumps;
	}
	
}
