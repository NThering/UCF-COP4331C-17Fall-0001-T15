/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;
import am_utils.ArticleInfo;
import java.io.File;
import java.util.ArrayList;
import am_utils.MainCategory;
import am_utils.SubCategory;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import org.mariadb.jdbc.Driver;

/**
 *
 * @author NThering
 * @author MAllen
 */
public class Public {
	static Connection DBConnection;
	
	
	// Returns null object if the creation of the connection fails.
	public static Connection getDBConnection()
	{
		Connection dbCon = null;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Failed to create database driver.");
			e.printStackTrace();
			return dbCon;
		}
    	
    	try {
			dbCon = DriverManager.getConnection("jdbc:mariadb://localhost/article_manager", "root", "cop4331");
		} catch (SQLException e) {
			System.err.println("Failed to connect to database.");
			e.printStackTrace();
			return dbCon;
		}
    	return dbCon;
	}
	
    /** Returns a list to the client of all articles in a given category/sub-category, this includes their ID number on the server. 
     * @throws ParseException */
    public static ArrayList<ArticleInfo> getArticlesFromCategory( MainCategory articleCategory, SubCategory subCategory )
    {	
    	Connection con = getDBConnection();
    	Statement queryStatement;
    	ResultSet rs;
    	ResultSet srs;
    	DateFormat dateConverter = DateFormat.getDateInstance();
    	ArrayList<ArticleInfo> articles = new ArrayList<ArticleInfo>();
    	ArticleInfo newArt = null;
		try {
			queryStatement = con.createStatement();
			rs = queryStatement.executeQuery("select * from article inner join article_subcat on article.id = article_subcat.id");
			
			
			while(rs.next())
			{
				if(rs.getString("mainCategory") == articleCategory.getName() && rs.getString("subcatName") == subCategory.printName())
				newArt = new ArticleInfo(rs.getInt("id"));
				newArt.doiNumber = rs.getString("doiNumber");
				newArt.printName = rs.getString("printName");
				newArt.mainCategory.setName(rs.getString("mainCategory"));
				newArt.mainCategory.setID(rs.getInt("mainID"));
				newArt.author = rs.getString("author");
				newArt.owner = rs.getString("owner");
				newArt.abstractText = rs.getString("abstractText");
				newArt.uploadTime = dateConverter.parse(rs.getString("uploadDate"));
				srs = queryStatement.executeQuery("select * from article_subcat where id=" + rs.getInt("id"));
				
				while(srs.next())
				{
					newArt.mainCategory.addNewSubcategory(srs.getString("subcatName"));
				}
				
				articles.add(newArt);
			}
			return articles;
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (ParseException e) {
			e.printStackTrace();
			System.exit(0);
		}
        return null;
    }
        
    /** Sends the article to the database for entry, returns 0 if successful and an int corresponding to the type of error if it was not.  If articleID is not 0 this upload is meant to replace an existing upload. 
     if ArticleID matches an articleID already in the database, replace it if the uploader is the owner of that article or is an admin.  Refuse to insert the new article if not.*/
    public static int insertArticle( File articleFile, ArticleInfo articleInfo, int userPermissions )
    {
        return 1;
    }
    
    /** Deletes the article under the specified ID, if the user has ownership of it or is an admin. */
    public static int deleteArticle( int articleID, String userUsername, int userPermissions )
    {
        return 1;
    }
        
    /** Gets information on the given article from the database. */
    public static ArticleInfo getArticleInfo( int articleID )
    {
        return null;
    }
        
    /** Returns a handle to the given article file. */
    public static File downloadArticle( int articleID )
    {
        return null;
    }
}
