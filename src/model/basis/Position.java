package model.basis;

/** Class representing position in checkers board.
 * 
 * @author Stanislav Smatana
 *
 */
public class Position {
	
	// Enumeration of possible directions in checkers board
	public enum Direction{NORTH_WEST,NORTH_EAST,SOUTH_WEST,SOUTH_EAST};
	
	/**Row*/
	protected int r;
	/**Column*/
	protected char c;
	protected Figure fig;
	protected Desk d;
	
	/** Constructor to set up Position.
	 * 
	 * @param d Desk to which Position belongs.
	 * @param c Column of thos position.
	 * @param r Row of this position.
	 */
	public Position(Desk d,char c, int r)
	{
		this.d=d;
		this.c=c;
		this.r=r;
	}
	
	/** Method returns position from desk using relative coordinates. 
	 * 
	 * @param dC Relative coord. of column.
	 * @param dR Relative coord. of row.
	 * @return Position on given coords. or null if wrong coords. were given.
	 */
	public Position nextPosition(int dC, int dR)
	{
		return d.getPositionAt((char) (c + dC), r + dR);
	}
	
	/** Method returns new position in given direction.
	 * 
	 * @param dir Direction of new position.
	 * @return New position if exists, null otherwise.
	 */
	public Position nextPosition(Direction dir)
	{
		switch(dir)
		{
			case NORTH_WEST:
				return nextPosition(-1,1);
			case NORTH_EAST:
				return nextPosition(1,1);
			case SOUTH_WEST:
				return nextPosition(-1,-1);
			case SOUTH_EAST:
				return nextPosition(1,-1);
			default:
				return null;
		}
		
	}
	
	/** Method returns Direction towards Position pos.
	 * 
	 * @param pos Second position.
	 * @return Direction enum value.
	 */
	public Direction getDirectionTo(Position pos)
	{
			
		int dx = (int) pos.c - (int) c;
		int dy = pos.r - r;
		
		assert(dx != 0 && dy != 0);
		
		//x is less then zero -> NW/SW
		if(dx < 0)
		{
			if(dy > 0)
				return Direction.NORTH_WEST;
			else
				return Direction.SOUTH_WEST;
		}
		else
		{ // NE/SE
			
			if(dy > 0)
				return Direction.NORTH_EAST;
			else
				return Direction.SOUTH_EAST;

		}
		
		
	}
	
	/** Checks if given position is on the same row.
	 * 
	 * @param p Position.
	 * @return True or false.
	 */
	public boolean sameRow(Position p)
	{
		return p.r == r;
	}
	
	/** Checks if given position is on the same column.
	 * 
	 * @param p Position.
	 * @return True or false.
	 */
	public boolean sameColumn(Position p)
	{
		return p.c == c;
	}
	
	
	/** Checks if given position is on the same diagonal.
	 * 
	 * @param p Position.
	 * @return True or false.
	 */
	public boolean sameDiagonal(Position p)
	{
		//positions on diagonal have equal difference on both axis
		return Math.abs(p.r - this.r) == Math.abs((int) p.c - (int) this.c);
	}
	
	/** Method creates copy of this position*/
	public Position getCopy()
	{
		Position p = new Position(d,c,r);
		// Can't use putFigure, because fig can be null !!!
		if(fig != null)
		{
			p.putFigure(fig.getCopy());		
		}
		else
		{
			p.fig = null;
		}
		return p;
	}
	
	/** Method returns figure on this Position.
	 * 
	 * @return Figure object or null if there is no Figure on this position.
	 */
	public Figure getFigure()
	{
		return fig;
	}
	
	
	/** Method puts new figure on this position.  
	 * 
	 *  
	 * @param f Figure to be put in here.
	 * @return Old figure or null if there wasn't any.
	 */
	public Figure putFigure(Figure f)
	{
		assert(f != null);
				
		Figure tmp = fig;
		fig = f;
		f.setPosition(this);
		return tmp;
		
	}
	
	/** Method puts new figure on this position, respecting transformation from Man to King.  
	 * 
	 * @note If figure is Man and this is last row for color of this Figure,
	 * 		 new King is put up instead.
	 * 
	 * @param f Figure to be put in here.
	 * @return Old figure or null if there wasn't any.
	 */
	public Figure transPutFigure(Figure f)
	{
		assert(f != null);
		
		//if figure is regular Man and is on the last row -> should be transformed to King
		if(f instanceof model.figures.Man && d.onLastRow(this, f.getColor()))
			return putFigure(new model.figures.King(this, f.getColor()));
		else
			return putFigure(f);
	}
	
	/** Method removes Figure from this Position.
	 * 
	 * @return Old figure or null if there wasn't any.
	 */
	public Figure removeFigure()
	{
		Figure tmp = fig;
		fig = null;
		return tmp;
	}
	
	/** Method sets desk of this position*/
	public void setDesk(Desk d)
	{
		this.d = d;
	}
	
	public boolean equals(Object o)
	{
		if(o instanceof Position)
		{
			Position x = (Position) o;
			
			if(this.sameColumn(x) && this.sameRow(x))
				return true;
		}
		
		return false;
	}
	
	public int getRow(){ return r;}
	public char getColumn(){ return c;}
	
	public String toString()
	{
		return c + "" + r;
	}

	public Desk getD() {
		return d;
	}
}
