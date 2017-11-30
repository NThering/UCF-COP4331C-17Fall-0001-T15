package articleManagerServer;

import networking.ConnectionHandler;
import testing.PublicTestDB;
import testing.PublicTestUsers;

public class ArticleManagerServer {
	public static void main(String[] args)
	{
		System.out.println("Starting server...");
		boolean running = true;
		
		if(args.length == 1)
		{
			if(args[0].equals("test"))
			{
				System.out.println("Launching with command line parameter 'test'");
				PublicTestDB dbTest = new PublicTestDB();
				PublicTestUsers userTest = new PublicTestUsers();
				dbTest.runTest();
				userTest.runTest();
			}
		}
		
		ConnectionHandler serverConnectionHandler = new ConnectionHandler(10);
		
		System.out.println("Connection handler successfully created.");
		
		while(running)
		{
			serverConnectionHandler.update();
		}
	}
}
