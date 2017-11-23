package list_builder;

import java.io.File;

/**
 * Created by NThering on 11/22/2017.
 */

public class Public
{
    /*
     * Returns text file that contains a list of the categories and the names of the articles under each category in this format:
     * Category
     *      Sub Category
     *          Article Name
     *          Article Name
     *      Sub Category
     *          Article Name
     * Category
     *      Sub Category
     *          Article Name
     *      Sub Category
     *      Sub Category
     * etc.
     *
     *  Enumerate through all categories/sub categories and use:
     *  networking.Public.getArticlesFromCategory( int mainCategoryID, int subCategoryID, false )
     *
     *  Look at am_utils.CUtils and the unit tests for Article Proccessing for examples of how to enumerate through categories.
     */
    public static File BuildDatabaseOverview()
    {
        return null;
    }

    /*
     * Returns text file for the given category that contains the title of the articles in that category, names of their authors, and their abstracts in this format:
     * Article Name
     *      Authors: Authors
     *      Abstract: Abstract Text
     * Article Name
     *      Authors: Authors
     *      Abstract: Abstract Text
     * etc.
     *
     * Enumerate through all sub categories in the given category and use:
     * Use networking.Public.getArticlesFromCategory( mainCategoryID, int subCategoryID, true )
     */
    public static File BuildDetailedCategoryListing( int mainCategoryID )
    {
        return null;
    }
}
