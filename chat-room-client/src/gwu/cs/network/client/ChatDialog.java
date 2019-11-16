package gwu.cs.network.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JList;
import java.awt.Font;

public class ChatDialog extends JDialog {
	
	private String roomID = "";
	private final JPanel contentPanel = new JPanel();
	

	private JTextArea recv_text;
	private JTextArea send_text;
	private JButton btn_send;
	private JList user_list;
	private JButton btn_destory_room;
	private JButton btn_quit_room;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ChatDialog dialog = new ChatDialog("");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ChatDialog(String roomID) {
		this.roomID = roomID;
		setBounds(100, 100, 694, 626);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		recv_text = new JTextArea();
		recv_text.setLineWrap(true);
		recv_text.setBounds(0, 25, 459, 381);
		contentPanel.add(recv_text);
		
		
		send_text = new JTextArea();
		send_text.setLineWrap(true);
		send_text.setBounds(0, 433, 459, 87);
		contentPanel.add(send_text);
		
		
		user_list = new JList();
		user_list.setBounds(483, 26, 175, 381);
		contentPanel.add(user_list);
		
		btn_send = new JButton("Send");
		btn_send.setFont(new Font("ËÎÌו", Font.PLAIN, 22));
		btn_send.setBounds(473, 448, 140, 65);
		contentPanel.add(btn_send);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
		btn_destory_room = new JButton("Destroy Room");
		btn_destory_room.setActionCommand("OK");
		buttonPane.add(btn_destory_room);
		getRootPane().setDefaultButton(btn_destory_room);
			
			
		btn_quit_room = new JButton("Quit Room");
		btn_quit_room.setActionCommand("Cancel");
		buttonPane.add(btn_quit_room);
			
		
	}
	public void showMessage(String username, String msg) {
		this.recv_text.append(username + ":" + msg);
	}
}
