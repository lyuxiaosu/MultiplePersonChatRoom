package gwu.cs.network.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Room {
	private String roomID;
	private Set<String> user_list = new HashSet();
	private String create_user;
	IUserListChanged subject;
	
	public Room(String roomID, String create_user, IUserListChanged subject) {
		this.roomID = roomID;
		this.create_user = create_user;
		this.subject = subject;
	}

	public void addUser(String user) {
		user_list.add(user);
		subject.userListChanged(roomID);
	}
	public void deleteUser(String user) {
		user_list.remove(user);
		subject.userListChanged(roomID);
	}
	
	public String getUserListString() {
		String joined = String.join(",", user_list);
		return joined;
	}
	
	public Set<String> getUserListSet() {
		return user_list;
	}
	public String getCreateUser() {
		return create_user;
	}
}
