package gwu.cs.network.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DestroyRoomResponse extends ChatRoomCommandMessage {
	int messageType = 10;
	public int result = -1;
	public String userID;
	public String roomID;
	
	public DestroyRoomResponse(String userID, String roomID, int result) {
		this.result = result;
		this.userID = userID;
		this.roomID = roomID;
	}
	
	@Override
	public byte[] serilize() {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(byteStream);
		try {
			out.writeByte(messageType);
			int len = 2 + userID.length() + 2 + roomID.length() + 1 + 1;
			out.writeChar(len);
			out.writeChar(userID.length());
			out.writeBytes(userID);
			out.writeChar(roomID.length());
			out.writeBytes(roomID);
			out.writeByte(1);
			out.writeByte(result);
			out.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] data = byteStream.toByteArray();
		return data;
	}
	
	public static DestroyRoomResponse unSerilize(byte[] stream) {
		// TODO Auto-generated method stub
		ByteArrayInputStream bs = new ByteArrayInputStream(stream);
		DataInputStream in = new DataInputStream(bs);
		
		int result = -1;
		String userID = "";
		String roomID = "";
		try {	
			
			int len_userID = in.readChar();
			byte[] byte_userID = new byte[len_userID];
			in.read(byte_userID);
			
			int len_roomID = in.readChar();
			byte[] byte_roomID = new byte[len_roomID];
			in.read(byte_roomID);
			
			userID = new String(byte_userID);
			roomID = new String(byte_roomID);
			
			int len = in.readByte();
			result = in.readByte();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new DestroyRoomResponse(userID, roomID, result);
	}
	

	@Override
	public int getMessageType() {
		return messageType;
	}

}
