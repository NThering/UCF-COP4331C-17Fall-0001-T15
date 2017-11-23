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
    
private static String driver = "org.mariadb.jdbc.Driver";
    private static String URL = "jdbc:mariadb://localhost/article_manager";
    private static String USER = "root";
    private static String PASS = "cop4331";
    private static String genPassword = null;

    public static Connection conn = null;

    public static int login(String username, String password) throws NoSuchAlgorithmException, NoSuchProviderException
    {
        /**	Checks the username and password against the stored password hashes on the database.  Returns -1 if login failed and an integer corresponding to that user's permissions if successful. */
        /** Passwords must be stored securely ( NOT IN PLAINTEXT ) so that a data breach would not compromise them. */
        try
        {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(URL, USER, PASS);

            PreparedStatement statement = connection.prepareStatement("SELECT 'username', 'password' FROM users WHERE 'username' = ? " +
                    "AND 'password' = ?");
            ResultSet r1 = statement.executeQuery();
            statement.setString(1, username);
            statement.setString(2, password);
            String usernameCounter;
            byte[] salty = salt();
            if(r1.next())
            {
                if(r1.getString("password").equals(securePassword(password, salty))) {
                    System.out.println("Login successful");
                    return 0; //return 0 for user
                }

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
    public static boolean register(String username, String password) throws NoSuchAlgorithmException, NoSuchProviderException
    {
        try
        {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection connection = DriverManager.getConnection(URL, USER, PASS);

            PreparedStatement st = connection.prepareStatement("select * from users order by username desc");
            PreparedStatement st2 = connection.prepareStatement("INSERT INTO users (username) VALUES (?)");
            ResultSet r1 = st.executeQuery();
            Statement s = connection.createStatement();
            String usernameCounter;
            byte[] salty = salt();
            if(r1.next())
            {
                usernameCounter = r1.getString("username");
                if(usernameCounter.equals(username))
                {
                    System.out.println("Username already exists");
                    return false;
                }

                else{

                    //add username to the database.
                    System.out.println("Username is available!");
                    st2.executeQuery();
                }
            }

            String secure = securePassword(password, salty);

            s.executeUpdate("INSERT INTO 'users'(passwords) VALUES (secure)");

        }

        catch (SQLException e)
        {
            System.out.println("SQL Excpetion: " + e.toString());
        }

        catch (ClassNotFoundException ce)
        {
            System.out.println("Class Not Found Exception: " + ce.toString());
        }
        return true;
    }

    public static String securePassword(String password, byte[] salt) throws NoSuchAlgorithmException
    {
        //utilizing MD5 algorithm to hash passwords.
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());

        byte []byteData = md.digest();

        //convert byte to hex
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < byteData.length; i++)
        {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        //Gets hashed password
        genPassword = sb.toString();

        return genPassword;
    }

    public static byte[] salt() throws NoSuchAlgorithmException, NoSuchProviderException
    {
        SecureRandom sec = SecureRandom.getInstance("SHA1PRNG", "SUN");

        //create salt array
        byte[]salt = new byte[16];

        //get random salt
        sec.nextBytes(salt);

        return salt;
    }
}
