package am_utils;

import am_utils.MainCategory;
import am_utils.SubCategory;
import am_utils.DefaultCategories;

/**
 *
 * @author NThering
 *
 * Console Utils; Utility functions concerning the console.
 */

public final class CUtils
{
    /** If we're allowing debug messages and features */
    private static final boolean DEBUGMODE = true;

    private CUtils()
    {
    }

    /** Prints a standard message to the console. */
    public static void msg( String msg )
    {
        System.out.println( msg );
    }

    /** Prints a standard message to the console only if debug mode is on. */
    public static void debugMsg( String msg )
    {
        if ( DEBUGMODE )
            System.out.println( "[DEBUG] " + msg );
    }

    /** Prints a warning to the console. Should only be used to reflect something going wrong. */
    public static void warning( String warningString )
    {
        System.out.println( "[WARNING] " + warningString );
    }


    /** Print out a nice list of all categories and subcategories.*/
    public static void printOutCategories()
    {
        CUtils.msg("Printing categories!\n");

        // This is the safe way to navigate Kelsey's category system.
        DefaultCategories defaultCat = new DefaultCategories();
        MainCategory mainCatList[] = defaultCat.getDefaultCategories();

        for( int i = 0; i < defaultCat.size(); i++ )
        {
            CUtils.msg( mainCatList[i].printName() );
            for( int g = 0; g < mainCatList[i].size(); g++ )
                CUtils.msg( "\t" + mainCatList[i].children()[g].printName() );
        }
    }
}