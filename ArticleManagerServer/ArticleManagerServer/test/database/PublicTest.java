/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import am_utils.ArticleInfo;
import java.io.File;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author NThering
 */
public class PublicTest {
    
    public PublicTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getArticlesFromCategory method, of class Public.
     */
    @Test
    public void testGetArticlesFromCategory() 
    {
        System.out.println("getArticlesFromCategory");
        
        for (int i = 0; i < 10; i++)
        {
            ArticleInfo testInfo = new ArticleInfo(0);
            
            testInfo.mainCategoryID = i;
            testInfo.subCategoryID = 2;
            
            File dummyFile = new File("Fake", "EEEEEEE");
            
            assertEquals(Public.insertArticle( dummyFile, testInfo, 0 ), 0);
        }
        
        for (int i = 0; i < 15; i++)
        {
            if ( i < 10 )
                assertEquals(Public.getArticlesFromCategory(i, 2).size(), 1);
            else
                assertEquals(Public.getArticlesFromCategory(i, 2).size(), 0);
        }
    }

    /**
     * Test of insertArticle method, of class Public.
     */
    @Test
    public void testInsertArticle() 
    {
        System.out.println("insertArticle");
        
        ArticleInfo testInfo = new ArticleInfo(12);
        assertEquals(Public.insertArticle( null, testInfo, 0 ), 0);
    }

    /**
     * Test of deleteArticle method, of class Public.
     */
    @Test
    public void testDeleteArticle() 
    {
        System.out.println("deleteArticle");
        
        ArticleInfo testInfo = new ArticleInfo(12);
        assertEquals(Public.insertArticle( null, testInfo, 0 ), 0);
        
        assertEquals(Public.deleteArticle(12, "Mr.E", 0), 0);
    }

    /**
     * Test of getArticleInfo method, of class Public.
     */
    @Test
    public void testGetArticleInfo() 
    {
        System.out.println("getArticleInfo");
        int articleID = 0;
        ArticleInfo expResult = null;
        ArticleInfo result = Public.getArticleInfo(articleID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of downloadArticle method, of class Public.
     */
    @Test
    public void testDownloadArticle() 
    {
        System.out.println("downloadArticle");
        File result = Public.downloadArticle(0); // ID 0 should never actually be assigned.
        assertNull(result);
        
        ArticleInfo testInfo = new ArticleInfo(12);
        
        assertEquals(Public.insertArticle( null, testInfo, 0 ), 0);
        
        result = Public.downloadArticle(12);
        assertNull(result);
    }
}
