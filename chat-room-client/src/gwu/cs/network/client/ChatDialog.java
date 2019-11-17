package gwu.cs.network.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import gwu.cs.network.common.DataMessage;
import gwu.cs.network.common.GetUserList;

import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JList;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Set;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class ChatDialog extends JDialog {
	
	private ChatClient chat_client;
	private String roomID = "";
	private String userID = "";
	private final JPanel contentPanel = new JPanel();
	

	private JTextArea recv_text;
	private JTextArea send_text;
	private JButton btn_send;
	private JList user_list;
	private JButton btn_destory_room;
	private JButton btn_quit_room;
	private DefaultListModel listmodel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ChatDialog dialog = new ChatDialog(null, "", "");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ChatDialog(ChatClient chat_client, String userID, String roomID) {
		this.chat_client = chat_client;
		this.roomID = roomID;
		this.userID = userID;
		setBounds(100, 100, 694, 626);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(0, 139, 139));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		recv_text = new JTextArea();
		recv_text.setForeground(new Color(255, 0, 0));
		recv_text.setBackground(new Color(224, 255, 255));
		recv_text.setFont(new Font("Monospaced", Font.BOLD, 25));
		recv_text.setLineWrap(true);
		recv_text.setBounds(10, 25, 459, 381);
		contentPanel.add(recv_text);
		
		
		send_text = new JTextArea();
		send_text.setBackground(new Color(224, 255, 255));
		send_text.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
		send_text.setLineWrap(true);
		send_text.setBounds(10, 428, 459, 92);
		contentPanel.add(send_text);
		
		
		user_list = new JList();
		user_list.setBackground(new Color(224, 255, 255));
		user_list.setFont(new Font("Consolas", Font.PLAIN, 25));
		user_list.setForeground(new Color(0, 128, 0));
		user_list.setBounds(483, 26, 175, 381);
		contentPanel.add(user_list);
		
		listmodel = new DefaultListModel();
		user_list.setModel(listmodel);
		
		btn_send = new JButton("Send");
		btn_send.setBackground(new Color(100, 149, 237));
		btn_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String content = send_text.getText();
				DataMessage msg = new DataMessage(userID, roomID, content);
				chat_client.send(msg.serilize());
				send_text.setText("");
			}
		});
		btn_send.setFont(new Font("ËÎÌו", Font.PLAIN, 22));
		btn_send.setBounds(483, 442, 140, 65);
		contentPanel.add(btn_send);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
		btn_destory_room = new JButton("Destroy Room");
		btn_destory_room.setBackground(new Color(100, 149, 237));
		btn_destory_room.setActionCommand("OK");
		buttonPane.add(btn_destory_room);
		getRootPane().setDefaultButton(btn_destory_room);
			
			
		btn_quit_room = new JButton("Quit Room");
		btn_quit_room.setBackground(new Color(100, 149, 237));
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
}
