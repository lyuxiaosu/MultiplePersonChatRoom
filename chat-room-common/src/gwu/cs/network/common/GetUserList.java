package gwu.cs.network.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class GetUserList extends ChatRoomCommandMessage {
	int messageType = 7;
	public String roomID;
	
	public GetUserList(String roomID) {
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
	public static GetUserList unSerilize(byte[] stream) {
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
		return new GetUserList(roomID);
	}
	@Override
	public int getMessageType() {
		return messageType;
	}

}
