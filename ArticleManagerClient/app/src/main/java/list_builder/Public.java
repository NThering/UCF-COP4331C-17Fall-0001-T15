package list_builder;

import java.util.*;
import java.io.*;
import am_utils.DefaultCategories; //import am_utils package
import am_utils.MainCategory;
import am_utils.SubCategory;
import am_utils.CUtils;
import networking.Public; //import networking package

/**
 * Created by NThering and belmaz on 11/22/2017.
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
     *  Look at am_utils.CUtils and the unit tests for Article Processing for examples of how to enumerate through categories.
     */


    private static BufferedReader br = null;
    private static BufferedWriter bw = null;

    public static File BuildDatabaseOverview() throws IOException
    {

        List<Object> listyList = new ArrayList<>();

        DefaultCategories dCat = new DefaultCategories();
        MainCategory mCat[] = new dCat.getDefaultCategories();
        File file = new File("categoryList.txt");
        bw = new BufferedWriter(new FileWriter("categoryList.txt", true));

        for(int i = 0; i < def.size(); i++)
        {
            bw.write(mCat[i]);
            for(int j = 0; j < mCat[i].size(); j++)
            {
                bw.newLine();
                bw.write(mCat[i].children()[j]);
            }
        }


        return file;
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

    public static File BuildDetailedCategoryListing() throws IOException
    {
        File file = new File("detailedCategoryList.txt");
        bw = new BufferedWriter(new FileWriter("detailedCategoryList.txt", true));


        for(int i = 0; i< ls.size(); i++)
        {
            //ArrayList<ArticleInfo> ls = Public.getArticlesFromCategory();

            bw.write(Public.getArticlesFromCategory(i));
            for(int j = 0; j<Public.getArticlesFromCategory().size(); j++)
            {

            }
        }


        return file;
    }
}
