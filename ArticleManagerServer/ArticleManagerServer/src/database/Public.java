/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;
import am_utils.ArticleInfo;

import java.beans.Statement;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.mariadb.jdbc.Driver;

/**
 *
 * @author NThering
 * @author MAllen
 */
public class Public {
	static Connection DBConnection;
	
	
	// Returns null object if the creation of the connection fails.
	private static Connection getDBConnection()
	{
		Connection dbCon = null;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Failed to create database driver.");
			e.printStackTrace();
			System.exit(-1);
		}
    	
    	try {
			dbCon = DriverManager.getConnection("jdbc:mariadb://localhost/article_manager", "root", "cop4331");
		} catch (SQLException e) {
			System.err.println("Failed to connect to database.");
			e.printStackTrace();
			System.exit(-1);
			return dbCon;
		}
    	return dbCon;
	}
	
	private static String dateToString(Date dateObject)
	{
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String dateString = df.format(dateObject);
		
		return dateString;
	}
	
	private static Date stringToDate(String dateString)
	{
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		try {
			Date returnDate = df.parse(dateString);
			return returnDate;
		} catch (ParseException e) {
			System.err.println("Error parsing date string.");
			e.printStackTrace();
			return null;
		}
	}
	
    /** Returns a list to the client of all articles in a given category/sub-category, this includes their ID number on the server. 
     * @throws ParseException */
    public static ArrayList<ArticleInfo> getArticlesFromCategory( int mainCategoryID, int subCategoryID )
    {	
    	Connection con = getDBConnection();
    	java.sql.Statement queryStatement;
    	ResultSet rs;
    	ArrayList<ArticleInfo> articles = new ArrayList<ArticleInfo>();
    	ArticleInfo newArt = null;
		try {
			queryStatement = con.createStatement();
			rs = queryStatement.executeQuery("select * from article where mainID=" + mainCategoryID + " and subID=" + subCategoryID);
			
			
			while(rs.next())
			{
				newArt = new ArticleInfo(rs.getInt("id"));
				newArt.doiNumber = rs.getString("doiNumber");
				newArt.printName = rs.getString("printName");
				newArt.mainCategoryID = rs.getInt("mainID");
				newArt.subCategoryID = rs.getInt("subID");
				newArt.author = rs.getString("author");
				newArt.owner = rs.getString("owner");
				newArt.abstractText = rs.getString("abstractText");
				newArt.uploadTime = stringToDate(rs.getString("uploadDate"));
				
				articles.add(newArt);
			}
			return articles;
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return null;
    }
    
    public static ArrayList<ArticleInfo> getArticlesFromUser(String username)
    {	
    	Connection con = getDBConnection();
    	java.sql.Statement queryStatement;
    	ResultSet rs;
    	ArrayList<ArticleInfo> articles = new ArrayList<ArticleInfo>();
    	ArticleInfo newArt = null;
		try {
			queryStatement = con.createStatement();
			rs = queryStatement.executeQuery("select * from article");
			
			
			while(rs.next())
			{
				newArt = new ArticleInfo(rs.getInt("id"));
				newArt.doiNumber = rs.getString("doiNumber");
				newArt.printName = rs.getString("printName");
				newArt.mainCategoryID = rs.getInt("mainID");
				newArt.subCategoryID = rs.getInt("subID");
				newArt.author = rs.getString("author");
				newArt.owner = rs.getString("owner");
				newArt.abstractText = rs.getString("abstractText");
				newArt.uploadTime = stringToDate(rs.getString("uploadDate"));
				
				articles.add(newArt);
			}
			return articles;
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return null;
    }
        
    /** Sends the article to the database for entry, returns 0 if successful and an int corresponding to the type of error if it was not.  If articleID is not 0 this upload is meant to replace an existing upload. 
     if ArticleID matches an articleID already in the database, replace it if the uploader is the owner of that article or is an admin.  Refuse to insert the new article if not.*/
    public static int insertArticle( File articleFile, ArticleInfo articleInfo, int userPermissions )
    {
    	Connection con = getDBConnection();
    	java.sql.Statement stmnt;
    	String finalString;
    	ResultSet rs;
    	File oldFile;
    	
		if(articleInfo.uploadTime == null)
		{
			articleInfo.uploadTime = new Date();
		}
    	
    	try {
    		stmnt = con.createStatement();
    		rs = stmnt.executeQuery("select owner, filePath from article where id = " + articleInfo.getArticleID());
    		if(rs.first())
    		{
    			oldFile = new File(rs.getString("filePath"));
    			oldFile.delete();
    			if((rs.getString("owner") == articleInfo.owner) || userPermissions == 1)
    			{
    				finalString = "update article set "
							+ "printName=" + "\""+articleInfo.printName+"\""
							+ ", mainID=" + articleInfo.mainCategoryID
							+ ", author = " + "\""+articleInfo.author+"\""
							+ ", owner = " + "\""+articleInfo.owner+"\""
							+ ", abstractText = " + "\""+articleInfo.abstractText+"\""
							+ ", uploadDate = " + "\""+dateToString(articleInfo.uploadTime)+"\""
							+ ", doiNumber = " + "\""+articleInfo.doiNumber+"\""
							+ ", subID = " + articleInfo.subCategoryID
							+ ", filePath=" + "\""+articleFile.getPath()+"\""
							+ " where id=" + articleInfo.getArticleID() +";";
    				stmnt.close();
    				stmnt = con.createStatement();
    	    		stmnt.executeUpdate(finalString);
    				return 0;
    			} else
    			{
    				return -1; // User does not have sufficient permissions to modify database entry.
    			}
    		}
    		
    		finalString = "insert into article values ("
    				+ "NULL" // Article ID will be automatically assigned by the database through increment.
    				+ ", " + "\""+articleInfo.printName+"\""
    				+ ", " + "\""+articleInfo.mainCategoryID+"\""
    				+ ", " + "\""+articleInfo.author+"\""
    				+ ", " + "\""+articleInfo.owner+"\""
    				+ ", " + "\""+articleInfo.abstractText+"\""
    				+ ", " + "\""+dateToString(articleInfo.uploadTime)+"\""
    				+ ", " + "\""+articleInfo.doiNumber+"\""
    				+ ", " + articleInfo.subCategoryID
    				+ ", " + "\""+articleFile.getPath()+"\"" + ");";
    		stmnt.close();
    		stmnt = con.createStatement();
    		stmnt.executeUpdate(finalString);
    		stmnt.close();
    		
    		return 0;
    		
    	} catch(SQLException e)
    	{
    		e.printStackTrace();
    	}
    	
    	return -1;
    }
    
    /** Deletes the article under the specified ID, if the user has ownership of it or is an admin. */
    public static int deleteArticle( int articleID, String userUsername, int userPermissions )
    {
    	Connection con = getDBConnection();
    	java.sql.Statement stmnt;
    	ResultSet rs;
    	File deleteFile;
    	
    	try
    	{
    		stmnt = con.createStatement();
    		stmnt.executeQuery("select id, owner, filePath from article where id=" + articleID + ";");
    		rs = stmnt.getResultSet();
    		if(rs.next())
    		{
    			if((rs.getString("owner") == userUsername) || (userPermissions == 1) )
    			{
    				deleteFile = new File(rs.getString("filePath"));
    				deleteFile.delete();
    				stmnt.close();
    				stmnt.executeUpdate("delete from article where id=" + articleID + ";");
    				return 0;
    			} else
    			{
    				return 1; // User lacks permissions to delete article.
    			}
    		} else
    		{
    			//There was no matching article, so nothing to be deleted. Return 0 anyways.
    			return 0;
    		}
    	} catch(SQLException e)
    	{
    		e.printStackTrace();
    		System.exit(-1);
    	}
        return 1;
    }
        
    /** Gets information on the given article from the database. */
    public static ArticleInfo getArticleInfo( int articleID )
    {
    	Connection con = getDBConnection();
    	java.sql.Statement stmnt;
    	ResultSet rs;
    	ArticleInfo returnedInfo = new ArticleInfo(articleID);
    	
    	try {
    		stmnt = con.createStatement();
    		stmnt.executeQuery("select * from article where id=" + articleID + ";");
    		rs = stmnt.getResultSet();
    		if(rs.next())
    		{
    			returnedInfo.doiNumber = rs.getString("doiNumber");
    			returnedInfo.printName = rs.getString("printName");
    			returnedInfo.mainCategoryID = rs.getInt("mainID");
    			returnedInfo.subCategoryID = rs.getInt("subID");
    			returnedInfo.author = rs.getString("author");
    			returnedInfo.owner = rs.getString("owner");
    			returnedInfo.abstractText = rs.getString("abstractText");
    			returnedInfo.uploadTime = stringToDate(rs.getString("uploadDate"));
    		} else
    		{
    			stmnt.close();
    			return null;
    		}
    		
    		stmnt.close();
    		return returnedInfo;
    	}catch(SQLException e)
    	{
    		e.printStackTrace();
    	}
        return null;
    }
        
    /** Returns a handle to the given article file. */
    public static File downloadArticle( int articleID )
    {
    	Connection con = getDBConnection();
    	java.sql.Statement stmnt;
    	ResultSet rs;
    	File returnedFile = null;
    	
    	try
    	{
    		stmnt = con.createStatement();
    		stmnt.executeQuery("select filePath from article where id=" + articleID + ";");
    		rs = stmnt.getResultSet();
    		if(rs.next())
    		{
    			returnedFile = new File(rs.getString("filePath"));
    		} else
    		{
    			return null; //Article does not exist in database.
    		}
    		stmnt.close();
    		return returnedFile;
    	}catch(SQLException e)
    	{
    		e.printStackTrace();
    	}
        return null;
    }
}
