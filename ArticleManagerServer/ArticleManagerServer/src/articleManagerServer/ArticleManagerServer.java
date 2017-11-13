package articleManagerServer;

import networking.ConnectionHandler;

public class ArticleManagerServer {
	public static void main(String[] args)
	{
		System.out.println("Starting server...");
		boolean running = true;
		
		ConnectionHandler serverConnectionHandler = new ConnectionHandler(10);
		
		System.out.println("Connection handle successfully created.");
		
		while(running)
		{
			serverConnectionHandler.update();
		}
	}
}
