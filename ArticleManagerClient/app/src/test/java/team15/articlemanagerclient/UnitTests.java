package team15.articlemanagerclient;

import android.view.ViewDebug;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import am_utils.ArticleInfo;
import am_utils.CUtils;
import am_utils.DefaultCategories;
import am_utils.MainCategory;
import am_utils.SubCategory;

import proccessing.FileConverter;
import proccessing.PublicUsage;

import networking.Public;
import list_builder.Public.*;

import static org.junit.Assert.*;

/**
 * Local unit test, which will execute on the development machine.
 *
 * Author: Noah Thering
 */

// Easy way to store category ID codes.
class CatIDs
{
    public int main;
    public int sub;

    CatIDs(int main, int sub)
    {
        this.main = main;
        this.sub = sub;
    }
}

// Format for categories that's easier to enumerate through.
class CatContainer
{
    public String name;
    public Integer id;
    public ArrayList<SubCatContainer> subcategories;
}

// Format for sub-categories that's easier to enumerate through.
// Only really need this so we can have ID without using null entries.
class SubCatContainer
{
    public String name;
    public Integer id;
}

class TestUtils
{
    static DefaultCategories defaultCat = new DefaultCategories();
    static ArrayList<CatContainer> mainCatList = convertToArrayLists( defaultCat.getDefaultCategories(), defaultCat.size());

    public static ArrayList<CatContainer> GetMainCategoryArrayList() { return mainCatList; }
    public static MainCategory[] GetMainCategoryArray() { return defaultCat.getDefaultCategories(); }
    public static int GetMainCategoryArraySize() { return defaultCat.size(); }

    // By having access to ArrayLists and the original implementation, we allow quick access to the original
    // implementation when needed, and easy enumeration with the ArrayLists.
    static ArrayList<CatContainer> convertToArrayLists( MainCategory[] categoryList, int categoryListSize )
    {
        ArrayList<CatContainer> finalCategoryList = new ArrayList<CatContainer>();

        // This is the safe way to enumerate over Kelsey's category listings.
        for( int i = 0; i < categoryListSize; i++ )
        {
            // The index of the category is actually its ID, so null entries are categories
            // that have been removed for one reason or another.  We need to account for this.
            if ( categoryList[i] == null )
                continue;

            CatContainer newCat = new CatContainer();
            newCat.name = categoryList[i].getName();
            newCat.subcategories = new ArrayList<SubCatContainer>();
            newCat.id = i;
            finalCategoryList.add(newCat);

            for( int g = 0; g < categoryList[i].size(); g++ )
            {
                // Same story as before.
                if ( categoryList[i].children()[g] == null )
                    continue;

                SubCatContainer newSubCat = new SubCatContainer();
                newSubCat.name = categoryList[i].children()[g].printName();
                newSubCat.id = g;

                newCat.subcategories.add(newSubCat);
            }
        }

        return finalCategoryList;
    }

    /** Get a category's IDs from its name.*/
    public static CatIDs CategoryIDsFromNames( String mainName, String subName )
    {
        for ( CatContainer category : mainCatList )
        {
            if (category.name != mainName)
                continue;

            for ( SubCatContainer subcategory : category.subcategories )
            {
                if (subcategory.name == subName)
                    return new CatIDs(category.id, subcategory.id);
            }
        }

        return null;
    }

    /** Get a category's IDs from its name.*/
    public static String MainCategoryNameFromId( int id )
    {
        // This isn't foolproof, as it does not check for exceeding the upper bounds
        // but it is good enough for our purposes.
        if (id < 0)
            return null;

        return defaultCat.getDefaultCategories()[id].printName();
    }

    /** Get a subcategory's IDs from its name.*/
    public static String SubCategoryNameFromIds( int mainID, int subID )
    {
        // This isn't foolproof, as it does not check for exceeding the upper bounds
        // but it is good enough for our purposes.
        if (mainID < 0 || subID < 0)
            return null;

        return defaultCat.getDefaultCategories()[mainID].children()[subID].printName();
    }
}

public class UnitTests
{
    // Test Tests, These do not map to RTM metrics.
    @Test
    public void testCategoryList() throws Exception
    {
        CUtils.printOutCategories();
    }

    @Test
    public void testCategoryIDFinder() throws Exception
    {
        CatIDs dbIDs = TestUtils.CategoryIDsFromNames("Computer Science", "Databases");

        assertNotNull(dbIDs);
        assertEquals(dbIDs.main, 10);
        assertEquals(dbIDs.sub, 12);
    }

    @Test
    public void testCategoryNameFinder() throws Exception
    {
        String mainCatName = TestUtils.MainCategoryNameFromId(10);
        String subCatName = TestUtils.SubCategoryNameFromIds(10, 12);

        assertNotNull(mainCatName);
        assertNotNull(subCatName);
        assertEquals(mainCatName, "Computer Science");
        assertEquals(subCatName, "Databases");
    }


    @Test
    public void testDict() throws Exception
    {
        FileConverter testConverter = new proccessing.FileConverter(null);

        assertTrue(testConverter.isWord("my"));
        assertTrue(testConverter.isWord("Testing"));
        assertFalse(testConverter.isWord("strats"));
        assertTrue(testConverter.isWord("are"));
        assertTrue(testConverter.isWord("Very"));
        assertTrue(testConverter.isWord("good"));

        assertFalse(testConverter.isWord("Johnathan"));
        assertFalse(testConverter.isWord("Ashley"));
        assertTrue(testConverter.isWord("horrible"));
        assertTrue(testConverter.isWord("environment"));
        assertTrue(testConverter.isWord("salvation"));

        assertTrue(testConverter.isWord("I"));
        assertTrue(testConverter.isWord("actually"));
        assertTrue(testConverter.isWord("had"));
        assertTrue(testConverter.isWord("a"));
        assertTrue(testConverter.isWord("lot"));
        assertTrue(testConverter.isWord("of"));
        assertTrue(testConverter.isWord("trouble"));
        assertTrue(testConverter.isWord("getting"));
        assertTrue(testConverter.isWord("a"));
        assertTrue(testConverter.isWord("good"));
        assertTrue(testConverter.isWord("dictionary"));

        assertTrue(testConverter.isWord("this"));
        assertTrue(testConverter.isWord("one"));
        assertTrue(testConverter.isWord("and"));
        assertTrue(testConverter.isWord("many"));
        assertTrue(testConverter.isWord("others"));
        assertTrue(testConverter.isWord("have"));
        assertTrue(testConverter.isWord("names"));
        assertTrue(testConverter.isWord("in"));
        assertTrue(testConverter.isWord("them"));
        assertTrue(testConverter.isWord("for"));
        assertTrue(testConverter.isWord("some"));
        assertTrue(testConverter.isWord("reason"));

        assertTrue(testConverter.isWord("also"));
        assertTrue(testConverter.isWord("random"));
        assertTrue(testConverter.isWord("words"));
        assertTrue(testConverter.isWord("are"));
        assertTrue(testConverter.isWord("capitalized"));
        assertTrue(testConverter.isWord("so"));
        assertTrue(testConverter.isWord("what"));
        assertTrue(testConverter.isWord("the"));
        assertTrue(testConverter.isWord("heck"));
    }


    //----------------
    //-----TC20X------
    //----------------

    /*
    * As testing this system depends heavily on manual review, this test exists mainly to facilitate it.
    * Test Case pass metrics are as follows:
    * TC201 - [NO VALUE] appears in less than 1/4 of metadata entries.
    * TC202 - All articles are assigned categories.
    * TC203 - 80% of articles are assigned to a category that is relevant to their content.  This requires manual evaluation.
    */

    @Test
    public void testFileConversionFromPDF() throws Exception
    {
        FileConverter fileConverter = new FileConverter(null);
        String testFilePath = "T:\\Projects\\OOP\\ProjectCode\\UCF-COP4331C-17Fall-0001-T15\\ArticleManagerClient\\app\\src\\test\\java\\team15\\articlemanagerclient\\Article Manager Papers\\Optimised Round Robin CPU Scheduling Algorithm.pdf";

        fileConverter.convertFromPDF( new File(testFilePath), 1);
        fileConverter.convertFromPDF( new File(testFilePath), 2);
        fileConverter.convertFromPDF( new File(testFilePath), 3);
    }

    @Test
    public void testFileConversionToPDF() throws Exception
    {
        FileConverter fileConverter = new FileConverter(null);
        String testFilePath = "T:\\Projects\\OOP\\ProjectCode\\UCF-COP4331C-17Fall-0001-T15\\ArticleManagerClient\\app\\src\\test\\java\\team15\\articlemanagerclient\\Article Manager Papers\\Readme\\TestText.txt";

        fileConverter.convertToPDF( new File(testFilePath));
    }

    @Test
    public void testCategorySortMetrics() throws Exception
    {
        // Prints out category sort results and statistics for manual review.
        int failFieldCount = 0;
        int totalFieldCount = 0;

        // HARDCODED TEST DIRECTORY, BE SURE TO POINT THIS AT WHEREVER ARTICLE_MANAGER_PAPERS IS ON YOUR COMPUTER!
        File testDirectory = new File("Article Manager Papers");

        // If the papers directory doesn't exist we've already failed.
        if ( !testDirectory.exists() )
            assertFalse("DID NOT FIND TEST DIRECTORY, CANNOT RUN TESTS", true);

        File[] files = testDirectory.listFiles();
        
        ArrayList<ArticleInfo> completeArticles = new ArrayList<ArticleInfo>();

        for (int i = 0; i < files.length; i++)
        {
            // Don't care about directories even though we shouldn't actually have any.
            if (files[i].isDirectory())
                continue;

            CUtils.msg("Parsing " + files[i].getName() + "!");

            ArticleInfo info = proccessing.PublicUsage.categorize( files[i], TestUtils.GetMainCategoryArray(), TestUtils.GetMainCategoryArraySize(), null );

            // Article ID MUST be 0 on fresh upload.
            assertNotNull(info);
            assertEquals(info.getArticleID(), 0);

            String[] fieldNames = {"Name", "Authors",
                                   "DOI Number", "Main Category ID",
                                   "Sub Category ID", "Main Category Name",
                                   "Sub Category Name", "Abstract Text"};

            String[] fieldValues = { info.printName, info.author,
                                     info.doiNumber, String.valueOf(info.mainCategoryID),
                                     String.valueOf(info.subCategoryID), TestUtils.MainCategoryNameFromId(info.mainCategoryID),
                                     TestUtils.SubCategoryNameFromIds(info.mainCategoryID, info.subCategoryID), info.abstractText};

            // TC202 - All articles must be assigned a category.
            assertNotEquals("MAIN CATEGORY NOT ASSIGNED TO ARTICLE" + files[i].getName(), info.mainCategoryID, -1);
            assertNotEquals("SUB CATEGORY NOT ASSIGNED TO ARTICLE" + files[i].getName(),info.subCategoryID, -1);
            
            boolean hasAllFields = true;

            for ( int g = 0; g < fieldNames.length; g++ )
            {
                if (fieldValues[g] == null && g != 2)
                {
                    hasAllFields = false;
                    failFieldCount++;
                }

                CUtils.msg("\t" + fieldNames[g] + " " + (fieldValues[g] != null ? fieldValues[g] : "[NO VALUE]"));

                totalFieldCount++;
            }
            
            if (hasAllFields)
                completeArticles.add(info);

            CUtils.msg("-----------------------------------------------------------");
        }
        
        
        CUtils.msg("Complete Articles:");
        for ( ArticleInfo passArtInfo : completeArticles )
               CUtils.msg("\t" + passArtInfo.printName);

        CUtils.msg("Total Complete Articles is " + String.valueOf( completeArticles.size() ));
        
        // TC201, [NO VALUE] entries must be less than 25% of the metadata entries.
        assertFalse("ONLY" + String.valueOf(1 - ((float)failFieldCount)/totalFieldCount) + "METADATA ENTRIES FILLED.  Need 0.75 or more.", ((float)failFieldCount)/totalFieldCount > 0.25);
    }

    @Test
    public void testNetverking() throws Exception
    {
        ArticleInfo testInfo = networking.Public.getArticleInfo(0);

        if (testInfo == null)
            return;


        CUtils.msg( String.valueOf(testInfo.subCategoryID) );
        CUtils.msg( String.valueOf(testInfo.mainCategoryID) );
        CUtils.msg( String.valueOf(testInfo.author) );
        CUtils.msg( String.valueOf(testInfo.printName) );
        CUtils.msg( String.valueOf(testInfo.doiNumber) );
        CUtils.msg( String.valueOf(testInfo.abstractText) );

        ArticleInfo testInfo2 = networking.Public.getArticleInfo(25);

        CUtils.msg( String.valueOf(testInfo2.subCategoryID) );
        CUtils.msg( String.valueOf(testInfo2.mainCategoryID) );
        CUtils.msg( String.valueOf(testInfo2.author) );
        CUtils.msg( String.valueOf(testInfo2.printName) );
        CUtils.msg( String.valueOf(testInfo2.doiNumber) );
        CUtils.msg( String.valueOf(testInfo2.abstractText) );

        CUtils.msg( String.valueOf(networking.Public.register("2", "NotAPassword1")) );
        CUtils.msg( String.valueOf(networking.Public.register("3", "NotAPassword2")) );
        CUtils.msg( String.valueOf(networking.Public.register("399", "NotAPassword398")) );
    }

    @Test
    public void testSimpleUpload() throws Exception
    {
        String testFilePath = "T:\\Projects\\OOP\\ProjectCode\\UCF-COP4331C-17Fall-0001-T15\\ArticleManagerClient\\app\\src\\test\\java\\team15\\articlemanagerclient\\Article Manager Papers\\Optimised Round Robin CPU Scheduling Algorithm.pdf";
        String testFileDir = "F:\\Desktop";

        ArticleInfo testInfo = new ArticleInfo(0);

        testInfo.printName = "Test Article!";
        testInfo.author = "Noah";
        testInfo.doiNumber = "EEEEEEEEE";
        testInfo.mainCategoryID = 10;
        testInfo.subCategoryID = 12;
        testInfo.abstractText = "This is a very abstract article let me tell you.";

        assertEquals(networking.Public.uploadArticle(new File(testFilePath), testInfo), 0);

        //File downloadedFile = networking.Public.downloadArticle(0, testFileDir);
        //CUtils.msg( "Downloaded file to " + downloadedFile.getAbsolutePath());
    }

    @Test
    public void testNetworkingUploadGetCategoryList() throws Exception
    {
        String testFilePath = "T:\\Projects\\OOP\\ProjectCode\\UCF-COP4331C-17Fall-0001-T15\\ArticleManagerClient\\app\\src\\test\\java\\team15\\articlemanagerclient\\Article Manager Papers\\Optimised Round Robin CPU Scheduling Algorithm.pdf";

        ArticleInfo testInfo = new ArticleInfo(0);

        testInfo.printName = "Test Article!";
        testInfo.author = "Noah";
        testInfo.doiNumber = "EEEEEEEEE";
        testInfo.mainCategoryID = 10;
        testInfo.subCategoryID = 12;
        testInfo.abstractText = "This is a very abstract article let me tell you.";

        assertEquals(networking.Public.uploadArticle(new File(testFilePath), testInfo), 0);

        ArrayList<ArticleInfo> categoryInfos = networking.Public.getArticlesFromCategory(10, 12, true);

        assertNotEquals("DID NOT EXTRACT ANY ARTICLE INFOS FOR CATEGORY WHEN THERE SHOULD BE AT LEAST ONE!", categoryInfos.size(), 0);

        ArticleInfo desiredInfo = null;

        for ( ArticleInfo info : categoryInfos )
        {
            if ( info.printName.compareTo(testInfo.printName) == 0 )
            {
                desiredInfo = info;
                break;
            }
        }

        assertNotNull( "DID NOT FIND UPLOADED ARTICLE IN RETURNED CATEGORY LISTING", desiredInfo);

        assertTrue("DOWNLOADED INFO DOES NOT MATCH UPLOADED INFO!", compareArticleInfos(testInfo, desiredInfo, "Original Info is:", "Downloaded Info is:"));
    }

    @Test
    public void testNetworkingNotGivingAbstractWhenNotAsked() throws Exception
    {
        String testFilePath = "T:\\Projects\\OOP\\ProjectCode\\UCF-COP4331C-17Fall-0001-T15\\ArticleManagerClient\\app\\src\\test\\java\\team15\\articlemanagerclient\\Article Manager Papers\\Optimised Round Robin CPU Scheduling Algorithm.pdf";

        ArticleInfo testInfo = new ArticleInfo(0);

        testInfo.printName = "Test Article!";
        testInfo.author = "Noah";
        testInfo.doiNumber = "EEEEEEEEE";
        testInfo.mainCategoryID = 10;
        testInfo.subCategoryID = 12;
        testInfo.abstractText = "This is a very abstract article let me tell you.";

        assertEquals(networking.Public.uploadArticle(new File(testFilePath), testInfo), 0);

        ArrayList<ArticleInfo> categoryInfos = networking.Public.getArticlesFromCategory(10, 12, false);

        assertNotEquals("DID NOT EXTRACT ANY ARTICLE INFOS FOR CATEGORY WHEN THERE SHOULD BE AT LEAST ONE!", categoryInfos.size(), 0);

        ArticleInfo desiredInfo = null;

        for ( ArticleInfo info : categoryInfos )
        {
            if ( info.printName.compareTo(testInfo.printName) == 0 )
            {
                desiredInfo = info;
                break;
            }
        }

        assertNotNull( "DID NOT FIND UPLOADED ARTICLE IN RETURNED CATEGORY LISTING", desiredInfo);

        assertNull( "DOWNLOADED INFO STILL HAS ABSTRACT EVEN THOUGH NOT REQUESTED!", desiredInfo.abstractText );
    }

    @Test
    public void testNetworkingUploadDownload() throws Exception
    {
        String testFilePath = "T:\\Projects\\OOP\\ProjectCode\\UCF-COP4331C-17Fall-0001-T15\\ArticleManagerClient\\app\\src\\test\\java\\team15\\articlemanagerclient\\Article Manager Papers\\Optimised Round Robin CPU Scheduling Algorithm.pdf";
        String testFileDir = "F:\\Desktop";

        ArticleInfo testInfo = new ArticleInfo(0);

        testInfo.printName = "Test Article!";
        testInfo.author = "Noah";
        testInfo.doiNumber = "EEEEEEEEE";
        testInfo.mainCategoryID = 10;
        testInfo.subCategoryID = 12;
        testInfo.abstractText = "This is a very abstract article let me tell you.";

        assertEquals(networking.Public.uploadArticle(new File(testFilePath), testInfo), 0);

        ArrayList<ArticleInfo> categoryInfos = networking.Public.getArticlesFromCategory(10, 12, false);

        assertNotEquals("DID NOT EXTRACT ANY ARTICLE INFOS FOR CATEGORY WHEN THERE SHOULD BE AT LEAST ONE!", categoryInfos.size(), 0);

        ArticleInfo desiredInfo = null;

        for ( ArticleInfo info : categoryInfos )
        {
            if ( info.printName.compareTo(testInfo.printName) == 0 )
            {
                desiredInfo = info;
                break;
            }
        }

        File downloadedFile = networking.Public.downloadArticle(desiredInfo.getArticleID(), testFileDir);

        CUtils.msg( "Downloaded file to " + downloadedFile.getAbsolutePath());
    }

    boolean compareArticleInfos( ArticleInfo info1, ArticleInfo info2, String msg1, String msg2 )
    {
        String[] fieldNames = {"Name", "Authors",
                "DOI Number", "Main Category ID",
                "Sub Category ID", "Main Category Name",
                "Sub Category Name", "Abstract Text"};

        String[] fieldValues1 = { info1.printName, info1.author,
                info1.doiNumber, String.valueOf(info1.mainCategoryID),
                String.valueOf(info1.subCategoryID), TestUtils.MainCategoryNameFromId(info1.mainCategoryID),
                TestUtils.SubCategoryNameFromIds(info1.mainCategoryID, info1.subCategoryID), info1.abstractText};

        String[] fieldValues2 = { info2.printName, info2.author,
                info2.doiNumber, String.valueOf(info2.mainCategoryID),
                String.valueOf(info2.subCategoryID), TestUtils.MainCategoryNameFromId(info2.mainCategoryID),
                TestUtils.SubCategoryNameFromIds(info2.mainCategoryID, info2.subCategoryID), info2.abstractText};

        if (msg1 != null)
        {
            CUtils.msg(msg1);
            for (int g = 0; g < fieldNames.length; g++)
                CUtils.msg("\t" + fieldNames[g] + " " + (fieldValues1[g] != null ? fieldValues1[g] : "[NO VALUE]"));
        }

        if (msg2 != null)
        {
            CUtils.msg(msg2);
            for (int g = 0; g < fieldNames.length; g++)
                CUtils.msg("\t" + fieldNames[g] + " " + (fieldValues2[g] != null ? fieldValues2[g] : "[NO VALUE]"));
        }

        for ( int g = 0; g < fieldNames.length; g++ )
        {
            if (fieldValues1[g].compareTo(fieldValues2[g]) != 0)
                return false;
        }

        return true;
    }

    @Test
    public void testNetworkingRegister() throws Exception
    {
        networking.Public.register( "ThisIsAUsername", "ThisIsAPassword" );

        assertNotEquals("SUCCESSFULLY REGISTERED SAME USER A SECOND TIME!",networking.Public.register( "ThisIsAUsername", "ThisIsAPassword" ), 0);
    }

    @Test
    public void testNetworkingRegisterLogin() throws Exception
    {
        networking.Public.register("ThisIsAUsername", "ThisIsAPassword");

        assertNotEquals("SUCCESSFULLY REGISTERED SAME USER A SECOND TIME!", networking.Public.register("ThisIsAUsername", "ThisIsAPassword"), 0);

        assertNotEquals("LOGGED IN AS NON-EXISTANT USER!", networking.Public.login( "ThisIsNOTAUsername", "ThisIsAPassword" ), 0);
        assertNotEquals("LOGGED IN WITH WRONG PASSWORD!", networking.Public.login( "ThisIsAUsername", "ThisIsNotAPassword" ), 0);

        assertEquals("FAILED TO LOG IN WITH CORRECT USERNAME AND PASSWORD!", networking.Public.login( "ThisIsAUsername", "ThisIsAPassword" ), 0);
    }

    @Test
    public void testNetworkingRegisterLoginPermissions() throws Exception
    {
        // -1 = user who is not logged in.
        assertEquals("WRONG PERMISSIONS BEFORE LOGGING IN!", networking.Public.getPermissions(), -1);

        networking.Public.register("ThisIsAUsername", "ThisIsAPassword");

        assertNotEquals("SUCCESSFULLY REGISTERED SAME USER A SECOND TIME!", networking.Public.register("ThisIsAUsername", "ThisIsAPassword"), 0);

        assertNotEquals("LOGGED IN AS NON-EXISTANT USER!", networking.Public.login( "ThisIsNOTAUsername", "ThisIsAPassword" ), 0);
        assertNotEquals("LOGGED IN WITH WRONG PASSWORD!", networking.Public.login( "ThisIsAUsername", "ThisIsNotAPassword" ), 0);

        assertEquals("FAILED TO LOG IN WITH CORRECT USERNAME AND PASSWORD!", networking.Public.login( "ThisIsAUsername", "ThisIsAPassword" ), 0);

        // 0 = logged in user who is not an admin.
        assertEquals("WRONG PERMISSIONS AFTER LOGGING IN!", networking.Public.getPermissions(), 0);
    }

    @Test
    public void testListBuilder() throws Exception
    {
        String testFilePath = "T:\\Projects\\OOP\\ProjectCode\\UCF-COP4331C-17Fall-0001-T15\\ArticleManagerClient\\app\\src\\test\\java\\team15\\articlemanagerclient\\Article Manager Papers";

        list_builder.Public.BuildDatabaseOverview(testFilePath);

        list_builder.Public.BuildDetailedCategoryListing(testFilePath, 10);
    }

    @Test
    public void testGetUserArticles() throws Exception
    {
        ArrayList<ArticleInfo> allMyInfo = networking.Public.getAllArticlesOfCurrentUser();

        for ( ArticleInfo info : allMyInfo )
            compareArticleInfos( info, info, "Info:", null );
    }
}
