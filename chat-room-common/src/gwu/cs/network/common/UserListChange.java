package gwu.cs.network.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UserListChange extends ChatRoomCommandMessage {
	int messageType = 14;
	public String roomID = "";
	
	public UserListChange(String roomID) {
		this.roomID = roomID;
	}
	
	@Override
	public byte[] serilize() {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(byteStream);
		try {
			out.writeByte(messageType);
			
			int len = 2 + roomID.length();
			out.writeChar(len);
			out.writeChar(roomID.length());
			out.writeBytes(roomID);
			out.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] data = byteStream.toByteArray();
		return data;
	}

	public static UserListChange unSerilize(byte[] stream) {
		// TODO Auto-generated method stub
		ByteArrayInputStream bs = new ByteArrayInputStream(stream);
		DataInputStream in = new DataInputStream(bs);
		
		String roomID = "";
		try {			
			int len_roomID = in.readChar();
			byte[] byte_roomID = new byte[len_roomID];
			in.read(byte_roomID);
			
			roomID = new String(byte_roomID);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new UserListChange(roomID);
	}
	
	@Override
	public int getMessageType() {
		return messageType;
	}

}
