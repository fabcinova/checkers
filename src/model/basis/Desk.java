/**
 */
package model.basis;
import model.basis.Figure.Color;
import model.figures.King;
import model.figures.Man;

/** Class for representation of game board.
 * 
 * @author Stanislav Smatana
 *
 */
public class Desk {
	
	protected Position[][] pole;
	int dim;
	
	//default Desk size -> 8x8
	private final static int DEFAULT_DIM = 8;
	
	//default starting configuration for desk
	private final static String DEF_DESK_STR = (
			   "XCXCXCXC" + // 8
	   		   "CXCXCXCX" + // 7
	   		   "XCXCXCXC" + // 6
	   		   "XXXXXXXX" + // 5
	   		   "XXXXXXXX" + // 4
	   		   "BXBXBXBX" + // 3
	   		   "XBXBXBXB" + // 2
	   		   "BXBXBXBX"); // 1
	
	/** Constructor creates default sized (8x8) desk with Figures placed on starting positions.*/
	public Desk()
	{
		this(DEF_DESK_STR);
	}
	
	/** Constructor creates and initializes Desk with given dimension. 
	 * 
	 * Size of desk is dim x dim.
	 * @note This constructor creates Desk with no figures.
	 * @param dim Dimension of Desk.
	 */
	public Desk(int dim)
	{
		this.dim = dim;
		pole = new Position[dim][dim];
		
		//Chessboard goes a -> h (left to right)
		//and 8 -> 1 (top to bottom)
		for(int j=0;j < dim; j++)
			for(int i=0; i < dim;i++)
				pole[i][j] = new Position(this,(char) ('a' + i) , dim - j);
				
				
		
	}
	
	/** Special constructor for debugging purposes.
	 * 
	 * Constructs Desk with figures from given string representation.
	 * Only 8x8 Desk is supported. Each cell is represented by one char.
	 * 
	 * Allowed chars and their meaning:
	 * X - empty cell
	 * C - cell with black man
	 * B - cell with white man
	 * V - cell with black king
	 * K - cell with white king
	 * 
	 * @param str String representation of 8x8 desk. 
	 */
	public Desk(String str)
	{
		assert(str.length() == 64);
		this.dim = DEFAULT_DIM;
		pole = new Position[dim][dim];
		
		int i = 0;
		int j = 0;
		
		for(char ch : str.toCharArray())
		{
			Position pos  = new Position(this,(char) ('a' + i), dim - j );
			
			switch(ch)
			{
			case 'C':
				pos.putFigure(new Man(pos,Figure.Color.BLACK));
				break;
			case 'B':
				pos.putFigure(new Man(pos,Figure.Color.WHITE));
				break;
			case 'V':
				pos.putFigure(new King(pos,Figure.Color.BLACK));
				break;
			case 'K':
				pos.putFigure(new King(pos,Figure.Color.WHITE));
				break;
			case 'X':
				break;
			default:
				throw new RuntimeException("Na sachovnici mozu byt len znaky C, B, V, K ! Zadany znak: " + ch);
				
			}
				
			pole[i][j] = pos;
			
			i++;
			if(i == 8)
			{
				i = 0;
				j = (j + 1) % 8;
			}
				
				
		}
		
		recalculateAllMoves();
		
	}
	
	/** Gets position at given coordinates.
	 * 
	 * @param c Column 
	 * @param r Row
	 * @return Position object if coords. were valid, null otherwise.
	 */
	public Position getPositionAt(char c, int r)
	{
		if(c - 'a' < dim && r <= dim && c >= 'a' && r > 0)
		{
			return pole[c-'a'][ dim - r];
		}
		else
		{
			return null;
		}
	}
	
	/** Gets figure at given position.
	 * 
	 * @param c Column
	 * @param r Row
	 * @return Figure object if coords. were valid and t
	 * 		   here was figure on given position, null otherwise.
	 */
	public Figure getFigureAt(char c, int r)
	{
		Position p;

		if((p = getPositionAt(c,r)) != null)
			return p.getFigure();
		else
			return null;
	}
	
	/** Method determines if given position is in last row for given color.
	 * 
	 * @param pos Position to be checked.
	 * @param col Color of figures to determine.
	 */
	public boolean onLastRow(Position pos, Color col)
	{
		//based on color 
		if(col == Color.WHITE)
		{
			//WHITE -> transform position is on row dim
			if(pos.sameRow(new Position(this,'a',dim)))
					return true;
		}
		else if(col == Color.BLACK)
		{
			//BLACK -> transform position is on row 1 (8 in this game)
			if(pos.sameRow(new Position(this,'a',1)))
				return true;
		}
		else
		{
			//SHOULD NOT HAPPEN
			assert(false);
		}
		
		return false;
	}
	
	/** Method returns array of Positions of this desk
	 * 
	 */
	public Position[][] toArray()
	{
		return pole;
	}
	
	/** Method creates copy of this desk.
	 * @return Copy of this desk.
	 * @note Desk object and all it's position objects are copied 
	 */
	public Desk getCopy()
	{
		Desk copy = new Desk(dim);
		
		for(int i=0; i < dim; i++)
		{
			for(int j=0; j < dim; j++)
			{
				copy.pole[i][j] = pole[i][j].getCopy();
				//also position has reference to desk -> MUST BE UPDATED
				copy.pole[i][j].setDesk(copy);
			}
		}
		copy.recalculateAllMoves();
		return copy;
	}
	
	// Debugging -> dumps desk
	// see Desk(String) for notation description
	public String toString()
	{
		String str = new String();
		
		//for better look with JUnit
		str += '\n';
		
		for(int i=0; i < dim;i++)
			for(int j=0; j < dim; j++)
			{
				Figure f;
				
				if( (f = pole[j][i].getFigure()) == null)
				{
					str += 'X';
				}
				else
				{
					if (f instanceof model.figures.Man)
					{
						if(f.getColor() == Figure.Color.BLACK)
							str += 'C';
						else
							str += 'B';
					}
					else
					{
						if(f.getColor() == Figure.Color.BLACK)
							str += 'V';
						else
							str += 'K';
						
					}
				}
				
				if(j == 7)
					str += '\n';
				
			}
		
		return str;
	}
	
	public boolean equals(Object o)
	{ 
		if(o instanceof Desk)
		{
			Desk d2 = (Desk) o;
			
			if(dim != d2.dim)
				return false;
						
			for(int i=0; i < dim; i++)
			{
				for(int j=0; j < dim; j++)
				{
					Position p1 = pole[i][j];
					Position p2 = d2.pole[i][j];
					
					//all positions must be equal		
					if(!p1.equals(p2))
						return false;
					
					Figure f1 = p1.getFigure();
					Figure f2 = p2.getFigure();
					
					// If both positions are without figure -> they are equal
					if(f1 == null && f2 == null)
						continue;
					else if (f1 == null || f2 == null)
						return false; // only one figure -> unqeual
								
					//both figures -> must be equal
					if(!f1.equals(f2))
						return false;
					
				}
			}
			
			//everything equal
			return true;
		}
		return false;
	}
	
	public Position[][] getPositionsArray()
	{
		return pole;
	}
	
	/** Method notifies all figures on this desk to recalculate their moves.
	 * 
	 */
	public void recalculateAllMoves()
	{
		//for all positions - if there is figure recalculate it's moves
		for(Position[] pos_arr: pole)
			for(Position pos : pos_arr)
				if(pos.getFigure() != null)
					pos.getFigure().recalculateMoves();
				
	}

}
