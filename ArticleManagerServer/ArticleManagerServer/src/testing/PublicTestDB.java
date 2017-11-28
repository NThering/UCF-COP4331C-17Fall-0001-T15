package testing;

import am_utils.ArticleInfo;
import java.io.File;
import java.util.ArrayList;
import database.Public;

public class PublicTestDB {
	ArrayList<ArticleInfo> testArticles;
	ArrayList<File> testFiles;
	
	void publicTestDB()
	{
		testArticles = new ArrayList<ArticleInfo>();
		testFiles = new ArrayList<File>();
		ArticleInfo insertArticle;
		File insertFile;
		
		insertFile = new File("articles/Solar Panel.pdf");
		testFiles.add(insertFile);
		insertFile = new File("articles/Nanomaterials.pdf");
		testFiles.add(insertFile);
		insertFile = new File("articles/Biosphere Research.pdf");
		testFiles.add(insertFile);
		insertFile = new File("articles/Automobile Efficiency.pdf");
		testFiles.add(insertFile);
		insertFile = new File("articles/Computer Science.pdf");
		testFiles.add(insertFile);
		
		insertArticle = new ArticleInfo(0);
		insertArticle.doiNumber = "021302/00002";
		insertArticle.printName = "Solar Panel Efficiency";
		insertArticle.mainCategoryID = 0;
		insertArticle.subCategoryID = 1;
		insertArticle.author = "Travis Bickle";
		insertArticle.owner = "User1";
		insertArticle.abstractText = "Something about solar panels.";
		insertArticle.uploadTime = null;
		testArticles.add(insertArticle);
		
		insertArticle = new ArticleInfo(1);
		insertArticle.doiNumber = "232425/12387";
		insertArticle.printName = "Advances in nanomaterials research";
		insertArticle.mainCategoryID = 0;
		insertArticle.subCategoryID = 1;
		insertArticle.author = "Human Personson";
		insertArticle.owner = "User1";
		insertArticle.abstractText = "Something about nanomaterials.";
		insertArticle.uploadTime = null;
		testArticles.add(insertArticle);
		
		insertArticle = new ArticleInfo(2);
		insertArticle.doiNumber = "3483/00012";
		insertArticle.printName = "Sustaining biospheres";
		insertArticle.mainCategoryID = 2;
		insertArticle.subCategoryID = 3;
		insertArticle.author = "Another Person";
		insertArticle.owner = "Admin";
		insertArticle.abstractText = "Something about natural science.";
		insertArticle.uploadTime = null;
		testArticles.add(insertArticle);
		
		insertArticle = new ArticleInfo(3);
		insertArticle.doiNumber = "21398192/12321";
		insertArticle.printName = "Enhancing automobile efficiency";
		insertArticle.mainCategoryID = 2;
		insertArticle.subCategoryID = 3;
		insertArticle.author = "Human Personson";
		insertArticle.owner = "User2";
		insertArticle.abstractText = "Something about cars.";
		insertArticle.uploadTime = null;
		testArticles.add(insertArticle);
		
		insertArticle = new ArticleInfo(4);
		insertArticle.doiNumber = "2130278/21312";
		insertArticle.printName = "CS Algorithms";
		insertArticle.mainCategoryID = 4;
		insertArticle.subCategoryID = 5;
		insertArticle.author = "Missy Pearson";
		insertArticle.owner = "User2";
		insertArticle.abstractText = "Something about computer science.";
		insertArticle.uploadTime = null;
		testArticles.add(insertArticle);
		
	}
	
	public void runTest()
	{
		int errcode;
		errcode = testInsertArticle();
		if(errcode == 0)
		{
			System.out.println("Article insertion test passed.");
		} else
		{
			System.out.println("Article insertion test failed with error code " + errcode);
			return;
		}
		
		errcode = testGetArticleInfo();
		if(errcode == 0)
		{
			System.out.println("Article info acquisition test passed.");
		} else
		{
			System.out.println("Article info acquisition test failed with error code " + errcode);
			return;
		}
		
		errcode = testGetArticlesFromCategory();
		if(errcode == 0)
		{
			System.out.println("Article acquisition by category test passed.");
		} else
		{
			System.out.println("Article acquisition by category test failed with error code " + errcode);
			return;
		}
		
		errcode = testDeleteArticle();
		if(errcode == 0)
		{
			System.out.println("Article deletion test passed.");
		} else
		{
			System.out.println("Article deletion test failed with error code " + errcode);
			return;
		}
	}
	
	public int testInsertArticle()
	{
		for(int i = 0; i < testArticles.size(); i++)
		{
			database.Public.insertArticle(testFiles.get(i), testArticles.get(i), 1);
		}
		
		return 0;
	}
	
	public int testGetArticleInfo()
	{
		ArticleInfo retrievedArticleInfo;
		for(int i = 0; i < testArticles.size(); i++)
		{
			retrievedArticleInfo = database.Public.getArticleInfo(testArticles.get(i).getArticleID());
			if(retrievedArticleInfo.printName != testArticles.get(i).printName)
			{
				return -1; // Error, the incorrect article has been retrieved.
			}
		}
		
		return 0;
	}
	
	public int testDeleteArticle()
	{
		for(int i = 0; i < testArticles.size(); i++)
		{
			if(database.Public.deleteArticle(testArticles.get(i).getArticleID(), "Admin", 1) != 0)
			{
				return -1; //We should have permissions to delete any article we wish.
			}
		}
		
		for(int i = 0; i < testArticles.size(); i++)
		{
			if(database.Public.getArticleInfo(testArticles.get(i).getArticleID()) != null)
			{
				return -1; // An article that should have been deleted still exists in the database.
			}
		}
		
		return 0;
	}
	
	public int testGetArticlesFromCategory()
	{
		ArrayList<ArticleInfo> returnedArticles;
		
		returnedArticles = database.Public.getArticlesFromCategory(0, 1);
		if(returnedArticles.size() != 2)
		{
			return -1; // Signifies incorrect number of articles returned in ArrayList.
		}
		
		returnedArticles = database.Public.getArticlesFromCategory(2, 3);
		if(returnedArticles.size() != 2)
		{
			return -1;
		}
		
		returnedArticles = database.Public.getArticlesFromCategory(4, 5);
		if(returnedArticles.size() != 1)
		{
			return -1;
		}
		
		return 0; 
	}
	
}
