package networking;
import java.io.File;
import java.util.ArrayList;
import am_utils.ArticleInfo;

public class Public {
    /*    General Responsibilities:
    *     Keep track of logged in users and their permissions so they don't need to re-authenticate for every action.
    */

    // All calls to networking should probably be done in a seperate thread to prevent hangups!

    /** Relays user info to the login system on the server, returning 0 on the client if the login was successful and an int corresponding to the type of error if it was not. */
    public static int Login( String username, String password )
    {
        return 1;
    }

    /** Attempts to log out of the server, returning 0 on the client if the logout was successful and an int corresponding to the type of error if it was not. */
    public static int Logout( )
    {
        return 1;
    }

    /** Relays user info to the login system on the server, returning 0 on the client if the registration was successful and an int corresponding to the type of error if it was not. */
    public static int Register( String username, String password )
    {
        return 1;
    }

    /** Returns a list to the client of all ArticleInfos in a given category/sub-category.  If IncludeAbstracts is false, abstracts are left blank to save bandwidth.*/
    public static ArrayList<ArticleInfo> GetArticlesFromCategory(int articleCategory, int subCategory, boolean IncludeAbstracts )
    {
        return null;
    }

    /** Sends the article to the database for entry, returns 0 if successful and an int corresponding to the type of error if it was not.  If ArticleID in the supplied ArticleInfo is not 0 this upload is meant to replace an existing upload. */
    public static int UploadArticle(File articleFile, ArticleInfo articleInfo )
    {
        return 1;
    }

    /** Gets information on the given article from the database. */
    public static ArticleInfo GetArticleInfo( int articleID )
    {
        return null;
    }

    /** Downloads the given article from the database. */
    public static ArticleInfo DownloadArticle( int articleID )
    {
        return null;
    }

    /** Gets the permissions flags for the user, returning -1 if not logged in. */
    public static int GetPermissions()
    {
        return 0;
    }

    /** Abort any ongoing networking activity  Do not terminate the user's session, just cancel whatever transfers were taking place
     * in case the user hits the back button or something. */
    public static void AbortActiveConnections()
    {
        return;
    }
}
