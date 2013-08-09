/** Represents packets, that are used during network game
*
* @author Stanislav Smatana, Dominika Fabcinova
* @throws DocumentException
*/

package controller.network;

import java.io.ByteArrayInputStream;

import model.basis.Figure.Color;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

/** Class representing game packet.
 * 
 * @author bakman
 *
 */
public class GamePacket {
	//Possible packet types
	public enum PacketType {INVITE,ACCEPT,TURN,END, MESSAGE};
	
	// String to insert into <packet> id attribute (session id)
	String id=null;
	// Data to be inserted inside <packet></packet> pair
	String data=null;
	// type of packet 
	PacketType type;
	Color col=null;
	
	/** Creates new game packet.
	 * 
	 * @param type Type of packet - one of PacketType enum
	 * @param id Session id - currently unused.
	 * @param data Textual data packet should carry.
	 * @param col Color specification for invitation packet.
	 */
	public GamePacket(PacketType type,String id, String data, Color col) {
		this.id = id;
		this.data = data;
		this.type = type;
		this.col = col;
	}
	
	/** Creates packet of given type.
	 * 
	 * @param type Type of packet PacketType enum.
	 */
	public GamePacket(PacketType type)
	{
		this(type,null,null,null);
	}
	
	/** Constructor builds packet from xml description 
	 * 
	 * @param xml_str String with XML description. 
	 * @throws DocumentException If xml document is invalid
	 */
	public GamePacket(String xml_str) throws DocumentException
	{
		SAXReader reader = new SAXReader();
        Document document = reader.read(new ByteArrayInputStream(xml_str.getBytes()));
        
        Element root = document.getRootElement();
        
        //root must be packet element
        if(root.getName() != "packet")
        	throw new DocumentException("Root element must be packet");
        
        String type_str = root.attributeValue("type");
        
        //Packet must have a type
        if(type_str == null)
        	throw new DocumentException("No type specified in packet");
        
        switch(type_str)
        {
        	case "INVITE":
        		type = PacketType.INVITE;
        		break;
        	case "ACCEPT":
        		type = PacketType.ACCEPT;
        		break;
        	case "TURN":
        		type = PacketType.TURN;
        		break;
        	case "END":
        		type = PacketType.END;
        		break;
        	default:
        		//unallowed type
        		throw new DocumentException("Wrong type of packet");
        }
        
        //invite and accept must have id
        if(type == PacketType.ACCEPT || type == PacketType.INVITE)
        {
        	String id_str = root.attributeValue("id");
        	
        	if(id_str == null)
        		throw new DocumentException("Accept or invite must have id");
        	id = id_str;
        }
        
        //invite must have color
        if(type == PacketType.INVITE)
        {
        	String color_str = root.attributeValue("color");
        	
        	if(color_str == null)
        		throw new DocumentException("Invite must have color");
        	
        	switch(color_str)
        	{
        	case "WHITE":
        		col = Color.WHITE;
        		break;
        	case "BLACK":
        		col = Color.BLACK;
        		break;
        	default:
        		//wrong color
        		throw new DocumentException("Wrong value of color");
        	}
        }
        
        data = "";
        
        //If element is textual -> directly add as data it's contents
        //otherwise is content converted from tree to xml notation 
        if(root.isTextOnly())
        	data = root.getText();
        else
        	for(Object el : root.elements())
        		data += ((Element) el).asXML();
        
     }
	
	public PacketType getType() {
		return type;
	}

	public void setType(PacketType type) {
		this.type = type;
	}

	/** Method returns packet in xml notation */
	public String toString()
	{
		String xml_str = "<packet type=\"" + type.toString() + "\"";
		
		if(id != null)
			xml_str += " id=\"" + id + "\"";
		
		if(col != null)
			xml_str += " color=\"" + col.toString() + "\"";
		
		xml_str += ">";
		
		if(data != null)
			xml_str += data;
		
		xml_str += "</packet>";
		
		return xml_str;
				
		
		
	}

	public Color getCol() {
		return col;
	}

	public void setCol(Color col) {
		this.col = col;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	

}
