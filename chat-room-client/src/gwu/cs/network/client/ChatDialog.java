package gwu.cs.network.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import gwu.cs.network.common.DataMessage;
import gwu.cs.network.common.DestroyRoom;
import gwu.cs.network.common.GetUserList;
import gwu.cs.network.common.QuitRoom;

import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Set;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.SystemColor;

public class ChatDialog extends JDialog {
	
	private ChatClient chat_client;
	private String roomID = "";
	private String userID = "";
	private final JPanel contentPanel = new JPanel();
	

	private JTextArea recv_text;
	private JTextArea send_text;
	private JButton btn_send;
	private JList user_list;
	public JButton btn_destory_room;
	private JButton btn_quit_room;
	private DefaultListModel listmodel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			/*ChatDialog dialog = new ChatDialog(null, "", "");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ChatDialog(ChatClient chat_client, String userID, String roomID) {
		setBackground(SystemColor.menu);
		this.chat_client = chat_client;
		this.roomID = roomID;
		this.userID = userID;
		setBounds(100, 100, 694, 626);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(SystemColor.inactiveCaption);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		
		recv_text = new JTextArea();
		recv_text.setForeground(new Color(255, 0, 0));
		recv_text.setBackground(SystemColor.inactiveCaptionBorder);
		recv_text.setFont(new Font("Monospaced", Font.BOLD, 25));
		recv_text.setLineWrap(true);
		recv_text.setBounds(5, 0, 474, 406);
		contentPanel.add(recv_text);
		
		
		send_text = new JTextArea();
		send_text.setForeground(SystemColor.desktop);
		send_text.setBackground(SystemColor.inactiveCaptionBorder);
		send_text.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
		send_text.setLineWrap(true);
		send_text.setBounds(5, 410, 474, 132);
		contentPanel.add(send_text);
		
		
		user_list = new JList();
		user_list.setBackground(SystemColor.inactiveCaptionBorder);
		user_list.setFont(new Font("Consolas", Font.PLAIN, 25));
		user_list.setForeground(new Color(0, 128, 0));
		user_list.setBounds(483, 0, 193, 406);
		contentPanel.add(user_list);
		
		listmodel = new DefaultListModel();
		user_list.setModel(listmodel);
		
		btn_send = new JButton("Send");
		btn_send.setBackground(SystemColor.activeCaptionBorder);
		btn_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String content = send_text.getText();
				DataMessage msg = new DataMessage(userID, roomID, content);
				chat_client.send(msg.serilize());
				send_text.setText("");
			}
		});
		btn_send.setFont(new Font("ËÎÌå", Font.PLAIN, 22));
		btn_send.setBounds(510, 437, 140, 65);
		contentPanel.add(btn_send);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
		btn_destory_room = new JButton("Destroy Room");
		btn_destory_room.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DestroyRoom destroy_room = new DestroyRoom(userID, roomID);
				chat_client.send(destroy_room.serilize());
			}
		});
		btn_destory_room.setBackground(SystemColor.activeCaptionBorder);
		btn_destory_room.setActionCommand("OK");
		buttonPane.add(btn_destory_room);
		getRootPane().setDefaultButton(btn_destory_room);
			
			
		btn_quit_room = new JButton("  Quit Room  ");
		btn_quit_room.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuitRoom quit_room = new QuitRoom(userID, roomID);
				chat_client.send(quit_room.serilize());
			}
		});
		btn_quit_room.setBackground(SystemColor.activeCaptionBorder);
		btn_quit_room.setActionCommand("Cancel");
		buttonPane.add(btn_quit_room);					
		GetUserList user_list = new GetUserList(roomID);
		chat_client.send(user_list.serilize());
	}
	public void showMessage(String username, String msg) {
		this.recv_text.append(username + ":" + msg + "\n");
	}
	
	public void updateUserList(Set<String> userlist) {
		this.listmodel.removeAllElements();
		for (String username: userlist) {
			System.out.println("-----------update userlist");
			listmodel.addElement(username);
		}
	}
	
	public void destroyRoom(String userID) {
		System.out.print("this.userID:" + this.userID + ", userID:¡¡" + userID);
		
		if (this.userID.equals(userID)) { 
			System.out.println("-------------enter destroyRoom-----------");
			this.dispose();
		} else {
			JOptionPane.showMessageDialog(this, "room " + this.userID+ "-" + this.roomID + " is destroyed", "warning", JOptionPane.WARNING_MESSAGE);		
			this.dispose();
		}
	}
}
