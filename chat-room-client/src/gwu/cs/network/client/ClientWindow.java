package gwu.cs.network.client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import gwu.cs.network.common.CreateRoom;
import gwu.cs.network.common.DataMessage;
import gwu.cs.network.common.DestroyRoom;
import gwu.cs.network.common.JoinRoom;
import gwu.cs.network.common.QuitRoom;
import gwu.cs.network.common.SetUserName;


import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.SystemColor;

public class ClientWindow {

	private ChatClient chat_client;
	private HashMap<String, ChatDialog> rooms = new HashMap();  
	private JFrame frmChatRoom;
	private JTextField username_text;
	private JTextField create_room_text;
	private JTextField join_room_text;
	private JButton btn_create_room;
	private JButton btn_join_room;
	private JButton btn_set_name;
	private HashMap<String, Boolean> created_rooms = new HashMap();//
	private Set<String> joined_rooms = new HashSet();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {				
					ClientWindow window = new ClientWindow();
					window.frmChatRoom.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientWindow() {
		chat_client = new ChatClient(this);
		chat_client.start();
		initialize();
	}
		

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChatRoom = new JFrame();
		frmChatRoom.getContentPane().setBackground(SystemColor.inactiveCaption);
		frmChatRoom.setBounds(100, 100, 440, 782);
		frmChatRoom.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChatRoom.getContentPane().setLayout(null);
		
		
		UIManager.put("OptionPane.cancelButtonText", "Cancel");
	    UIManager.put("OptionPane.noButtonText", "No");
	    UIManager.put("OptionPane.okButtonText", "OK");
	    UIManager.put("OptionPane.yesButtonText", "Yes");
	    UIManager.put("OptionPane.messageDialogTitle", "Message");

		
		/*Some piece of code*/
		frmChatRoom.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	super.windowClosing(windowEvent);	    	
		    	/*for(String room:created_rooms) {
		    		DestroyRoom destroy_room = new DestroyRoom(chat_client.username, room);
					chat_client.send(destroy_room.serilize());
		    	}
		    	
		    	for (String room:joined_rooms) {
		    		QuitRoom quit_room = new QuitRoom(chat_client.username, room);
					chat_client.send(quit_room.serilize());
		    	}*/
		    	chat_client.closeSocket();
		    }
		});

		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.inactiveCaption);
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(14, 331, 394, 92);
		frmChatRoom.getContentPane().add(panel);
		panel.setLayout(null);
		
		create_room_text = new JTextField();
		create_room_text.setBounds(29, 32, 166, 29);
		panel.add(create_room_text);
		create_room_text.setColumns(20);
		create_room_text.setEnabled(false);
		
		btn_create_room = new JButton("Create Room");
		btn_create_room.setBounds(229, 32, 145, 29);
		btn_create_room.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String roomID = create_room_text.getText();
					if (created_rooms.containsKey(roomID)) {
						JOptionPane.showMessageDialog(frmChatRoom, "You have already created \n this room, do not create multiple times");
					} else {
						CreateRoom create_room = new CreateRoom(chat_client.username, roomID);
						chat_client.send(create_room.serilize());	
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btn_create_room.setFont(new Font("宋体", Font.BOLD, 18));
		panel.add(btn_create_room);
		btn_create_room.setEnabled(false);
		
		JLabel lblNewLabel = new JLabel("Create Room");
		lblNewLabel.setFont(new Font("宋体", Font.BOLD, 18));
		lblNewLabel.setBounds(14, 306, 112, 24);
		frmChatRoom.getContentPane().add(lblNewLabel);
		
		JLabel lblJoinRoom = new JLabel("Join Room");
		lblJoinRoom.setFont(new Font("宋体", Font.BOLD, 18));
		lblJoinRoom.setBounds(14, 501, 112, 24);
		frmChatRoom.getContentPane().add(lblJoinRoom);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(SystemColor.inactiveCaption);
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(14, 526, 394, 92);
		frmChatRoom.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		join_room_text = new JTextField();
		join_room_text.setBounds(30, 32, 166, 29);
		join_room_text.setColumns(20);
		panel_1.add(join_room_text);
		join_room_text.setEnabled(false);
		
		btn_join_room = new JButton("Join Room");
		btn_join_room.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String roomID = join_room_text.getText();
					if (joined_rooms.contains(roomID) || (created_rooms.containsKey(roomID) && created_rooms.get(roomID) == true)) {
						JOptionPane.showMessageDialog(frmChatRoom, "You are already in this room");
					} else {
						JoinRoom join_room = new JoinRoom(chat_client.username, roomID);
						chat_client.send(join_room.serilize());		
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btn_join_room.setBounds(229, 32, 145, 29);
		btn_join_room.setFont(new Font("宋体", Font.BOLD, 18));
		panel_1.add(btn_join_room);
		btn_join_room.setEnabled(false);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(SystemColor.inactiveCaption);
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.setBounds(14, 114, 394, 92);
		frmChatRoom.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		username_text = new JTextField();
		username_text.setBounds(30, 32, 166, 29);
		panel_2.add(username_text);
		username_text.setColumns(20);
		
		btn_set_name = new JButton("Set Name");
		btn_set_name.setForeground(new Color(0, 0, 0));
		btn_set_name.setBounds(229, 32, 145, 29);
		panel_2.add(btn_set_name);
		btn_set_name.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = username_text.getText();
				
				SetUserName set_username = new SetUserName(username);
				chat_client.send(set_username.serilize());
				try {
					System.out.write(set_username.serilize());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				};
			}
		});
		btn_set_name.setFont(new Font("宋体", Font.BOLD, 18));
		
		JLabel lblUsername = new JLabel("Set Name");
		lblUsername.setBounds(14, 86, 138, 21);
		frmChatRoom.getContentPane().add(lblUsername);
		lblUsername.setFont(new Font("宋体", Font.BOLD, 18));
	}

	public void handleDataMessage(String username, String roomID, String message) {
		ChatDialog dialog = rooms.get(roomID);
		if (dialog != null) {
			message = message.trim();
			dialog.showMessage(username, message);
		}
	}
	
	public void handleSetUserNameResponse(int result) {
		if (result == 0) {
			create_room_text.setEnabled(true);
			join_room_text.setEnabled(true);
			btn_create_room.setEnabled(true);
			btn_join_room.setEnabled(true);
			this.btn_set_name.setEnabled(false);
			username_text.setEnabled(false);
			this.frmChatRoom.setTitle(chat_client.username);
			
			JOptionPane.showMessageDialog(this.frmChatRoom, "Username has been set successfully. \n"
					             + "You can create a new chat room or \n join an existing chat room.");			
		} else {		
			JOptionPane.showMessageDialog(this.frmChatRoom, "Username already exist, please try another one!");
		}
	}
	
	public void handleCreateRoomResponse(int result, String roomID) {
		if (result == 0) {
			ChatDialog dialog = new ChatDialog(chat_client, chat_client.username, roomID);
			String title = chat_client.username + "-" + roomID;
			dialog.setTitle(title);
			rooms.put(roomID, dialog);
			dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			dialog.setVisible(true);
			created_rooms.put(roomID, true);
		} else {
			JOptionPane.showMessageDialog(this.frmChatRoom, "Room ID already exist, please try another one!");
		}
	}
	
	public void handleJoinRoomResponse(int result, String roomID) {
		if (result == 0) {
			ChatDialog dialog = new ChatDialog(chat_client, chat_client.username, roomID);
			String title = chat_client.username + "-" + roomID;
			dialog.setTitle(title);
			if (created_rooms.containsKey(roomID)) { 			
				created_rooms.put(roomID, true);
			} else {
				joined_rooms.add(roomID);
				dialog.btn_destory_room.setEnabled(false);
			}
			rooms.put(roomID, dialog);
			dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			dialog.setVisible(true);			
		} else {
			JOptionPane.showMessageDialog(this.frmChatRoom, "Room does not exist, \n make sure the roomID is correct");
		}
	}

	public void handleQuitRoomResponse(int result, String roomID) {
		if (result == 0) {
			ChatDialog dialog = rooms.remove(roomID);
			if (created_rooms.containsKey(roomID)) {
				created_rooms.put(roomID, false);
			}
			joined_rooms.remove(roomID);
			dialog.dispose();
		} else {
			JOptionPane.showMessageDialog(this.frmChatRoom, "Quit room failed, please try again");
		}
	}
	
	public void handleDestroyRoomResponse(int result, String roomID, String userID) {
		if (result == 0) {
			ChatDialog dialog = rooms.remove(roomID);
			dialog.destroyRoom(userID);
			created_rooms.remove(roomID);
			joined_rooms.remove(roomID);
		} else if (userID.equals(chat_client.username)){
			ChatDialog dialog = rooms.get(roomID);
			JOptionPane.showMessageDialog(dialog, "Destroy room failed, you are not allowed \n to destroy a room that not created by you");
		}
	}
	public void handleGetUserListResponse(Set<String> user_list, String roomID) {
		// TODO Auto-generated method stub
		ChatDialog dialog = rooms.get(roomID);
		if (dialog != null) {
			//System.out.println("update userlist for room:" + roomID + " user_list size is:" + user_list.size());
			dialog.updateUserList(user_list);
		}
	}
}
