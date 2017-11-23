package networking;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import am_utils.ArticleInfo;
import am_utils.CategoryEnums.MainCategory;
import am_utils.CategoryEnums.SubCategory;
import java.sql.*;
import static java.util.concurrent.TimeUnit.*;

public class Public extends Thread {
    /*    General Responsibilities:
    *     Keep track of logged in users and their permissions so they don't need to re-authenticate
    *     for every action.
    */

    // All calls to networking should probably be done in a seperate thread to prevent hangups!

    //socket to send/receive data
    private Socket clientSocket = null;
    private ObjectInputStream inputObject = null;
    private ObjectOutputStream outputObject = null;

    private final String HOSTNAME = "odroid.now-dns.net";
    private final int PORT = 1906;

    //keep connection established (2 to 5 sec heartbeat intervals)
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    //BufferedReader for retrieving data
    private static BufferedReader reader = null;

    //PrintWriter for sending data
    private static PrintWriter writer = null;

    public static SessionManager session = null;

    //heartbeat sends "0" to server every 3 secs, maintain connection
    public void setHeartbeat() {
        final Runnable heartbeat = new Runnable() {
            public void run() {
                clientSocket.getOutputStream("0");
            }
        };
        final ScheduledFuture beatHandler =
                scheduler.scheduleAtFixedRate(heartbeat, 3, 3, TimeUnit.SECONDS);

        scheduler.schedule(new Runnable() {
            public void run() {
                beatHandler.cancel(true);
            }
        }, 3600, TimeUnit.SECONDS);
    }

    //initialize socket, object streams and printwriter
    try {
        clientSocket = new Socket(HOSTNAME, PORT);
        inputObject = new ObjectInputStream(clientSocket.getInputStream());
        outputObject = new ObjectOutputStream(clientSocket.getOutputStream());

        writer = new PrintWriter(outputObject, true);

        System.out.println("Connection successful");
    }
    catch (UnkownHostException e) {
        System.err.println("Unknown host: " + HOSTNAME);
    }
    catch (IOException e) {
        System.err.println("I/O connection failed");
    }

    /** Relays user info to the login system on the server, returning 0 on the client if the login
     * was successful and an int corresponding to the type of error if it was not. */
    public static int login( String username, String password )
    {
        writer.println("1 " + username + " " + password);

        if (reader.readLine().equals("0")) {
            session.createSession(username, password);
            return 0;
        }
        else {
            return 1;
        }
    }

    /** Attempts to log out of the server, returning 0 on the client if the logout was successful
     * and an int corresponding to the type of error if it was not. */
    public static int logout( )
    {
        writer.println("2");

        session.logoutSession();
        return 0;
    }

    /** Relays user info to the login system on the server, returning 0 on the client if the
     * registration was successful and an int corresponding to the type of error if it was not. */
    public static int register( String username, String password )
    {
        writer.println("3 " + username + " " + password);

        return (reader.readLine.().equals("0")) ? 0 : 1;
    }

    /** Returns a list to the client of all ArticleInfos in a given category/sub-category.  If
     * IncludeAbstracts is false, abstracts are left blank to save bandwidth.*/
    public static ArrayList<ArticleInfo> getArticlesFromCategory( int mainCategoryID,
          int subCategoryID, boolean IncludeAbstracts )
    {
        //prepare includeAbstacts for output
        String bool = (includeAbstracts) ? "1" : "0";

        writer.println("4 " + bool);
        outputObject.writeObject(articleCategory);
        outputObject.writeObject(subCategory);

        return inputObject.readObject();
    }

    /** Sends the article to the database for entry, returns 0 if successful and an int
     * corresponding to the type of error if it was not.  If ArticleID in the supplied ArticleInfo
     * is not 0 this upload is meant to replace an existing upload. */
    public static int uploadArticle( File articleFile, ArticleInfo articleInfo )
    {
        int fileSize = articleFile.length();
        byte[] articleByteArray = new byte[fileSize];

        writer.println("5 " + fileSize.toString());
        //send articleInfo
        outputObject.writeObject(articleInfo);

        try {
            FileOutputStream fileOutStream = new FileOutputStream("");
            BufferedOutputStream outStream = new BufferedOutputStream(fileOutStream);
            outStream.write(articleByteArray, 0, fileSize);

            return 0;
        }
        catch (FileNotFoundException e) {
            System.err.println("File not found");
            return 1;
        }

    }

    /** Gets information on the given article from the database. */
    public static ArticleInfo getArticleInfo( int articleID )
    {
        writer.println("6 " + articleID.toString());

        articleInfo = inputObject().readObject();

        return articleInfo;
    }

    /** Downloads the given article from the database. */
    public static File downloadArticle( int articleID )
    {
        writer.println("7 " + articleID.toString());

        //receive file size
        int fileSize = Integer.parseInt(reader.readLine());
        //byte array
        byte[] articleByteArray = new byte[fileSize];

        InputStream inStream = clientSocket.getInputStream();

        try {
            int bytesReadIn = inStream.read(articleByteArray, 0, fileSize);
        }
        catch (IOException e) {
            System.err.println("Article download failed");
        }

        //receive article
        return ;
    }

    /** Gets the permissions flags for the user, returning -1 if not logged in. */
    public static int getPermissions()
    {
        writer.println("8");

        return Integer.parseInt(reader.readLine());
    }

    /** Abort any ongoing networking activity  Do not terminate the user's session, just cancel
     * whatever transfers were taking place in case the user hits the back button or something. */
    public static void abortActiveConnections()
    {

    }
}
