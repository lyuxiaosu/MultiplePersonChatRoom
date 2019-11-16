package gwu.cs.network.client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import gwu.cs.network.common.CreateRoom;
import gwu.cs.network.common.DataMessage;
import gwu.cs.network.common.SetUserName;

import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;

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
		frmChatRoom.setTitle("Chat Room");
		frmChatRoom.setBounds(100, 100, 440, 782);
		frmChatRoom.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChatRoom.getContentPane().setLayout(null);
		
		/*Some piece of code*/
		frmChatRoom.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	super.windowClosing(windowEvent);
		    	chat_client.closeSocket();
		    }
		});

		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(14, 331, 394, 92);
		frmChatRoom.getContentPane().add(panel);
		panel.setLayout(null);
		
		create_room_text = new JTextField();
		create_room_text.setBounds(30, 30, 166, 24);
		panel.add(create_room_text);
		create_room_text.setColumns(20);
		create_room_text.setEnabled(false);
		
		btn_create_room = new JButton("Create Room");
		btn_create_room.setBounds(229, 32, 140, 29);
		btn_create_room.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String roomID = create_room_text.getText();
					CreateRoom create_room = new CreateRoom(chat_client.username, roomID);
					chat_client.send(create_room.serilize());				
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btn_create_room.setFont(new Font("宋体", Font.PLAIN, 18));
		panel.add(btn_create_room);
		btn_create_room.setEnabled(false);
		
		JLabel lblNewLabel = new JLabel("Create Room");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 18));
		lblNewLabel.setBounds(14, 306, 112, 24);
		frmChatRoom.getContentPane().add(lblNewLabel);
		
		JLabel lblJoinRoom = new JLabel("Join Room");
		lblJoinRoom.setFont(new Font("宋体", Font.PLAIN, 18));
		lblJoinRoom.setBounds(14, 501, 112, 24);
		frmChatRoom.getContentPane().add(lblJoinRoom);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(14, 526, 394, 92);
		frmChatRoom.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		join_room_text = new JTextField();
		join_room_text.setBounds(30, 30, 166, 24);
		join_room_text.setColumns(20);
		panel_1.add(join_room_text);
		join_room_text.setEnabled(false);
		
		btn_join_room = new JButton("Join Room");
		btn_join_room.setBounds(238, 30, 128, 29);
		btn_join_room.setFont(new Font("宋体", Font.PLAIN, 18));
		panel_1.add(btn_join_room);
		btn_join_room.setEnabled(false);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.setBounds(14, 114, 394, 92);
		frmChatRoom.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		username_text = new JTextField();
		username_text.setBounds(30, 30, 166, 24);
		panel_2.add(username_text);
		username_text.setColumns(20);
		
		btn_set_name = new JButton("Set Name");
		btn_set_name.setBounds(242, 30, 126, 29);
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
		btn_set_name.setFont(new Font("宋体", Font.PLAIN, 18));
		
		JLabel lblUsername = new JLabel("Set Name");
		lblUsername.setBounds(14, 86, 138, 21);
		frmChatRoom.getContentPane().add(lblUsername);
		lblUsername.setFont(new Font("宋体", Font.PLAIN, 18));
	}

	public void handleDataMessage(String username, String roomID, String message) {
		ChatDialog dialog = rooms.get(roomID);
		if (dialog != null) {
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
			
			JOptionPane.showMessageDialog(this.frmChatRoom, "Username has been set successfully.");			
		} else {		
			JOptionPane.showMessageDialog(this.frmChatRoom, "Username already exist, please try another one!");
		}
	}
	
	public void handleCreateRoomResponse(int result, String roomID) {
		if (result == 0) {
			ChatDialog dialog = new ChatDialog(roomID);
			dialog.setTitle(roomID);
			rooms.put(roomID, dialog);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(this.frmChatRoom, "Room ID already exist, please try another one!");
		}
	}
}
