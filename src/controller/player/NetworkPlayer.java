/*
 * TODO: Ako signalizovat network chyby ? 
 */

package controller.player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.dom4j.DocumentException;

import controller.network.GamePacket;

import model.basis.Figure.Color;
import model.exceptions.IllegalTurnException;
import model.history.Turn;

public class NetworkPlayer extends Player {

	//Socket is here because it should be properly closed
	protected Socket sock;
	protected ObjectOutputStream oos;
	protected ObjectInputStream ios;
	
	//default socket timeout (ms) for end operation
	static final int DEFAULT_TIMEOUT = 500;
	
	//determines if end was called -> end is running in thread
	//so it may happen that turnNotify() is called while end is running
	//because of this end sets this flag before creating new thread
	//to signal for any turnNotify() that there is no point in contionuing
	boolean ended = false;

	/** Creates network player.
	 * 
	 * Socket is included for proper closure.
	 * 
	 * @param sock Socket with connection to remote host.
	 * @param ios InputObjectStream on this socket.
	 * @param oos InputObject stream on this socket.
	 * @param c Color of this player.
	 * 
	 */
	public NetworkPlayer(Socket sock,ObjectInputStream ios,ObjectOutputStream oos,Color c)  {
		super(c);
	
		
		//create Output and Input streams
		this.oos = oos;
		this.ios = ios;
		this.sock = sock;
		
	}

	@Override
	public void turnNotify(Turn t){
		
		//connection is in cleanup or ended
		if(ended)
			return;
		
		//This includes network operations and must be run as thread
		class turnThread extends Thread
		{
			Turn t;
			
			public turnThread(Turn t){this.t = t;}
			
			public void run()
			{
				// if it is not start notification -> send turn to enemy
				if(t != null)
				{
					
					GamePacket out_turn = new GamePacket(GamePacket.PacketType.TURN,null,t.toString(),null);
					System.err.println("Network: Sending turn " + out_turn.toString() );
					
					try {
						oos.writeObject(out_turn.toString());
					} catch (IOException e) {
						ctrl.end(true,"NETWORK ERROR: Unable to send" );
						return;
					}
					
				}
				
				String in_turn = null;		
				try {
					in_turn = (String) ios.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					return;
				} catch (IOException e) {
					ctrl.end(true,"NETWORK ERROR: Unable to read" );
					return;
				} 
				
					
				GamePacket in_turn_pack = null;
				try {
					in_turn_pack = new GamePacket(in_turn);
					System.err.println("Network: Recieved turn " + in_turn_pack.toString() );
				} catch (DocumentException e) {
					ctrl.end(true,"NETWORK ERROR: Unable to read" );
					return;
				}
				
				
				if(in_turn_pack.getType() == GamePacket.PacketType.END)
				{
					ctrl.end(false,"Opponent closed the game" );
					return;
				}
				
				
				//Can be only END or turn
				if(in_turn_pack.getType() != GamePacket.PacketType.TURN)
				{
					ctrl.end(true,"NETWORK ERROR: Wrong packet recieved" );
					return;
				}
				
				
				
				try {
					ctrl.sendTurn(new Turn(in_turn_pack.getData()));
				} catch (IllegalTurnException e) {
					ctrl.end(true,"NETWORK ERROR: Illegal turn recieved" );
					return;
				}
				}/* end run*/
			};/*end thread*/
			
			(new turnThread(t)).start();
	}

	@Override
	public void end()
	{
		ended = true;
		
		Thread t = new Thread()
		{
			public void run()
			{
				//We will try to send end -> but only for a while
				try {
					sock.setSoTimeout(DEFAULT_TIMEOUT);
					oos.writeObject((new GamePacket(GamePacket.PacketType.END, null, null, null)).toString());
				} catch (IOException e) {
					//If we were unable to send end -> don't care
				}
				
				try
				{
					//cleanup
					oos.close();
					ios.close();
					sock.close();
				}
				catch(IOException e)
				{
					//Well unable to close. So ? 
				}
				
			}/*end run()*/
		};/* end Thread */
		
		t.start();
	}
}
