package networking;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import am_utils.ArticleInfo;
import am_utils.CUtils;

import java.sql.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.PatternSyntaxException;

import android.content.Context;
import android.content.pm.PackageManager;

import static java.util.concurrent.TimeUnit.*;

public class Public extends Thread {
    /*    General Responsibilities:
    *     Keep track of logged in users and their permissions so they don't need to re-authenticate
    *     for every action.
    */

    // All calls to networking should probably be done in a seperate thread to prevent hangups!

    //socket to send/receive data
    protected static ObjectInputStream inputObject = getOurInputStream();
    protected static ObjectOutputStream outputObject = getOurOutputStream();

    protected static final String HOSTNAME = "odroid.now-dns.net";
    protected static final int PORT = 1906;

    //keep connection established (2 to 5 sec heartbeat intervals)
    protected static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    //BufferedReader for retrieving data
    protected static BufferedReader reader;

    //shared preferences
    public static SessionManager session = new SessionManager(getActivity().getApplicationContext());

    //initialize socket, object streams and printwriter
    protected static Socket clientSocket = getOurSocket();

    //PrintWriter for sending data
    //writer = new PrintWriter(outputObject, true);
    protected static PrintWriter writer = getOurPrintWriter();


    private static Socket getOurSocket()
    {
        try
        {
            return new Socket(HOSTNAME, PORT);
        }
        catch( Exception e )
        {
            CUtils.warning(e.getMessage());
            return null;
        }
    }

    private static PrintWriter getOurPrintWriter()
    {
        try
        {
            return new PrintWriter(clientSocket.getOutputStream(), true);
        }
        catch( Exception e )
        {
            CUtils.warning(e.getMessage());
            return null;
        }
    }

    private static ObjectOutputStream getOurOutputStream()
    {
        try
        {
            return new ObjectOutputStream(clientSocket.getOutputStream());
        }
        catch( Exception e )
        {
            CUtils.warning(e.getMessage());
            return null;
        }
    }

    private static ObjectInputStream getOurInputStream()
    {
        try
        {
            return new ObjectInputStream(clientSocket.getInputStream());
        }
        catch( Exception e )
        {
            CUtils.warning(e.getMessage());
            return null;
        }
    }

    //heartbeat sends "0" to server every 3 secs, maintain connection
    public void setHeartbeat() {
        final Runnable heartbeat = new Runnable() {
            public void run()
            {
                try {
                    clientSocket.getOutputStream().write('0');
                }
                catch(Exception e) {}
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
        try
        {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


            if (reader.readLine().equals("0")) {
                session.createSession(username, password);
                reader.close();
                return 0;
            } else {
                reader.close();
                return 1;
            }
        }
        catch(Exception e)
        {
            CUtils.warning("Failed to network login!");
            return 2;
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
        try
        {
            writer.println("3 " + username + " " + password);
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            int result = (reader.readLine().equals("0")) ? 0 : 1;
            reader.close();

            return result;
        }
        catch(Exception e)
        {
            CUtils.warning("Failed to network register!");
            return 2;
        }
    }

    /** Returns a list to the client of all ArticleInfos in a given category/sub-category.  If
     * IncludeAbstracts is false, abstracts are left blank to save bandwidth.*/
    public static ArrayList<ArticleInfo> getArticlesFromCategory( int mainCategoryID,
                                                                  int subCategoryID, boolean IncludeAbstracts )
    {
        try
        {
            //prepare includeAbstacts for output
            String bool = (IncludeAbstracts) ? "1" : "0";

            writer.println("4 " + bool);
            outputObject.write(mainCategoryID);
            outputObject.write(subCategoryID);

            return inputObject.readObject();
        }
        catch(Exception e)
        {
            CUtils.warning("Failed to network getArticlesFromCategory!");
            return null;
        }
    }

    /** Sends the article to the database for entry, returns 0 if successful and an int
     * corresponding to the type of error if it was not.  If ArticleID in the supplied ArticleInfo
     * is not 0 this upload is meant to replace an existing upload. */
    public static int uploadArticle( File articleFile, ArticleInfo articleInfo )
    {
        try
        {
            int fileSize = (int) articleFile.length();
            byte[] articleByteArray = new byte[fileSize];

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
            } catch (FileNotFoundException e) {
                System.err.println("File not found");
                return 1;
            } catch (Exception e) {
                System.err.println(e.getMessage());
                return 1;
            }
        }
        catch(Exception e)
        {
            CUtils.warning("Failed to network uploadArticle!");
            return 2;
        }
    }

    /** Gets information on the given article from the database. */
    public static ArticleInfo getArticleInfo( int articleID )
    {
        writer.println("6 " + Integer.toString(articleID));

        try
        {
            return inputObject.readObject();
        }
        catch(Exception e)
        {
            CUtils.warning("Failed to network getArticleInfo!");
            return null;
        }
    }

    /** Downloads the given article from the database. */
    public static File downloadArticle( int articleID )
    {
        try {
            writer.println("7 " + Integer.toString(articleID));
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //receive file size and title of article
            String result = reader.readLine();
            reader.close();

            //prepare results for use
            String[] resultArray = result.split(" ");
            int fileSize = Integer.parseInt(resultArray[0]);
            String articleTitle = resultArray[1];

            //byte array for file data
            byte[] articleByteArray = new byte[fileSize];

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
            } catch (Exception e) {
                System.err.println(e.getMessage());
                return null;
            } catch (IOException e) {
                System.err.println("Article download failed");
                return null;
            } catch (NullPointerException e) {
                System.err.println(e.getMessage());
                return null;
            } catch (PatternSyntaxException e) {
                System.err.println(e.getMessage());
                return null;
            }
        }
        catch(Exception e)
        {
            CUtils.warning("Failed to network downloadArticle!");
            return null;
        }
    }

    /** Gets the permissions flags for the user, returning -1 if not logged in. */
    public static int getPermissions()
    {
        try
        {
            writer.println("8");
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            int permission = Integer.parseInt(reader.readLine());
            reader.close();

            return permission;
        }
        catch(Exception e)
        {
            CUtils.warning("Failed to network get permissions!");
            return -2;
        }
    }

    /** Abort any ongoing networking activity  Do not terminate the user's session, just cancel
     * whatever transfers were taking place in case the user hits the back button or something. */
    public static void abortActiveConnections()
    {

    }


    // I forgot to include these originally so only bother with them if they are easy and you have time.

    /**
     * Deletes the article with the specified ID, returning 0 if successful and an integer corresponding to the type
     * of error if it was not.  Users can only delete articles that they have made.
     */
    public static int deleteArticle( int articleID )
    {
        return 1;
    }

    /**
     * Returns the username we are currently logged in under.  Returns null if not logged in.
     */
    public static String getUsername()
    {
        return null;
    }

    /**
     * Returns a list of all articles uploaded by the current user.  Returns null if user has not uploaded anything.
     */
    public static ArrayList<ArticleInfo> getAllArticlesOfCurrentUser()
    {
        return null;
    }
}
