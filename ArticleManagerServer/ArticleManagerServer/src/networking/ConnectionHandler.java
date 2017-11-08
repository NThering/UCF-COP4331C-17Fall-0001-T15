package networking;

import java.util.ArrayList;

public class ConnectionHandler {
	private int maxConnections;
	private ArrayList<Connection> connectionsList = new ArrayList<Connection>();
	private Connection nextConnection;
	public static final int LISTENPORT = 1906;
	public static final int TOLERANCE = 10;
	
	public ConnectionHandler(int connectionCap)
	{
		maxConnections = connectionCap;
		nextConnection = new Connection(LISTENPORT, TOLERANCE);
		nextConnection.start();
	}
	
	public void update()
	{
		if(!nextConnection.isWaiting())
		{
			System.out.println("Received new connection!");
			if(connectionsList.size() < maxConnections)
			{
				connectionsList.add(nextConnection);
				nextConnection = new Connection(LISTENPORT, TOLERANCE);
				nextConnection.start();
			} else
			{
				System.err.println("Failed to add new client: Too many connections.");
				//TODO: Send some indication as to the reason for rejection to the client.
				nextConnection = new Connection(LISTENPORT, TOLERANCE);
				nextConnection.start();
			}
		}
		
		for(int i = 0; i < connectionsList.size(); i++)
		{
			if(connectionsList.get(i).isAlive() == false)
			{
				connectionsList.remove(i);
				System.out.println("Connection removed.");
			}
		}
		
	}
}
