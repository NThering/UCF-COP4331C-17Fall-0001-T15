package am_utils;

/**
 *
 * @author NThering
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
    // Invalidated, Kelsey will have to fix.
    /*
    public static void printOutCategories()
    {
        CUtils.msg("Printing categories!\n");
        for( CategoryEnums.MainCategory mainCat: CategoryEnums.MainCategory.values() )
        {
            CUtils.msg(mainCat.printName());
            for( CategoryEnums.SubCategory subCat: mainCat.children() )
                CUtils.msg( "\t" + subCat.printName() );
        }
    }*/
}