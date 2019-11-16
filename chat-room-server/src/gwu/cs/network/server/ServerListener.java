package gwu.cs.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class ServerListener extends Thread {
	public void run() {
		try {
            ServerSocket serverSocket = new ServerSocket(23456);
            System.out.println("Server is listening on port 23456"); 
            while (true) {
                // block
                Socket socket = serverSocket.accept();              
                System.out.println("A client come in");           
                ChatSocket cs= new ChatSocket(socket, ChatManager.getChatManager());
                cs.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
