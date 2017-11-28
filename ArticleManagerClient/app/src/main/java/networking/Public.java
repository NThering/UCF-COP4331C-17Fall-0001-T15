package networking;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import am_utils.ArticleInfo;
import java.sql.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import android.content.Context;
import static java.util.concurrent.TimeUnit.*;

public class Public extends Thread {
    /*    General Responsibilities:
    *     Keep track of logged in users and their permissions so they don't need to re-authenticate
    *     for every action.
    */

    // All calls to networking should probably be done in a seperate thread to prevent hangups!

    //socket to send/receive data
    protected Socket clientSocket;
    protected ObjectInputStream inputObject;
    protected ObjectOutputStream outputObject;

    protected final String HOSTNAME = "odroid.now-dns.net";
    protected final int PORT = 1906;

    //keep connection established (2 to 5 sec heartbeat intervals)
    protected final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    //BufferedReader for retrieving data
    protected static BufferedReader reader;

    //PrintWriter for sending data
    protected static PrintWriter writer;

    //shared preferences
    try {
        public static SessionManager session = new SessionManager(getActivity().getApplicationContext());
    }
    catch (Exception e) {
        System.err.println(e.getMessage());
    }
    catch (NameNotFoundException e) {
        System.err.println(e.getMessage());
    }
    catch (NotContextException e) {
        System.err.println(e.getMessage());
    }

    //initialize socket, object streams and printwriter
    try {
        clientSocket = new Socket(HOSTNAME, PORT);
        inputObject = new ObjectInputStream(clientSocket.getInputStream());
        outputObject = new ObjectOutputStream(clientSocket.getOutputStream());

        //writer = new PrintWriter(outputObject, true);
        writer = new PrintWriter(clientSocket.getOutputStream(), true);

        System.out.println("Connection successful");
    }
    catch (Exception e) {
        System.err.println(e.getMessage());
    }
    catch (UnknownHostException e) {
        System.err.println("Unknown host: " + HOSTNAME);
    }
    catch (IOException e) {
        System.err.println("I/O connection failed: ");
    }

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

    /** Relays user info to the login system on the server, returning 0 on the client if the login
     * was successful and an int corresponding to the type of error if it was not. */
    public static int login( String username, String password )
    {
        writer.println("1 " + username + " " + password);
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        if (reader.readLine().equals("0")) {
            session.createSession(username, password);
            reader.close();
            return 0;
        }
        else {
            reader.close();
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
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        private int result = (reader.readLine().equals("0")) ? 0 : 1;
        reader.close();

        return result;
    }

    /** Returns a list to the client of all ArticleInfos in a given category/sub-category.  If
     * IncludeAbstracts is false, abstracts are left blank to save bandwidth.*/
    public static ArrayList<ArticleInfo> getArticlesFromCategory( int mainCategoryID,
                                                                  int subCategoryID, boolean IncludeAbstracts )
    {
        //prepare includeAbstacts for output
        private String bool = (IncludeAbstracts) ? "1" : "0";

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
        private int fileSize = (int)articleFile.length();
        private byte[] articleByteArray = new byte[fileSize];

        writer.println("5 " + Integer.toString(fileSize));
        //send articleInfo
        outputObject.writeObject(articleInfo);

        try {
            FileInputStream fileInStream = new FileInputStream(articleFile);
            BufferedInputStream inStream = new BufferedInputStream(fileInStream);
            inStream.read(articleByteArray, 0, fileSize);

            OutputStream outStream = clientSocket.getOutputStream();
            outStream.write(articleByteArray, 0, fileSize);
            outStream.flush();
            outStream.close();

            return 0;
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            return 1;
        }
        catch (FileNotFoundException e) {
            System.err.println("File not found");
            return 1;
        }
    }

    /** Gets information on the given article from the database. */
    public static ArticleInfo getArticleInfo( int articleID )
    {
        writer.println("6 " + Integer.toString(articleID));

        return inputObject().readObject();
    }

    /** Downloads the given article from the database. */
    public static File downloadArticle( int articleID )
    {
        writer.println("7 " + Integer.toString(articleID));
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        //receive file size and title of article
        private String result = reader.readLine();
        reader.close();

        //prepare results for use
        private String[] resultArray = result.split(" ");
        private int fileSize = Integer.parseInt(resultArray[0]);
        private String articleTitle = resultArray[1];

        //byte array for file data
        private byte[] articleByteArray = new byte[fileSize];

        try {
            File articleDL = new File(getCacheDir(), articleTitle);

            InputStream inStream = clientSocket.getInputStream();
            FileOutputStream fileOutStream = new FileOutputStream(articleDL);
            BufferedOutputStream outStream = new BufferedOutputStream(fileOutStream);

            int bytesReadIn = inStream.read(articleByteArray, 0, fileSize);
            outStream.write(articleByteArray, 0, bytesReadIn);
            outStream.flush();
            outStream.close();

            return articleDL;
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
        catch (IOException e) {
            System.err.println("Article download failed");
            return null;
        }
        catch (NullPointerException e) {
            System.err.println(e.getMessage());
            return null;
        }
        catch (PatternSyntaxException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /** Gets the permissions flags for the user, returning -1 if not logged in. */
    public static int getPermissions()
    {
        writer.println("8");
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        private int permission = Integer.parseInt(reader.readLine());
        reader.close();

        return permission;
    }

    /** Abort any ongoing networking activity  Do not terminate the user's session, just cancel
     * whatever transfers were taking place in case the user hits the back button or something. */
    public static void abortActiveConnections()
    {

    }
}
