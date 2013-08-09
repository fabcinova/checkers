package model.figures;
import java.util.ArrayList;
import java.util.List;

import model.basis.Figure;
import model.basis.Position;
import model.basis.Position.Direction;


/** King figure.
 * 
 * @author Stanislav Smatana
 * 
 */
//TODO: PRASACKA getCapturableEnemies()
public class King extends Figure{

	public King(Position p, Color c)
	{
		super(p,c);
	}
	
	public List<Figure> getCapturableEnemies()
	{
		List<Figure> list = new ArrayList<Figure>();
		
		//in every direction
		for(Direction dir : Direction.values())
		{
			//find first enemy, null means we are past board
			Position e_pos;
			for(e_pos = p; e_pos != null; e_pos = e_pos.nextPosition(dir))
			{
				Figure f;
				if((f = e_pos.getFigure()) != null && f.getColor() != col)
				{
					//look ahead if next position is free
					Position next = e_pos.nextPosition(dir);
					
					if(next != null && next.getFigure() == null)
						list.add(f);
				}
			}
		}
		
		return list;
	}

	public void recalculateMoves()
	{
		moves.clear();
		jumps.clear();
		//King can move in every direction
		for(Direction dir : Direction.values())
		{
			//already met enemy figure ?
			boolean met_enemy = false;
			
			for(Position pos = p.nextPosition(dir); pos != null; pos = pos.nextPosition(dir))
			{
				Figure fig = pos.getFigure();
				
				//empty position
				if(fig == null)
				{
					//we already met enemy -> jumping
					if(met_enemy)
						jumps.add(pos);
					else
						moves.add(pos);
				}
				else if(fig.getColor() != col) 
				{ // we met enemy
					
					//can't jump past two enemies
					if(met_enemy)
						break;
					else
						met_enemy = true;
					
				}
				else
					break; // can't jump past friendly figure
			}
		}
		
	}
	
	
	public Figure getCopy()
	{
		return new King(p,col);
	}
	
	public boolean equals(Object o)
	{
		if(o instanceof King)
		{
			if( ((King) o).col == col)
				return true;
		}
		
		return false;
	}

}
