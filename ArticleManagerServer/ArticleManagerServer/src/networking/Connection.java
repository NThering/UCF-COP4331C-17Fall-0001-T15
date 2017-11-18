package networking;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import am_utils.ArticleInfo;

public class Connection extends Thread {
	private int port; // Port to listen for clients on.
	private Socket clientSocket; // Client socket object.
	private int failTolerance; // Number of consecutive communication failures to tolerate before the connection is considered terminated.
	private boolean waiting; // indicates whether thread is waiting on a client or not. If false, connection manager will place in connection list and spin up a new listener thread.
	private int consecFail;
	private int permissionLevel;
	private Object waitingLock = new Object();
	
	Connection(int serverPort, int failTolerance)
	{
		port = serverPort;
		this.failTolerance = failTolerance;
		waiting = true;
		consecFail = 0;
		permissionLevel = -1;
	}

	public boolean isWaiting()
	{
		synchronized(waitingLock)
		{
			return waiting;
		}
	}
	
	private void sendString(String outString)
	{
		try {
			PrintWriter netOut = new PrintWriter(clientSocket.getOutputStream(), true);
			netOut.println(outString);
		} catch (IOException e) {
			System.err.println("Failed to send string to client.");
			e.printStackTrace();
		}
	}
	
	private String receiveString()
	{
		String messageString;
		
		while(consecFail <= failTolerance)
		try {
			BufferedReader inReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			messageString = inReader.readLine();
			consecFail = 0;
			return messageString;
		} catch (IOException e) {
			consecFail++;
		}
		return null;
	}
	
	private void sendObject(Object outObj)
	{
		while(consecFail <= failTolerance)
		{
			try {
				ObjectOutputStream sendStream = new ObjectOutputStream(clientSocket.getOutputStream());
				sendStream.writeObject(outObj);
			} catch (IOException e) {
				consecFail++;
			}
		}
	}
	
	private Object receiveObject()
	{
		Object receiveObject;
		try {
			ObjectInputStream receiveStream = new ObjectInputStream(clientSocket.getInputStream());
			try {
				receiveObject = receiveStream.readObject();
			} catch (ClassNotFoundException e) {
				System.err.println("Error reconstituting serialized object from stream.");
				e.printStackTrace();
				return null;
			}
			return receiveObject;
		} catch (IOException e) {
			consecFail++;
		}
		return null;
	}
	
	private void sendFile(File fp)
	{
		byte[] fileBytes = new byte[(int)fp.length()];
		OutputStream byteOutput;
		BufferedInputStream inputStream;
		
		try {
		inputStream = new BufferedInputStream(new FileInputStream(fp));
		} catch(FileNotFoundException e)
		{
			e.printStackTrace();
			return;
		}
		while(consecFail <= failTolerance)
		{
			try {
				
				inputStream.read(fileBytes, 0, fileBytes.length);
				byteOutput = clientSocket.getOutputStream();
				byteOutput.write(fileBytes, 0, fileBytes.length);
				byteOutput.flush();
				byteOutput.close();
				return;
			} catch (IOException e) {
				consecFail++;
			}
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	private File receiveFile() // writes file to disk and returns file location as File object.
	{
		
		String byteString = receiveString();
		int byteCount = Integer.parseInt(byteString);
		byte[] receivedBytes = new byte[byteCount];
		File newFile = new File(new SimpleDateFormat("files/yyyyMMddHHmm.pdf").format(new Date()));
		
		while(consecFail <= failTolerance)
		{
			try {
				FileOutputStream fos = new FileOutputStream(newFile);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				int bytesRead = clientSocket.getInputStream().read(receivedBytes, 0, receivedBytes.length);
				bos.write(receivedBytes,0 , bytesRead);
				bos.close();
				return newFile;
			} catch (IOException e) {
				consecFail++;
			}
		}
		
		return null;
	}
	
	private void parse(String input)
	{
		String[] substrings = input.split(" ");
		String opString;
		String arg1 = "";
		String arg2 = "";
		int opcode;
		opString = substrings[0];
		opcode = Integer.parseInt(opString);
		ArrayList<ArticleInfo> returnedArticles;
		ArticleInfo tempArticleInfo;
		File tempFile;
		
		if(substrings.length > 1)
		{
			arg1 = substrings[1];
		}
		if(substrings.length > 2)
		{
			arg2 = substrings[2];
		}
		
		//Try to keep networking sends here and remember to update consecFail as necessary.
		switch(opcode) { //TODO: Add functionality for opcodes here, sending execution off to proper subsystems/modules if necessary.
			case 0:
				consecFail = 0;
				break;
			case 1:
				permissionLevel = users.Public.login(arg1, arg2);
				if(permissionLevel == -1)
				{
					sendString("-1");
				}
				break;
			case 2:
				permissionLevel = -1;
				sendString("0"); //Confirm that logout was successful to client.
				break;
			case 3:
				if(users.Public.register(arg1, arg2))
				{
					sendString("0");
				} else
				{
					sendString("-1");
				}
				break;
			case 4:
				returnedArticles = database.Public.getArticlesFromCategory(Integer.valueOf(arg1), Integer.valueOf(arg2));
				sendObject(returnedArticles);
				break;
			case 5:
				tempFile = receiveFile();
				tempArticleInfo = (ArticleInfo)receiveObject();
				database.Public.insertArticle(tempFile, tempArticleInfo, permissionLevel);
				break;
			case 6:
				tempArticleInfo = database.Public.getArticleInfo(Integer.valueOf(arg1));
				sendObject(tempArticleInfo);
				break;
			case 7:
				tempFile = database.Public.downloadArticle(Integer.valueOf(arg1));
				sendFile(tempFile);
				break;
			case 8:
				sendString(Integer.toString(permissionLevel));
				break;
		}
		
	}
	
	public void run()
	{
		BufferedReader clientStream;
		String clientMessage;
		try {
			ServerSocket listener = new ServerSocket(port);
			clientSocket = listener.accept();
			clientSocket.setSoTimeout(2000);
			listener.close();
			synchronized(waitingLock)
			{
				this.waiting = false;
			}
		} catch (IOException e) {
			System.err.println("Error listening for clients.");
			e.printStackTrace();
			return;
		}
		
		try {
			clientStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Failed to create buffered reader for client input stream.");
			return;
		}
		
		while(!clientSocket.isClosed() && consecFail <= failTolerance)
		{
			try {
				clientMessage = clientStream.readLine();
				parse(clientMessage);
				consecFail = 0;
			} catch (IOException e) {
				System.out.println("Listen timeout, consecutive timeouts: " + (consecFail+1));
				consecFail++;
			}
		}
		
		try {
			clientSocket.close();
		} catch (IOException e) {
			System.err.println("Error closing client socket.");
			e.printStackTrace();
		}
	}
}
