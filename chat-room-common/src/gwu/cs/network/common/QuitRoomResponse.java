package gwu.cs.network.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class QuitRoomResponse extends ChatRoomCommandMessage {
	int messageType = 12;
	public int result = -1;

	public QuitRoomResponse(int result) {
		this.result = result;
	}
	
	@Override
	public byte[] serilize() {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(byteStream);
		try {
			out.writeByte(messageType);
			out.writeByte(2);
			out.writeByte(result);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] data = byteStream.toByteArray();
		return data;
	}

	public static QuitRoomResponse unSerilize(byte[] stream) {
		// TODO Auto-generated method stub
		ByteArrayInputStream bs = new ByteArrayInputStream(stream);
		DataInputStream in = new DataInputStream(bs);
		
		int result = -1;
		try {			
			int len = in.readByte();
			result = in.readByte();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new QuitRoomResponse(result);
	}
	
	@Override
	public int getMessageType() {
		return messageType;
	}

}
