package articleManagerServer;

import networking.ConnectionHandler;
import database.Public;

public class ArticleManagerServer {
	public static void main(String[] args)
	{
		boolean running = true;
		
		database.Public.getDBConnection();
		
		ConnectionHandler serverConnectionHandler = new ConnectionHandler(10);
		
		while(running)
		{
			serverConnectionHandler.update();
		}
	}
}
