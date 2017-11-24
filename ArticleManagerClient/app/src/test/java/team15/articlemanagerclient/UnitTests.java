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

import proccessing.PublicUsage;

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
    public void testCategorySortMetrics() throws Exception
    {
        // Prints out category sort results and statistics for manual review.
        int failFieldCount = 0;
        int totalFieldCount = 0;

        // HARDCODED TEST DIRECTORY, BE SURE TO UPDATE THIS!!!
        File testDirectory = new File("T:\\Projects\\OOP\\ProjectCode\\UCF-COP4331C-17Fall-0001-T15\\ArticleManagerClient\\app\\src\\test\\java\\team15\\articlemanagerclient\\Article Manager Papers");

        // If the papers directory doesn't exist we've already failed.
        if ( !testDirectory.exists() )
            assert(false);

        File[] files = testDirectory.listFiles();
        
        ArrayList<ArticleInfo> completeArticles = new ArrayList<ArticleInfo>();

        for (int i = 0; i < files.length; i++)
        {
            // Don't care about directories even though we shouldn't actually have any.
            if (files[i].isDirectory())
                continue;

            CUtils.msg("Parsing " + files[i].getName() + "!");

            ArticleInfo info = proccessing.PublicUsage.categorize( files[i], TestUtils.GetMainCategoryArray(), TestUtils.GetMainCategoryArraySize() );

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
                if (fieldValues[g] == null)
                {
                    hasAllFields = false;
                    failFieldCount++;
                }

                CUtils.msg("\t" + fieldNames[g] + " " + (fieldValues[g] != null ? fieldValues[g] : "[NO VALUE]"));

                totalFieldCount++;
            }
            
            if (hasAllFields)
            {
                CUtils.msg("-----------------------------------------------------------");
                CUtils.msg("THE ABOVE ARTICLE HAS NO NULL FIELDS!!!!");
                CUtils.msg("-----------------------------------------------------------");
                
                completeArticles.add(info);
            }
        }
        
        
        CUtils.msg("Complete Articles:");
        for ( CatContainer passArtInfo : completeArticles )
               CUtils.msg("\t" + passArtInfo.printName);

        CUtils.msg("Total Complete Articles is " + String.valueOf(completeArticles.size()) );
        
        // TC201, [NO VALUE] entries must be less than 25% of the metadata entries.
        assertFalse("ONLY" + String.valueOf(1 - ((float)failFieldCount)/totalFieldCount) + "METADATA ENTRIES FILLED.  Need 0.75 or more.", ((float)failFieldCount)/totalFieldCount > 0.25);
    }
}
