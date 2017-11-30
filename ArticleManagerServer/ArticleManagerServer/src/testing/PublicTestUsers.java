package testing;

import users.Public;

public class PublicTestUsers {
	
	String[] testUsernames = new String[5];
	String[] testPasswords = new String[5];
	public PublicTestUsers()
	{
		testUsernames[0] = "User0";
		testUsernames[1] = "User1";
		testUsernames[2] = "User2";
		testUsernames[3] = "User3";
		testUsernames[4] = "User4";
		
		testPasswords[0] = "Password0";
		testPasswords[1] = "12345678";
		testPasswords[2] = "BadPasswords";
		testPasswords[3] = "AnotherPassword";
		testPasswords[4] = "JustOneMore!";
	}
	
	public void runTest()
	{
		int errcode;
		errcode = testRegister();
		if(errcode != 0)
		{
			System.out.println("User registration test failed with error code " + errcode);
			return;
		} else
		{
			System.out.println("User registration test passed.");
		}
		
		errcode = testDuplicateRegister();
		if(errcode != 0)
		{
			System.out.print("Duplicate user registration test failed.");
		} else
		{
			System.out.println("Duplicate user registration test passed.");
		}
		
		errcode = loginTest();
		if(errcode != 0)
		{
			
		}
	}
	
	public int testRegister()
	{
		
		for(int i = 0; i < 5; i++)
		{
			if(!users.Public.register(testUsernames[i],testPasswords[i]))
			{
				return -1;
			}
		}
		return 0;
	}
	
	public int testDuplicateRegister()
	{
		if(!users.Public.register(testUsernames[0], testPasswords[0]))
		{
			return 0; // We want the registration to fail in this case, return 0 if so.
		} else
		{
			return -1;
		}
	}
	
	public int loginTest()
	{
		if(users.Public.login("testLogin", "testPassword") == 0)
		{
			return -1; // We should not be able to log in successfully with these parameters.
		}

		users.Public.register("testLogin", "testPassword");
		if(users.Public.login("testLogin", "testPassword") == 0)
		{
			return 0;
		} else
		{
			return 1;
		}
	}
}
