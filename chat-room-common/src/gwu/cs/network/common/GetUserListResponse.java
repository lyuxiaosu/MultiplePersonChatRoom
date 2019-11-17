package gwu.cs.network.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GetUserListResponse extends ChatRoomCommandMessage {
	int messageType = 8;
	public String roomID;
	public Set<String> user_list;
	
	public GetUserListResponse(Set<String> user_list, String roomID) {
		this.user_list = user_list;
		this.roomID = roomID;
	}

	@Override
	public byte[] serilize() {
		String user_list_string = String.join(",", user_list);		
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(byteStream);
		try {
			out.writeByte(messageType);
			int len = 2 + roomID.length() + 2 + user_list_string.length();
			out.writeChar(len);
			out.writeUTF(roomID);
			out.writeUTF(user_list_string);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] data = byteStream.toByteArray();
		return data;
	}

	public static GetUserListResponse unSerilize(byte[] stream) {
		// TODO Auto-generated method stub
		ByteArrayInputStream bs = new ByteArrayInputStream(stream);
		DataInputStream in = new DataInputStream(bs);
		
		String roomID = "";
		Set<String> user_list = new HashSet();
		try {		
			roomID = in.readUTF();			
			String user_list_string = in.readUTF();
			if (user_list_string.length() > 0) {
				String[] names = user_list_string.split(",");
				Collections.addAll(user_list, names);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new GetUserListResponse(user_list, roomID);
	}
	
	@Override
	public int getMessageType() {
		return messageType;
	}

}
