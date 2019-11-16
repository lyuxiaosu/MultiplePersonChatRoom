package gwu.cs.network.server;

import java.util.HashSet;
import java.util.Set;

public class User {
	String username;
	Set<Room> rooms = new HashSet();
	ChatSocket chat_socket;
	
	public User(String username, ChatSocket chat_socket) {
		this.username = username;
		this.chat_socket = chat_socket;
	}
	
	public void joinRoom(Room room) {
		rooms.add(room);
	}
	
	public void quitRoom(Room room) {
		rooms.remove(room);
	}
	
	public ChatSocket getChatSocket() {
		return chat_socket;
	}
	
	public String getUserName() {
		return username;
	}
	
	public Set<Room> getRooms() {
		return rooms;
	}
}
