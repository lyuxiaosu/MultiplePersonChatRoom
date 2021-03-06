package gwu.cs.network.server;

import java.util.Vector;

import gwu.cs.network.common.CreateRoomResponse;
import gwu.cs.network.common.DataMessage;
import gwu.cs.network.common.DestroyRoom;
import gwu.cs.network.common.DestroyRoomResponse;
import gwu.cs.network.common.GetUserListResponse;
import gwu.cs.network.common.JoinRoomResponse;
import gwu.cs.network.common.QuitRoomResponse;
import gwu.cs.network.common.SetUserNameResponse;
import gwu.cs.network.common.UserListChange;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ChatManager implements IUserListChanged, IUserDeleted {
	private ChatManager() {}
	private static final ChatManager CM=new ChatManager();
	public static ChatManager getChatManager(){
        return CM;
    }
	
	private HashMap<String, Room> rooms = new HashMap();  //key is roomID, value is Object Room
	private HashMap<String, User> users = new HashMap();
	
	
	public boolean setUserName(String username, ChatSocket c) {
		if (users.containsKey(username)) {
			SetUserNameResponse response = new SetUserNameResponse(username, 1);
			c.send(response.serilize());
			return false;
		} else {
			c.setUserName(username);
			User user = new User(username, c);
			users.put(username, user);
			SetUserNameResponse response = new SetUserNameResponse(username, 0);
			c.send(response.serilize());
			return true;
		}
	}
	
	public boolean createRoom(String username, String roomID) {
		if (rooms.containsKey(roomID)) {
			CreateRoomResponse response = new CreateRoomResponse(roomID, 1);
			users.get(username).getChatSocket().send(response.serilize());
			System.out.println("roomID:" + roomID + " has already been existed");
			return false;
		} else {
			//System.out.println("-----createRoom--------");
			Room room = new Room(roomID, username, this);
			rooms.put(roomID, room);
			//System.out.println("-----createRoom2--------");	
			room.addUser(username); //the one who creates the room will be joined this room automatically				
			//System.out.println("-----createRoom3--------");
			users.get(username).joinRoom(room); //user join room
			System.out.println("Create Room ID:" + roomID + " created by userID:" + username);
			CreateRoomResponse response = new CreateRoomResponse(roomID, 0);
			users.get(username).getChatSocket().send(response.serilize());
			//System.out.println("send create room response");
			return true;
		}
	}
		
	public boolean joinRoom(String username, String roomID) {
		if (rooms.containsKey(roomID)) {
			Room room = rooms.get(roomID);
			room.addUser(username);
			users.get(username).joinRoom(room);
			JoinRoomResponse response = new JoinRoomResponse(roomID, 0);
			users.get(username).getChatSocket().send(response.serilize());
			System.out.println("userID:" + username + " joined in room " + roomID);
			return true;
		} else {
			JoinRoomResponse response = new JoinRoomResponse(roomID, 1);
			users.get(username).getChatSocket().send(response.serilize());
			System.out.println("room " + roomID + " is not existing");
			return false;
		}
	}
	
	public boolean destroyRoom(String username, String roomID) {
		if (rooms.containsKey(roomID)) {
			Room room = rooms.get(roomID);
			if (room.getCreateUser().equals(username) && users.get(username).getUserName().equals(username)) {
				users.get(username).quitRoom(room); //remove room from user
				notifyRoomDestoried(username, roomID, room.getUserListSet());
				rooms.remove(roomID);//remove room from rooms
				System.out.println("user " + username + " destroied room " + roomID + " successfully");
				return true;
			} 		
		}
				
		DestroyRoomResponse response = new DestroyRoomResponse(username, roomID, 1);
		users.get(username).getChatSocket().send(response.serilize());
		System.out.println("user " + username + " destroied room " + roomID + " failed");
		return false;
	}
	
	public boolean quitRoom(String username, String roomID) {
		if (rooms.containsKey(roomID)) {
			Room room = rooms.get(roomID);
			if (users.get(username).getUserName().equals(username)) {
				room.deleteUser(username);
				users.get(username).quitRoom(room); //remove room in user's hashmap
				QuitRoomResponse response = new QuitRoomResponse(roomID, 0);
				users.get(username).getChatSocket().send(response.serilize());
				System.out.println("user " + username + " quit room " + roomID);
				return true;
			} 
		}
		
		QuitRoomResponse response = new QuitRoomResponse(roomID, 1);
		users.get(username).getChatSocket().send(response.serilize());
		return false;
	}
	
	public void getUserList(String roomID, ChatSocket cs) {
		if (rooms.containsKey(roomID)) {
			Set<String> user_list = rooms.get(roomID).getUserListSet();
			//System.out.println("roomID:" + roomID + " has " + user_list.size() + "users");
			GetUserListResponse response = new GetUserListResponse(user_list, roomID);
			cs.send(response.serilize());
		} else {
			GetUserListResponse response = new GetUserListResponse(null, roomID);
			cs.send(response.serilize());
		}
	}
	public void removeUser(String username) { //when client socket disconnect, call this function to remove user
		Set<Room> rooms = users.get(username).getRooms();
		Iterator<Room> it = rooms.iterator();
		while (it.hasNext()) {
			Room room = it.next(); 			
			if (room.getCreateUser().equals(username)) {
				this.destroyRoom(username, room.getRoomID());				
			} else {
				room.deleteUser(username);
			}
		}
		users.remove(username);
	}
	
	public void publish(String userID, String roomID, String msg){
		DataMessage dmg = new DataMessage(userID, roomID, msg);
		Room room = rooms.get(roomID);
		Set<String> user_list = room.getUserListSet();
		for (String user:user_list) {
			users.get(user).getChatSocket().send(dmg.serilize());
		}
    }
	
	private void notifyRoomDestoried(String username, String roomID, Set<String> user_list) {
		DestroyRoomResponse response = new DestroyRoomResponse(username, roomID, 0);		
		for (String user:user_list) {
			//if (user.equals(username) == false) {
				users.get(user).getChatSocket().send(response.serilize());
			//}
		}
	}

	@Override
	public void userListChanged(String roomID) {
		// TODO Auto-generated method stub
		UserListChange msg = new UserListChange(roomID);
		Set<String> user_list = rooms.get(roomID).getUserListSet();
		for (String user:user_list) {
			users.get(user).getChatSocket().send(msg.serilize());
		}
	}

	@Override
	public void userDeleted(String username) {
		this.removeUser(username);
	}

}
