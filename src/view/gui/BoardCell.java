/**
 * TODO: Vytvor element
 * TODO: Zmena stavu
 * TODO: Stav oznacenia
 * TODO: Obrazky
 * TODO: Implicitny JLabel
 * TODO: listener zo swingu na mouseClick
 * on event - zisitit position
 * konstruktor position, view, nastavit
 * oznacit kliknutu bunku
 */
package view.gui;

import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JLabel;
import model.basis.*;
import model.figures.King;

/** Class for graphical representation of board cell.
 * 
 * @author Stanislav Smatana
 *
 */
public class BoardCell extends JPanel implements MouseListener
{
	private static final long serialVersionUID = 1L;

	// states graphical cell can be in
	public enum CellState { WHITE_FREE, BLACK_FREE, WHITE_MAN, BLACK_MAN, WHITE_KING, BLACK_KING};
	
	// images representing figure states  -> no need to have one for each object
	protected static ImageIcon[] images =
	{
		// WHITE_FREE and BLACK_FREE have no images
		null,
		null,
		new ImageIcon("media/gradient_style/white_man.png"),
		new ImageIcon("media/gradient_style/black_man.png"),
		new ImageIcon("media/gradient_style/white_king.png"),
		new ImageIcon("media/gradient_style/black_king.png"),
	};
	
	final static Color focused_color = Color.GREEN; // background color of focused cell
	Color original_color; // original background color of a cell - BLACK / WHITE
	CellState state; // states according to enum CellState
	boolean focused; // isFocused ?
	GUIView view;
	Position pos;
	int row;
	int column;
	
	/** Constructor creates new cell
	 * 
	 * @param row Row.
	 */
	public BoardCell(int r, int c, Position pos, GUIView view, Color color)
	{
		this.original_color = color;
		this.pos = pos;
		this.view = view;
		this.row = r;
		this.column = c;
		
		focused = false;
		this.setBackground(original_color);
		addMouseListener(this);
		this.add(new JLabel());
		this.setState();
	}
	
	public void setPosition(Position p)
	{
		this.pos = p;
	}
	
//	/** Constructor creates new cell with state
//	 * 
//	 * @param c State of cell
//	 * @param focused Is focused ?
//	 */
//	public BoardCell(CellState c, boolean focused)
//	{
//		this.focused = focused;
//		state = c;
//		
//		//Build gui element
//		this.add(new JLabel());
//				
//		//set background color based on state
//		refreshColor();
//		
//		//set appropriate image based on state
//		refreshImage();
//						
//	}
	
	public Position getPosition()
	{
		return this.pos;
	}
	
	
//	/** Method refreshes cell background color based on it's state.*/
//	public void refreshColor()
//	{
//		if(focused)
//		{
//			this.setBackground(focused_color);
//			return;
//		}
//		
//		/* Set background color */
//		switch(state)
//		{
//		case WHITE_FREE:
//		case WHITE_MAN:
//		case WHITE_KING:
//			this.setBackground(Color.WHITE);
//			break;
//		default:
//			this.setBackground(Color.BLACK);
//			break;
//		}
//			
//	}
//	
	/** Method refreshes image on this cell.
	 * 
	 */
	public void refreshImage()
	{
		//Some states don't have images
		if(images[state.ordinal()] == null)
		{
			//this.setVisible(false);
			((JLabel) getComponent(0)).setIcon(null);
			return;
		}
		
		
		//set corresponding icon
		JLabel label = (JLabel) this.getComponent(0);
		label.removeAll();
		label.setIcon(images[state.ordinal()]);
		label.setVisible(true);
		this.setBackground(original_color);
	}
	

	/** Method sets new state of cell
	 * 
	 * @param c State to set into
	 * @warn Cell gets unfocused
	 */
	public void setState()
	{	
		if (this.pos.getFigure() != null)
		{
			Figure f = this.pos.getFigure();
			Figure.Color color = this.pos.getFigure().getColor();
			switch(color)
			{
				case BLACK:
					if(f instanceof King){
						this.state = CellState.BLACK_KING;
						this.refreshImage();
						break;}
					else{
						this.state = CellState.BLACK_MAN;
						this.refreshImage();
						break;}
				case WHITE:
					if(f instanceof King){
						this.state = CellState.WHITE_KING;
						this.refreshImage();
						break;}
					else{
						this.state = CellState.WHITE_MAN;
						this.refreshImage();
						break;}
				default:
					break;
			}
		}
		else
		{
			if (this.original_color.equals(Color.black))
			{
				this.state = CellState.BLACK_FREE;
				this.setBackground(original_color);
				this.refreshImage();
			}
			else if (this.original_color.equals(Color.white))
			{
				this.state = CellState.WHITE_FREE;
				this.setBackground(original_color);
				this.refreshImage();
			}
		}
	}
	
	public JPanel getPanel(){return this;}
	
	/** Method checks if cell is focused.
	 * 
	 * @return True/false
	 */
	public boolean isFocused(){return focused;}
	
	/** Unfocus this cell
	 * 
	 */
	public void unfocus()
	{
		this.focused = false;
		this.setBackground(original_color);
	}
	
	/** Focus this cell
	 * 
	 */
	public void focus()
	{
		this.focused = true;
		this.setBackground(focused_color);
	}
	
	/** Toggle focus of this cell.
	 * 
	 */
	public void toggleFocus()
	{
		focused = !focused;
		if (this.isFocused())
			this.setBackground(focused_color);
		else
			this.setBackground(original_color);
	}
	
	public CellState getState(){return state;}
	
	/** Method handles mouse click on panel */
	public void mouseClicked(MouseEvent me)
	{
		view.clicked(this);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	
}
