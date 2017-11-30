package am_utils;
import java.util.Date;


/**
 *
 * @author NThering
 */

public class ArticleInfo implements java.io.Serializable {

    private static final long serialVersionUID = 3985753306929785362L;

    /**
     * The unique internal ID of the article.  No two articles must ever share the same ID, as this way they can share the same print name and still be uniquely addressable.
     * Used in identifying the article for all critical article functions.
     */
    private int articleID;
   
    /**
     * DOI number of the article, if any.
     * Assigned a value of null if no DOI number exists.
     * Used for future proofing the application
     */
    public String doiNumber;

    /**
     * The name that the article is displayed under, can be the same as other articles.
     * Used for display in the article lists, along with display in the article info and text files.
     */
    public String printName;

    /**
     * Category the article is assigned to.
     * Critical for category display and storage.
     */
    public int mainCategoryID;

    /**
     * Sub-Category the article is assigned to
     * Critical for category display and storage.
     */
    public int subCategoryID;

    /**
     * Authors of the article
     * Used primarily to display the more specific details of an article when it's selected, but
     * is also used to make that one text file the assignment specs want us to have.
     */
    public String author;

    /**
     * Account who uploaded the article
     * Used for permissions tracking in the database and display of the article's information.
     */
    public String owner;

    /**
     * Text of the abstract section of the document, limited to 5000 characters.  "..." will replace the last 3 characters if the 5000 limit is exceeded.
     * Used for the display of an article's properties along with making the -other- pointless text file required by the assignment.
     */
    public String abstractText;

    /**
     * Time the article was uploaded or last updated.
     * Used for record keeping/article info display.
     */
    public Date uploadTime;

    /**
     * Basic constructor that assumes all relevant values will be manually set after it is called.
     * articleID should be 0 on fresh upload, once a value other than 0 is assigned it cannot be changed.
     * @param articleID Unique ID of the article
     */
    public ArticleInfo( int articleID )
    {
        this.articleID = articleID;
        this.doiNumber = null;
        this.printName = null;
        this.mainCategoryID = -1;
        this.subCategoryID = -1;
        this.author = null;
        this.owner = null;
        this.abstractText = null;
        this.uploadTime = null;
    }
    
    /*
        Allows you to create a new Article Info object that has a default category and subcategory,
        and allows you to set the categories.  
        
    */
    public ArticleInfo(int id, int sub, int articleID)
    {
        mainCategoryID = id;
        subCategoryID = sub;
        author = "";
        owner = "";
        abstractText = "";
        doiNumber = "";
        articleID = 0;
    }

    /** Constructor that builds an ArticleInfo with all the data that Article Processing needs to fill out.
     * articleID should be 0 on fresh upload, once a value other than 0 is assigned it cannot be changed.
     * @param articleID Unique ID of the article, set by the server and used internally.
     * @param doiNumber Object Identification number of the article, if there is one.
     * @param printName Name the article is displayed under, typically the title.
     * @param mainCategoryID ID of the main category the article is under.
     * @param subCategoryID ID of the sub category the article is under.
     * @param author Authors of the article
     * @param abstractText Abstract of the article
     */
    public ArticleInfo( int articleID, String doiNumber, String printName, int mainCategoryID, int subCategoryID, String author, String abstractText)
    {
        this.articleID = articleID;
        this.doiNumber = null;
        this.printName = null;
        this.mainCategoryID = -1;
        this.subCategoryID = -1;
        this.author = null;
        this.owner = null;
        this.abstractText = null;
        this.uploadTime = null;
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
    public int getArticleID() { return articleID; }
    
    public void setMainCategoryIndex( int mainID )
    {
       
            this.mainCategoryID = mainID;

    }
    public void setSubCategoryIndex( int subID )
    {
            this.subCategoryID = subID;

    }
    // author of article
    public void setAuthor(String author){
        this.author = author;
    }

    public void setDoiNumber(String doi){
        this.doiNumber = doi;
    }
    public void setAbstractText(String abs){
        this.abstractText = abs;
    }
    public void setOwner(String owner){
        this.owner = owner;
    }
    /** Gets the unique internal ID of the article.  No two articles can ever share the same ArticleID, as this way they can share the same print name and still be uniquely addressable. */
    public int getMainArticleID() { return mainCategoryID; }
    public int getSubArticleID() { return subCategoryID; }
    
}
