package list_builder;

import java.util.*;
import java.io.*;

import am_utils.ArticleInfo;
import am_utils.DefaultCategories; //import am_utils package
import am_utils.MainCategory;
import am_utils.SubCategory;
import am_utils.CUtils;
import networking.Public.*; //import networking package

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

    public static File BuildDatabaseOverview( String targetDirectory )
    {
        DefaultCategories dCat = new DefaultCategories();
        MainCategory mCat[] = dCat.getDefaultCategories();
        File file = new File(targetDirectory + "/categoryList.txt");

        BufferedWriter bw;

        try
        {
            bw = new BufferedWriter(new FileWriter(file, false));
        }
        catch (Exception e)
        {
            CUtils.warning("Failed to create buffered writer!!!");
            return null; // Can't win now.
        }

        try
        {
            for (int i = 0; i < dCat.size(); i++)
            {
                if (mCat[i] == null) // This can happen because this system requires retired category entries to be replaced by null to preserve ids.
                    continue;

                bw.write(mCat[i].printName());
                bw.newLine();

                for (int j = 0; j < mCat[i].size(); j++) {
                    if (mCat[i].children()[j] == null)
                        continue;


                    bw.write("\t" + mCat[i].children()[j].printName());
                    bw.newLine();

                    ArrayList<ArticleInfo> ls = networking.Public.getArticlesFromCategory(i, j, false);
                    for (ArticleInfo item : ls)
                    {
                        bw.write("\t\t" + item.printName);
                        bw.newLine();
                    }
                }
            }
        }
        catch (Exception e)
        {
            CUtils.warning("Encountered error when writing out file!!!");
            return null; // Ragequit
        }

        try
        {
            bw.close();
        }
        catch (Exception e)
        {
            CUtils.warning("Could not save file!");
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

    public static File BuildDetailedCategoryListing( String targetDirectory, int targetCategory )
    {
        DefaultCategories dCat = new DefaultCategories();
        MainCategory mCat[] = dCat.getDefaultCategories();

        if ( mCat[targetCategory] == null ) // This can happen because this system requires retired category entries to be replaced by null to preserve ids.
            return null;

        File file = new File(targetDirectory + "/" + mCat[targetCategory].printName().replace(" ","") + "ArticleList.txt");

        BufferedWriter bw;

        try
        {
            bw = new BufferedWriter(new FileWriter(file, false));
        }
        catch (Exception e)
        {
            CUtils.warning("Failed to create buffered writer!!!");
            return null; // Can't win now.
        }

        try
        {
            bw.write(mCat[targetCategory].printName());
            bw.newLine();
            for (int j = 0; j < mCat[targetCategory].size(); j++)
            {
                if (mCat[targetCategory].children()[j] == null)
                    continue;

                bw.write("\t" + mCat[targetCategory].children()[j].printName());
                bw.newLine();

                ArrayList<ArticleInfo> ls = networking.Public.getArticlesFromCategory(targetCategory, j, true);
                for (ArticleInfo item : ls)
                {
                    bw.write("\t\t" + item.printName);
                    bw.newLine();
                    bw.write("\t\t\t" + item.author);
                    bw.newLine();
                    bw.write("\t\t\t" + item.abstractText);
                    bw.newLine();
                }
            }
        }
        catch (Exception e)
        {
            CUtils.warning("Encountered error when writing out file!!!");
            return null; // Ragequit
        }

        try
        {
            bw.close();
        }
        catch (Exception e)
        {
            CUtils.warning("Could not save file!");
        }

        return file;
    }
}
