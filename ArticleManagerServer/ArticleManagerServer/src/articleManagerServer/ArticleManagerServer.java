package articleManagerServer;

import networking.ConnectionHandler;

public class ArticleManagerServer {
	public static void main(String[] args)
	{
		boolean running = true;
		ConnectionHandler serverConnectionHandler = new ConnectionHandler(10);
		if(database.Public.startDBCon("jdbc:mysql://localhost:1906/(dbname)", "root", "password") != 0)
		{
			System.err.println("Error: Unable to connect to database.");
			return;
		}
		
		while(running)
		{
			serverConnectionHandler.update();
		}
	}
}
