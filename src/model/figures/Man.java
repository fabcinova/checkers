package model.figures;

import java.util.List;
import java.util.ArrayList;

import model.basis.Figure;
import model.basis.Position;
import model.basis.Position.Direction;


/** Class for man figure.
 * 
 * @author Stanislav Smatana
 *
 */
public class Man extends Figure{

	//Possible directions of movement for given figure color
	static final Direction[][] possible_dirs = 
		{
			{Direction.NORTH_EAST,Direction.NORTH_WEST}, //White
			{Direction.SOUTH_EAST,Direction.SOUTH_WEST}, //Black
		};
	
	public Man(Position p, Color c)
	{
		super(p,c);
	}
	
	public List<Figure> getCapturableEnemies()
	{
		List<Figure> list = new ArrayList<Figure>();
		
		for(Direction dir : possible_dirs[col.ordinal()])
		{
			Position next,next_next;
			Figure f;
			
			next = p.nextPosition(dir);
			
			//enemy figure must be on next 
			if(next != null && (f = next.getFigure()) != null && f.getColor() != col)
			{
				next_next = next.nextPosition(dir);
				
				//cell next to enemy figure must be free
				if(next_next != null && next_next.getFigure() == null)
					list.add(f);
			}
			
		}
		
		return list;
	}
	
	
	public Figure getCopy()
	{
		return new Man(p,col);
	}
	
	public boolean equals(Object o)
	{
		if(o instanceof Man)
		{
			if( ((Man) o).col == col)
				return true;
		}
		
		return false;
	}
	
	//see Figure
	public void recalculateMoves()
	{
		//clear lists
		moves.clear();
		jumps.clear();
		
		// For all move directions possible for this color 
		for(Direction dir : possible_dirs[col.ordinal()])
		{
			//if next tile is empty -> we can move there
			Position next = p.nextPosition(dir);
			
			//It's not on board -> no logic in checking
			if(next == null)
				continue;
			
			Figure figure = next.getFigure();
			
			//we can move to empty postition
			if(figure == null)
			{
				moves.add(next);
			}
			else
			{
				//there is figure -> it must be of different color
				//and next position past it must be free
				if(figure.getColor() == col)
					continue;
				
				next = next.nextPosition(dir);
				
				if(next == null)
					continue;
				
				figure = next.getFigure();
				
				//empty
				if(figure ==  null)
					jumps.add(next);
				
			}
		}		
	}
	
}

