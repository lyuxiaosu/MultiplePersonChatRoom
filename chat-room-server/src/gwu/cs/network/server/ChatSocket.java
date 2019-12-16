package gwu.cs.network.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.portable.InputStream;

import gwu.cs.network.common.CreateRoom;
import gwu.cs.network.common.DataMessage;
import gwu.cs.network.common.DestroyRoom;
import gwu.cs.network.common.GetUserList;
import gwu.cs.network.common.JoinRoom;
import gwu.cs.network.common.QuitRoom;
import gwu.cs.network.common.SetUserName;
import gwu.cs.network.common.UserListChange;

public class ChatSocket extends Thread {
	Socket socket;
	String username;
	IUserDeleted deleteuser;

	DataInputStream in;
	DataOutputStream out;
	boolean called_finalize = false;

	public ChatSocket(Socket s, IUserDeleted deleteuser) {
		this.socket = s;
		this.deleteuser = deleteuser;
	}


	public void finalize() {
		if (!called_finalize) {
			called_finalize = true;
			deleteuser.userDeleted(username);
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
	
	public void setUserName(String username) {
		this.username = username;
	}
	
	public String getUserName() {
		return this.username;
	}
	
	public void run() {
		try {
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			
			while(true) {	
				//System.out.println("waiting for reading stream");
				int messageType = in.readByte();
				int length = in.readChar();
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
			    //System.out.println("receive message type is :" + messageType + " length:" + length);
			    handleMessage(messageType, message_payload);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.toString());			
		} finally {
			System.out.println("close client socket anyway");
			this.finalize();
		
		}
	}

	public void handleMessage(int messageType, byte[] message_payload) {
		switch(messageType) {
		case 13:		
			handleDataMessage(message_payload);
			break;
		case 1:
			handleSetUserName(message_payload);
			break;
		case 3:
			handleCreateRoom(message_payload);
			break;
		case 9:
			handleDestroyRoom(message_payload);
			break;
		case 5:
			handleJoinRoom(message_payload);
			break;
		case 11:
			handleQuitRoom(message_payload);
			break;
		case 7:
			handleGetUserList(message_payload);
			break;
		case 14:
			handleUserListChanged(message_payload);
			break;
		default:
		}	
	}
	
	private void handleSetUserName(byte[] message_payload) {
		// TODO Auto-generated method stub
		SetUserName set_user_name = SetUserName.unSerilize(message_payload);
		System.out.println("set username:" + set_user_name.userName);
		ChatManager.getChatManager().setUserName(set_user_name.userName, this);		
	}

	private void handleDataMessage(byte[] message_payload) {
		DataMessage data = DataMessage.unSerilize(message_payload);
		String userID = data.userID;
		String roomID = data.roomID;
		String msg = data.message;	
		
		ChatManager.getChatManager().publish(userID, roomID, msg);
		//String display = userID + " from room:" + roomID + " saying:" + msg + "\n";
		//System.out.print(display);
	}
	private void handleCreateRoom(byte[] message_payload) {
		CreateRoom create_room = CreateRoom.unSerilize(message_payload);
		String userID = create_room.userID;
		String roomID = create_room.roomID;
		//System.out.println("user id:" + userID + " roomID:" + roomID);
		ChatManager.getChatManager().createRoom(userID, roomID);
	}
	
	private void handleDestroyRoom(byte[] message_payload) {
		DestroyRoom destroy_room = DestroyRoom.unSerilize(message_payload);
		String userID = destroy_room.userID;
		String roomID = destroy_room.roomID;
		ChatManager.getChatManager().destroyRoom(userID, roomID);
	}
	
	private void handleJoinRoom(byte[] message_payload) {
		JoinRoom join_room = JoinRoom.unSerilize(message_payload);
		String userID = join_room.userID;
		String roomID = join_room.roomID;
		//System.out.println("handle joinRoom user id:" + userID + " roomID:" + roomID);
		ChatManager.getChatManager().joinRoom(userID, roomID);
	}
	
	private void handleQuitRoom(byte[] message_payload) {
		QuitRoom quit_room = QuitRoom.unSerilize(message_payload);
		String userID = quit_room.userID;
		String roomID = quit_room.roomID;
		ChatManager.getChatManager().quitRoom(userID, roomID);
	}
	
	private void handleGetUserList(byte[] message_payload) {
		GetUserList user_list = GetUserList.unSerilize(message_payload);
		//System.out.println("handle get userlist, roomID:" + user_list.roomID);
		ChatManager.getChatManager().getUserList(user_list.roomID, this);
	}
	private void handleUserListChanged(byte[] message_payload) {
		UserListChange change = UserListChange.unSerilize(message_payload);
		String roomID = change.roomID;
		//System.out.println("handle userlist changed----------");
		ChatManager.getChatManager().userListChanged(roomID);
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
