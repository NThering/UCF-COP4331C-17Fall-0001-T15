/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package users;

/**
 *
 * @author NThering
 */

// Responsible for handling user logins.
public class Public {
    /**	Checks the username and password against the stored password hashes on the database.  Returns -1 if login failed and an integer corresponding to that user's permissions if successful. */
    /** Passwords must be stored securely ( NOT IN PLAINTEXT ) so that a data breach would not compromise them. */
    public static int Login( String username, String password )
    {
        return -1;
    }

    /** Ensures that the username is unique and can be registered in the database, then registers it if so.  Returns true if registration successful and false if not. */
    public static boolean Register( String username, String password )
    {
        return false;
    }
}
