package networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


enum UserType
{
	UNAUTHENTICATED,
	USER,
	ADMIN
}

public class Connection extends Thread {
	private int port; // Port to listen for clients on.
	private Socket clientSocket; // Client socket object.
	private int failTolerance; // Number of consecutive communication failures to tolerate before the connection is considered terminated.
	private boolean waiting; // indicates whether thread is waiting on a client or not. If false, connection manager will place in connection list and spin up a new listener thread.
	private int consecFail;
	private UserType permissionLevel;
	private Object waitingLock = new Object();
	
	Connection(int serverPort, int failTolerance)
	{
		port = serverPort;
		this.failTolerance = failTolerance;
		waiting = true;
		consecFail = 0;
		permissionLevel = UserType.UNAUTHENTICATED;
	}

	public boolean isWaiting()
	{
		synchronized(waitingLock)
		{
			return waiting;
		}
	}
	
	private void parse(String input)
	{
		String[] substrings = input.split(" ");
		String opString;
		String arg1;
		String arg2;
		int opcode;
		opString = substrings[0];
		opcode = Integer.parseInt(opString);
		
		if(substrings.length > 1)
		{
			arg1 = substrings[1];
		}
		if(substrings.length > 2)
		{
			arg2 = substrings[2];
		}
		
		//Try to keep networking sends here and remember to update consecFail as necessary.
		switch(opcode) { //TODO: Add functionality for opcodes here, sending execution off to proper subsystems/modules if necessary.
			case 0:
				consecFail = 0;
				break;
			case 1:
				break; //Reference with database and change permission level if login is valid.
		}
		
	}
	
	public void run()
	{
		BufferedReader clientStream;
		String clientMessage;
		try {
			ServerSocket listener = new ServerSocket(port);
			clientSocket = listener.accept();
			clientSocket.setSoTimeout(2000);
			listener.close();
			synchronized(waitingLock)
			{
				this.waiting = false;
			}
		} catch (IOException e) {
			System.err.println("Error listening for clients.");
			e.printStackTrace();
			return;
		}
		
		try {
			clientStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Failed to create buffered reader for client input stream.");
			return;
		}
		
		while(!clientSocket.isClosed() && consecFail <= failTolerance)
		{
			try {
				clientMessage = clientStream.readLine();
				parse(clientMessage);
				consecFail = 0;
			} catch (IOException e) {
				System.out.println("Listen timeout, consecutive timeouts: " + (consecFail+1));
				consecFail++;
			}
		}
		
		try {
			clientSocket.close();
		} catch (IOException e) {
			System.err.println("Error closing client socket.");
			e.printStackTrace();
		}
	}
}
