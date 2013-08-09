package controller.tests;

import static org.junit.Assert.*;

import model.basis.Figure.Color;

import org.dom4j.DocumentException;
import org.junit.Test;

import controller.network.GamePacket;

public class GamePacketTests {

	GamePacket[] testPackets=
		{
			new GamePacket(GamePacket.PacketType.END),
			new GamePacket(GamePacket.PacketType.TURN),
			new GamePacket(GamePacket.PacketType.ACCEPT,"A2",null,null),
			new GamePacket(GamePacket.PacketType.INVITE,"A2","<game><turn>c2-a2</turn></game>",Color.WHITE),
			new GamePacket(GamePacket.PacketType.TURN,null,"c2-a2",null),			
		};
	
	String[] testPacketStrings =
		{

			"<packet type=\"END\"></packet>",
			"<packet type=\"TURN\"></packet>",
			"<packet type=\"ACCEPT\" id=\"A2\"></packet>",
			"<packet type=\"INVITE\" id=\"A2\" color=\"WHITE\"><game><turn>c2-a2</turn></game></packet>",
			"<packet type=\"TURN\">c2-a2</packet>",
		};
	
	@Test
	public void testToString() {
		
		for(int i=0; i < testPackets.length;i++)
		{
			assertEquals("Packet zadany konstruktorom sa nezhoduje s XML notaciou",testPacketStrings[i], testPackets[i].toString());
		}
	}
	
	@Test
	public void testFromString() {
		
		for(int i=0; i < testPackets.length;i++)
		{
			try
			{
				//string -> packet -> string should be same as string
				assertEquals("Packet zadany konstruktorom sa nezhoduje s XML notaciou",testPacketStrings[i], (new GamePacket(testPacketStrings[i])).toString());
			}
			catch(DocumentException e)
			{
				assertFalse("DocumentException thrown in " + testPacketStrings[i] +": " + e.getMessage(),true);
			}
		}
		
	}

}
