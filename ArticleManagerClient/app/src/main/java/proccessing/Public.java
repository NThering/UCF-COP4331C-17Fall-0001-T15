package proccessing;

import java.io.File;
import am_utils.ArticleInfo;

public class Public {

    /** Takes a reference to an article and returns a filled out ArticleInfo object.  This includes extracting the Abstract of the paper, but not assigning an ArticleID, Owner, or UploadTime as these are determined elsewhere.
     * Returns null on complete failure to parse article, Abstract  */
    ArticleInfo CategorizeArticle( File articleFile )
    {
        return null;
    }

    /** Takes a pdf file and turns it into a file of the designated type. Returns null on error. */
    File ConvertFromPDF( File inputFile, int fileType )
    {
        return null;
    }

    /** Takes a file of a supported type and turns it into a pdf.  Returns null on error or unsupported filetype. */
    File ConvertToPDF( File inputFile )
    {
        return null;
    }
}
