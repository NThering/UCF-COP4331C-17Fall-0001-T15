/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package am_utils;
import am_utils.CategoryEnums.MainCategory;
import am_utils.CategoryEnums.SubCategory;
import java.util.Date;


/**
 *
 * @author NThering
 */
public class ArticleInfo {
    /** The unique internal ID of the article.  No two articles must ever share the same ID, as this way they can share the same print name and still be uniquely addressable. */
    private int articleID; 
        
    /** DOI number of the article, if any. */
    public String doiNumber; 
        
    /**The name that the article is displayed under, can be the same as other articles.*/
    public String printName; 
        
    /** Category the article is assigned to */
    public MainCategory mainCategory; 
        
    /** Sub-Category the article is assigned to */
    public SubCategory subCategory; 
        
    /** Authors of the article */
    public String author;
        
    /** Account who uploaded the article */
    public String owner; 
        
    /** Text of the abstract section of the document, limited to 5000 characters.  "..." will replace the last 3 characters if the 5000 limit is exceeded.*/
    public String abstractText; 
        
    /** Time the article was uploaded or last updated. */
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
