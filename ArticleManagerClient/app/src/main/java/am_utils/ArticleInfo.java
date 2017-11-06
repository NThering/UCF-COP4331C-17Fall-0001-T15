package am_utils;
import am_utils.MainCategory;
import am_utils.SubCategory;
import java.util.Date;


/**
 *
 * @author NThering
 */

public class ArticleInfo {
    /** The unique internal ID of the article.  No two articles must ever share the same ID, as this way they can share the same print name and still be uniquely addressable.
     * Used in identifying the article for all critical article functions.
     */
    private int articleID;

    /** DOI number of the article, if any.
     * Assigned a value of null if no DOI number exists.
     * Used for future proofing the application
     */
    public String doiNumber;

    /**The name that the article is displayed under, can be the same as other articles.
     * Used for display in the article lists, along with display in the article info and text files.
     */
    public String printName;

    /** Category the article is assigned to.
     * Critical for category display and storage.
     */
    public MainCategory mainCategory;

    /** Sub-Category the article is assigned to
     * Critical for category display and storage.
     */
    public SubCategory subCategory;

    /** Authors of the article
     * Used primarily to display the more specific details of an article when it's selected, but
     * is also used to make that one text file the assignment specs want us to have.
     */
    public String author;

    /** Account who uploaded the article
     * Used for permissions tracking in the database and display of the article's information.
     */
    public String owner;

    /** Text of the abstract section of the document, limited to 5000 characters.  "..." will replace the last 3 characters if the 5000 limit is exceeded.
     * Used for the display of an article's properties along with making the -other- pointless text file required by the assignment.
     */
    public String abstractText;

    /** Time the article was uploaded or last updated.
     * Used for record keeping/article info display.
     */
    public Date uploadTime;

    /** articleID should be 0 on fresh upload, once a value other than 0 is assigned it cannot be changed.
     * @param articleID */
    public ArticleInfo( int articleID )
    {
        this.articleID = articleID;
    }

    /** articleID should be 0 on fresh upload, once a value other than 0 is assigned it cannot be changed. */
    void setArticleID( int articleID )
    {
        if ( articleID == 0 ) // We can't overwrite the article ID of an article that already has an ID!
            this.articleID = articleID;
        else
            CUtils.warning( "Tried to assign articleID to article that's already been assigned one!" );
    }

    /** Gets the unique internal ID of the article.  No two articles can ever share the same ArticleID, as this way they can share the same print name and still be uniquely addressable. */
    int getArticleID() { return articleID; }
}
