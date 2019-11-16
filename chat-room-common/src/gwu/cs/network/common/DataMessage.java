package gwu.cs.network.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DataMessage extends ChatRoomMessage {
	int messageType = 13;
	public String message = "";
	public String userID = "";
	public String roomID = "";
	
	public DataMessage(String userID, String roomID, String message) {
		this.message = message;
		this.roomID = roomID;
		this.userID = userID;
	}

	@Override
	public byte[] serilize() {
		// TODO Auto-generated method stub
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(byteStream);
		try {
			out.writeByte(messageType);
			int len = 2 + userID.length() + 2 + roomID.length() + 2 + message.length();
			out.writeChar(len);
			out.writeChar(userID.length());
			out.writeBytes(userID);
			out.writeChar(roomID.length());
			out.writeBytes(roomID);
			out.writeChar(message.length());
			out.writeBytes(message);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] data = byteStream.toByteArray();
		return data;
	}

	public static DataMessage unSerilize(byte[] stream) {
		ByteArrayInputStream bs = new ByteArrayInputStream(stream);
		DataInputStream in = new DataInputStream(bs);
		
		String userID = "";
		String roomID = "";
		String message = "";
		
		try {
			int len_userID = in.readChar();
			byte[] byte_userID = new byte[len_userID];
			in.read(byte_userID);
			
			int len_roomID = in.readChar();
			byte[] byte_roomID = new byte[len_roomID];
			in.read(byte_roomID);
			
			int len_message = in.readChar();
			byte[] byte_message = new byte[len_message];
			in.read(byte_message);
			
			userID = new String(byte_userID);
			roomID = new String(byte_roomID);
			message = new String(byte_message);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new DataMessage(userID, roomID, message);
	}

	@Override
	public int getMessageType() {
		return messageType;
	}
	
}
