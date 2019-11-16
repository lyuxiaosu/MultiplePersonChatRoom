package gwu.cs.network.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DestroyRoom extends ChatRoomCommandMessage {
	private int messageType = 9;
	public String roomID;
	public String userID;
	
	public DestroyRoom(String userID, String roomID) {
		this.userID = userID;
		this.roomID = roomID;
	}

	@Override
	public byte[] serilize() {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(byteStream);
		try {
			out.writeByte(messageType);
			int len = 2 + userID.length() + 2 + roomID.length();
			out.writeChar(len);
			out.writeChar(userID.length());
			out.writeBytes(userID);
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

	public static DestroyRoom unSerilize(byte[] stream) {
		// TODO Auto-generated method stub
		ByteArrayInputStream bs = new ByteArrayInputStream(stream);
		DataInputStream in = new DataInputStream(bs);
		
		String roomID = "";
		String userID = "";
		try {			
			int len_userID = in.readChar();
			byte[] byte_userID = new byte[len_userID];
			in.read(byte_userID);
			
			int len_roomID = in.readChar();
			byte[] byte_roomID = new byte[len_roomID];
			in.read(byte_roomID);
			
			userID = new String(byte_userID);
			roomID = new String(byte_roomID);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new DestroyRoom(userID, roomID);
	}
	
	@Override
	public int getMessageType() {
		return messageType;
	}

}
