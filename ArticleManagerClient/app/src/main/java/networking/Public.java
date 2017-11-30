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

class NetworkingHeartbeatManager extends Thread {

    boolean connected = true;

    @Override
    public void run()
    {
        while( true )
        {
            // Don't send heartbeats -too- often!
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {}

            // Send our heartbeat!
            if (connected)
            {
                CUtils.msg("Sending heartbeat!");
                Public.heartBeat();
            }
        }
    }
}

public class Public extends Thread {
    /*    General Responsibilities:
    *     Keep track of logged in users and their permissions so they don't need to re-authenticate
    *     for every action.
    */

    // All calls to networking should probably be done in a seperate thread to prevent hangups!

    // Sign in details
    protected static final String HOSTNAME = "odroid.now-dns.net";
    //protected static final String HOSTNAME = "127.0.0.1";
    protected static final int TEXTPORT = 1906;
    protected static final int OBJECTPORT = 1907;
    protected static final int FILEPORT = 1908;

    // socket to send/receive data, MUST BE STARTED FIRST
    protected static Socket textSocket = getOurSocket();

    //keep connection established (2 to 5 sec heartbeat intervals)
    protected static final NetworkingHeartbeatManager heartbeater = getOurHeartbeater();

    //------------------------------------------------
    // Initialization Functions
    //------------------------------------------------

    private static Socket getOurSocket()
    {
        try
        {
            return new Socket(HOSTNAME, TEXTPORT);
        }
        catch( Exception e )
        {
            CUtils.warning("Failed to create socket!!!");
            CUtils.warning("Details: " + e.getMessage());
            return null;
        }
    }

    private static NetworkingHeartbeatManager getOurHeartbeater()
    {
        NetworkingHeartbeatManager newManager = new NetworkingHeartbeatManager();
        newManager.start();

        return newManager;
    }

    //------------------------------------------------
    // Network transmit functions
    //------------------------------------------------


    public static void heartBeat()
    {
        sendSignal(0);
    }

    private static int sendSignal(int opCode)
    {
        return sendSignal(opCode, null, null);
    }

    private static int sendSignal(int opCode, String param1)
    {
        return sendSignal(opCode, param1, null);
    }

    // writer is a PrintWriter initalized with clientSocket.getOutputStream(), autoflush = true
    private static int sendSignal(int opCode, String param1, String param2)
    {
        if (opCode < 0 || opCode > 10)
        {
            CUtils.warning("Opcode " + String.valueOf(opCode) + " outside of expected range!");
            return 2;
        }

        PrintWriter writer;

        try
        {
            writer = new PrintWriter(textSocket.getOutputStream(), true);
        }
        catch( Exception e )
        {
            CUtils.warning("Failed to create print writer!!!");
            CUtils.warning("Details: " + e.getMessage());
            return 2;
        }


        String signal = String.valueOf(opCode);

        if (param1 != null)
        {
            signal += " " + param1;

            // No need to check second parameter if we don't have a first!
            if (param2 != null)
                signal += " " + param2;
        }

        try
        {
            CUtils.msg("Sending signal: " + signal);
            writer.println(signal);
            heartbeater.connected = true;
        }
        catch(Exception e)
        {
            CUtils.warning("Failed to send signal " + signal);
            return 1;
        }

        return 0;
    }

    private static int sendObject(Object object)
    {
        ObjectOutputStream outputObjectStream;
        Socket clientSocket;

        try
        {
            clientSocket = new Socket(HOSTNAME, OBJECTPORT);
        }
        catch(Exception e)
        {
            CUtils.warning("Failed to create object output stream socket!!!");
            CUtils.warning("Details: " + e.getMessage());
            return 3;
        }

        try
        {
            outputObjectStream =  new ObjectOutputStream(clientSocket.getOutputStream());
        }
        catch( Exception e )
        {
            CUtils.warning("Failed to create object output stream!!!");
            CUtils.warning("Details: " + e.getMessage());
            return 2;
        }

        try
        {
            outputObjectStream.writeObject(object);
            clientSocket.close();
            heartbeater.connected = true;
        }
        catch(Exception e)
        {
            CUtils.warning("Failed to send object!");
            return 1;
        }

        return 0;
    }

    private static int sendFile(File file)
    {
        int fileSize = (int) file.length();
        byte[] articleByteArray = new byte[fileSize];

        Socket clientSocket;

        try
        {
            clientSocket = new Socket(HOSTNAME, FILEPORT);
        }
        catch(Exception e)
        {
            CUtils.warning("Failed to create file output stream socket!!!");
            CUtils.warning("Details: " + e.getMessage());
            return 3;
        }

        try
        {
            FileInputStream fileInStream = new FileInputStream(file);

            //inStream.read(articleByteArray, 0, fileSize);


            CUtils.debugMsg("Read file, time to transmit...");

            OutputStream outStream = clientSocket.getOutputStream();
            CUtils.debugMsg("Got output stream...");

            int count = 0;
            while ((count = fileInStream.read(articleByteArray)) > 0)
            {
                outStream.write(articleByteArray, 0, count);
            }

            //outStream.write(articleByteArray, 0, fileSize);
            CUtils.debugMsg("Wrote file to buffer, time to close stream...");
            fileInStream.close();
            outStream.close();
            CUtils.debugMsg("Closed, time to close socket...");
            clientSocket.close();

            CUtils.debugMsg("Closed!");

            heartbeater.connected = true;
        }
        catch (FileNotFoundException e)
        {
            CUtils.warning("File not found");
            return 2;
        }
        catch (Exception e)
        {
            CUtils.warning(e.getMessage());
            return 1;
        }

        return 0;
    }

    //------------------------------------------------
    // Network receive functions
    //------------------------------------------------

    private static String receiveString()
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(textSocket.getInputStream()));

            String returnVal = reader.readLine();

            return returnVal;
        }
        catch(Exception e)
        {
            CUtils.warning("Failed to receive string!!!");
            CUtils.warning(e.getMessage());
            return null;
        }
    }

    private static Object receiveObject()
    {
        try
        {
            ObjectInputStream inputObject = null;
            Socket clientSocket;

            try
            {
                clientSocket = new Socket(HOSTNAME, OBJECTPORT);
            }
            catch(Exception e)
            {
                CUtils.warning("Failed to create object input stream socket!!!");
                CUtils.warning("Details: " + e.getMessage());
                return null;
            }

            try
            {
                inputObject = new ObjectInputStream( clientSocket.getInputStream() );
            }
            catch( Exception e )
            {
                CUtils.warning("Failed to create input stream!!!");
                CUtils.warning("Details: " + e.getMessage());
                return null;
            }

            Object receivedObject = inputObject.readObject();

            clientSocket.close();

            return receivedObject;
        }
        catch(Exception e)
        {
            CUtils.warning("Failed to receive any objects!!!");
            CUtils.warning(e.getMessage());
            return null;
        }
    }

    private static File receiveFile( String tempDir, int fileSize )
    {
        //byte array for file data
        byte[] articleByteArray = new byte[fileSize];
        Socket clientSocket;

        try
        {
            clientSocket = new Socket(HOSTNAME, FILEPORT);
        }
        catch(Exception e)
        {
            CUtils.warning("Failed to create file input stream socket!!!");
            CUtils.warning("Details: " + e.getMessage());
            return null;
        }

        try
        {
            File articleDL = new File(tempDir, "DownloadedArticle");

            InputStream inStream = clientSocket.getInputStream();
            FileOutputStream fileOutStream = new FileOutputStream(articleDL);
            BufferedOutputStream outStream = new BufferedOutputStream(fileOutStream);


            int count = 0;
            while ((count = inStream.read(articleByteArray)) > 0)
            {
                fileOutStream.write(articleByteArray, 0, count);
            }

            inStream.close();
            fileOutStream.close();
            clientSocket.close();


            return articleDL;
        }
        catch (IOException e)
        {
            System.err.println("Article download failed");
            return null;
        }
        catch (NullPointerException e)
        {
            System.err.println(e.getMessage());
            return null;
        }
        catch (PatternSyntaxException e)
        {
            System.err.println(e.getMessage());
            return null;
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            return null;
        }
    }

    //------------------------------------------------
    // Public Functions
    //------------------------------------------------

    /** Relays user info to the login system on the server, returning 0 on the client if the login
     * was successful and an int corresponding to the type of error if it was not. */
    public static int login( String username, String password )
    {
        sendSignal(1, username, password);

        return Integer.parseInt(receiveString());
    }

    /** Attempts to log out of the server, returning 0 on the client if the logout was successful
     * and an int corresponding to the type of error if it was not. */
    public static int logout( )
    {
        sendSignal(2);
        return Integer.parseInt(receiveString());
    }

    /** Relays user info to the login system on the server, returning 0 on the client if the
     * registration was successful and an int corresponding to the type of error if it was not. */
    public static int register( String username, String password )
    {
        sendSignal(3, username, password);

        return Integer.parseInt(receiveString());
    }

    /** Returns a list to the client of all ArticleInfos in a given category/sub-category.  If
     * IncludeAbstracts is false, abstracts are left blank to save bandwidth.*/
    public static ArrayList<ArticleInfo> getArticlesFromCategory( int mainCategoryID, int subCategoryID, boolean IncludeAbstracts )
    {
        sendSignal(4, String.valueOf(mainCategoryID), String.valueOf(subCategoryID));

        return (ArrayList<ArticleInfo>)receiveObject();
    }

    /** Sends the article to the database for entry, returns 0 if successful and an int
     * corresponding to the type of error if it was not.  If ArticleID in the supplied ArticleInfo
     * is not 0 this upload is meant to replace an existing upload. */
    public static int uploadArticle( File articleFile, ArticleInfo articleInfo )
    {
        int fileSize = (int) articleFile.length();
        int errorValue = 0;

        errorValue += sendSignal(5, Integer.toString(fileSize));
        CUtils.debugMsg("Sent signal");
        errorValue += sendObject(articleInfo);
        CUtils.debugMsg("Sent object");
        errorValue += sendFile(articleFile);
        CUtils.debugMsg("Sent file");

        // Will only return 0 if no errors were encountered.
        return errorValue;
    }

    /** Gets information on the given article from the database. */
    public static ArticleInfo getArticleInfo( int articleID )
    {
        sendSignal(6, Integer.toString(articleID));

        return (ArticleInfo)receiveObject();
    }

    /** Downloads the given article from the database. */
    public static File downloadArticle( int articleID, String tempDirectory )
    {
        sendSignal(7, Integer.toString(articleID));


        String receivedString = receiveString();

        CUtils.msg("Got " + receivedString);

        String[] args = receivedString.split(" ");

        //receive file size of article
        int fileSize = Integer.parseInt(args[args.length - 1]);

        File downloadedFile = receiveFile( tempDirectory, fileSize );

        String fileName = "";

        for ( int i = 0; i < args.length - 1; i++ )
        {
            fileName += args[i];
        }

        fileName += ".pdf";

        // Rename our temp file to its final name.
        downloadedFile.renameTo( new File(tempDirectory, fileName) );

        return downloadedFile;
    }

    /** Gets the permissions flags for the user, returning -1 if not logged in. */
    public static int getPermissions()
    {
        sendSignal(8);
        return  Integer.parseInt(receiveString());
    }

    /** Abort any ongoing networking activity  Do not terminate the user's session, just cancel
     * whatever transfers were taking place in case the user hits the back button or something. */
    public static void abortActiveConnections()
    {
        heartbeater.connected = false;
    }

    // I forgot to include these originally so only bother with them if they are easy and you have time.

    /**
     * Returns a list of all articles uploaded by the current user.  Returns null if user has not uploaded anything.
     */
    public static ArrayList<ArticleInfo> getAllArticlesOfCurrentUser()
    {
        sendSignal(9);

        return (ArrayList<ArticleInfo>)receiveObject();
    }
}
