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
    int ArticleID; 
        
    /** DOI number of the article, if any. */
    public String DOINumber; 
        
    /**The name that the article is displayed under, can be the same as other articles.*/
    public String PrintName; 
        
    /** Category the article is assigned to */
    public MainCategory MainCategory; 
        
    /** Sub-Category the article is assigned to */
    public SubCategory SubCategory; 
        
    /** Authors of the article */
    public String Author;
        
    /** Account who uploaded the article */
    public String Owner; 
        
    /** Text of the abstract section of the document, limited to 5000 characters.  "..." will replace the last 3 characters if the 5000 limit is exceeded.*/
    public String Abstract; 
        
    /** Time the article was uploaded or last updated. */
    public Date UploadTime; 
        
    /** articleID should be 0 on fresh upload, once a value other than 0 is assigned it cannot be changed. */
    public ArticleInfo( int articleID )
    {
        ArticleID = articleID;
    }
        
    /** articleID should be 0 on fresh upload, once a value other than 0 is assigned it cannot be changed. */
    void SetArticleID( int articleID ) 
    { 
        if ( articleID == 0 ) // We can't overwrite the article ID of an article that already has an ID!
            ArticleID = articleID; 
        else
            CUtils.Warning( "Tried to assign ArticleID to article that's already been assigned one!" );
    }
        
    /** Gets the unique internal ID of the article.  No two articles can ever share the same ArticleID, as this way they can share the same print name and still be uniquely addressable. */
    int GetArticleID() { return ArticleID; }
}
