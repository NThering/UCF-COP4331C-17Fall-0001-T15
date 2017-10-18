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
    public static void Msg( String msg )
    {
        System.out.println( msg );
    }

    /** Prints a standard message to the console only if debug mode is on. */
    public static void DebugMsg( String msg )
    {
        if ( DEBUGMODE )
            System.out.println( "[DEBUG] " + msg );
    }

    /** Prints a warning to the console. Should only be used to reflect something going wrong. */
    public static void Warning( String warningString )
    {
        System.out.println( "[WARNING] " + warningString );
    }

    /** Print out a nice list of all categories and subcategories.*/
    public static void PrintOutCategories()
    {
        CUtils.Msg("Printing categories!\n");
        for( CategoryEnums.MainCategory mainCat: CategoryEnums.MainCategory.values() )
        {
            CUtils.Msg(mainCat.PrintName());
            for( CategoryEnums.SubCategory subCat: mainCat.Children() )
                CUtils.Msg( "\t" + subCat.PrintName() );
        }
    }
}