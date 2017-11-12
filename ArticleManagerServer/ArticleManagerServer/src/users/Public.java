/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package users;
import java.util.*;
import java.sql.*;
import org.mariadb.jdbc.Driver;
//import com.mysql.jdbc.Driver;
/**
 *
 * @author NThering
 */

// Responsible for handling user logins.
public class Public {
    /**	Checks the username and password against the stored password hashes on the database.  Returns -1 if login failed and an integer corresponding to that user's permissions if successful. */
    /** Passwords must be stored securely ( NOT IN PLAINTEXT ) so that a data breach would not compromise them. */
    
    private static String driver = "com.mysql.jdbc.Driver";
    private static String URL = "jdbc:mysql://localhost:8080/ArticleManager";
    private static String USER = "root";
    private static String PASS = "";
    
    public static int login( String username, String password )
    {
        try
        {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(URL, USER, PASS);

            PreparedStatement statement = connection.prepareStatement("SELECT 'username', 'password' FROM 'users' WHERE 'username' = ? " +
                    "AND 'password' = ?");
            ResultSet r1 = statement.executeQuery();
            statement.setString(1, username);
            statement.setString(2, password);
            String usernameCounter;
            if(r1.next())
            {
                System.out.println("Login successful");
                return 0;

            }

            else{
                System.out.println("Invalid Login");
            }
        }

        catch (SQLException e)
        {
            System.out.println("SQL Excpetion: " + e.toString());
        }
        catch (ClassNotFoundException ce)
        {
            System.out.println("Class Not Found Exception: " + ce.toString());
        }
        return -1;
    }

    /** Ensures that the username is unique and can be registered in the database, then registers it if so.  Returns true if registration successful and false if not. */
    public static boolean register( String username, String password )
    {
         try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(URL, USER, PASS);

            PreparedStatement st = connection.prepareStatement("select * from Members order by username desc");
            ResultSet r1 = st.executeQuery();
            String usernameCounter;
            if(r1.next())
            {
                usernameCounter = r1.getString("username");
                if(usernameCounter.equals(username))
                {
                    System.out.println("Username already exists");
                }

                else{

                }

            }
        }

        catch (SQLException e)
        {
            System.out.println("SQL Excpetion: " + e.toString());
        }

        catch (ClassNotFoundException ce)
        {
            System.out.println("Class Not Found Exception: " + ce.toString());
        }
        return false;
    }
}
