package gwu.cs.network.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CreateRoomResponse extends ChatRoomCommandMessage {
	int messageType = 4;
	public int result = -1;
	public String roomID;
	
	public CreateRoomResponse(String roomID, int result) {
		this.roomID = roomID;
		this.result = result;
	}
	
	@Override
	public byte[] serilize() {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(byteStream);
		try {
			out.writeByte(messageType);
			int len = 2 + roomID.length() + 1 + 1;
			out.writeChar(len);
			out.writeUTF(roomID);
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
	
	public static CreateRoomResponse unSerilize(byte[] stream) {
		// TODO Auto-generated method stub
		ByteArrayInputStream bs = new ByteArrayInputStream(stream);
		DataInputStream in = new DataInputStream(bs);
		
		String roomID = "";
		int result = -1;
		try {		
			int len_roomID = in.readChar();
			byte[] byte_roomID = new byte[len_roomID];
			in.read(byte_roomID);
			
			int len = in.readByte();
			result = in.readByte();
			roomID = new String(byte_roomID);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new CreateRoomResponse(roomID, result);
	}
	

	@Override
	public int getMessageType() {
		return messageType;
	}

}
