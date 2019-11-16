package gwu.cs.network.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SetUserNameResponse extends ChatRoomCommandMessage {
	int messageType = 2;
	public String userID;
	public int result = -1;

	public SetUserNameResponse(String userID, int result) {
		this.result = result;
		this.userID = userID;
	}
	
	@Override
	public byte[] serilize() {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(byteStream);
		try {
			out.writeByte(messageType);
			int len = 2 + userID.length() + 1 + 1;
			out.writeChar(len);
			out.writeUTF(userID);
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
	public static SetUserNameResponse unSerilize(byte[] stream) {
		// TODO Auto-generated method stub
		ByteArrayInputStream bs = new ByteArrayInputStream(stream);
		DataInputStream in = new DataInputStream(bs);
		
		String userID = "";
		int result = -1;
		try {	
			int len_userID = in.readChar();
			byte[] byte_userID = new byte[len_userID];
			in.read(byte_userID);
			
			int len = in.readByte();
			result = in.readByte();
			userID = new String(byte_userID);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new SetUserNameResponse(userID, result);
	}
	
	@Override
	public int getMessageType() {
		return messageType;
	}

}
