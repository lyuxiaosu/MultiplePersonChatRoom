package gwu.cs.network.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import gwu.cs.network.common.CreateRoomResponse;
import gwu.cs.network.common.DataMessage;
import gwu.cs.network.common.GetUserList;
import gwu.cs.network.common.GetUserListResponse;
import gwu.cs.network.common.JoinRoomResponse;
import gwu.cs.network.common.SetUserNameResponse;
import gwu.cs.network.common.UserListChange;
import gwu.cs.network.common.GetUserListResponse;

public class ChatClient extends Thread {
	String username;
	Socket socket;
	DataOutputStream out;
	DataInputStream in;
	ClientWindow main_window;
	
	public ChatClient(ClientWindow main_window) {
		this.main_window = main_window;
	}

	public void finalize() {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void closeSocket() {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void run() {
		try {		
			socket = new Socket("127.0.0.1", 23456);  
			out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			        
			while(true) {			
				int messageType = in.readByte();			
				int length = in.readChar();
				System.out.println("client receive messageType is:" + messageType + " len is:" + length);
				byte[] message_payload = new byte[length];
				boolean end = false;
				StringBuilder dataString = new StringBuilder(length);
			    int totalBytesRead = 0;
			    while(!end) {
			    	int currentBytesRead = in.read(message_payload);
			        totalBytesRead = currentBytesRead + totalBytesRead;
			        if(totalBytesRead <= length) {
			            dataString
			              .append(new String(message_payload, 0, currentBytesRead, StandardCharsets.UTF_8));
			        } else {
			            dataString
			              .append(new String(message_payload, 0, length - totalBytesRead + currentBytesRead, 
			              StandardCharsets.UTF_8));
			        }
			        if(dataString.length()>=length) {
			            end = true;
			        }
			    }
			    handleMessage(messageType, message_payload);
			}
	
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
	}
	
	public void handleMessage(int messageType, byte[] message_payload) {
		System.out.println("client receive messageType:" + messageType);
		switch(messageType) {
		case 13:		
			handleDataMessage(message_payload);
			break;
		case 2:
			handleSetUserNameResponse(message_payload);
			break;
		case 4: 
			handleCreateRoomResponse(message_payload);
			break;
		case 6:
			handleJoinRoomResponse(message_payload);
			break;
		case 8:
			handleGetUserListResponse(message_payload);
		case 10:
			handleDestroyRoomResponse(message_payload);
			break;
		case 12:
			handleQuitRoomResponse(message_payload);
			break;
		case 14:
			handleUserListChanged(message_payload);
		default:
		}
	}
	
	
	private void handleDataMessage(byte[] message_payload) {
		DataMessage data = DataMessage.unSerilize(message_payload);
		String userID = data.userID;
		String roomID = data.roomID;
		String msg = data.message;
		
		String display = userID + ":" + msg + "\n";
		
		this.main_window.handleDataMessage(userID, roomID, msg);
	}
	
	private void handleSetUserNameResponse(byte[] message_payload) {
		SetUserNameResponse response = SetUserNameResponse.unSerilize(message_payload);
		System.out.println("handle setusername response, username:" + response.userID + " result is:" + response.result);

		if (response.result == 0) {
			this.username = response.userID;
		}		
		this.main_window.handleSetUserNameResponse(response.result);
	}
	
	private void handleCreateRoomResponse(byte[] message_payload) {
		CreateRoomResponse response = CreateRoomResponse.unSerilize(message_payload);
		System.out.println("handle createRoom response, roomid:" + response.roomID + " result is:" + response.result);
		
		this.main_window.handleCreateRoomResponse(response.result, response.roomID);
	}
	
	private void handleJoinRoomResponse(byte[] message_payload) {
		JoinRoomResponse response = JoinRoomResponse.unSerilize(message_payload);
		System.out.println("handle joinRoom response,roomid:" + response.roomID + " result is:" + response.result);
		
		this.main_window.handleJoinRoomResponse(response.result, response.roomID);
	}
	
	private void handleDestroyRoomResponse(byte[] message_payload) {
		
	}
	
	private void handleQuitRoomResponse(byte[] message_payload) {
		
	}
	
	private void handleGetUserListResponse(byte[] message_payload) {
		GetUserListResponse response = GetUserListResponse.unSerilize(message_payload);
		this.main_window.handleGetUserListResponse(response.user_list, response.roomID);
	}
	
	private void handleUserListChanged(byte[] message_payload) {
		UserListChange change = UserListChange.unSerilize(message_payload);
		GetUserList user_list = new GetUserList(change.roomID);
		send(user_list.serilize());
	}
	public void send(byte[] message) {
		try {
			out.write(message);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
