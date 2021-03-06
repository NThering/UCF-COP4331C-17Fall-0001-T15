package proccessing;

import android.content.Context;

import java.io.File;

import am_utils.ArticleInfo;
import am_utils.MainCategory;
import am_utils.SubCategory;

public class PublicUsage {

    /** Takes a reference to an article and returns a filled out ArticleInfo object.  This includes extracting the Abstract of the paper, but not assigning an ArticleID, Owner, or UploadTime as these are determined elsewhere.
     * Returns null on complete failure to parse article, Abstract
     * ONLY TAKES PDF FILES
     * */
    public static ArticleInfo categorize(File uploadedFile, MainCategory[] mainCat, int numberOfCat, Context context)
    {
        ArticleInfo filledArticle = new ArticleInfo(0);
        filledArticle.printName = uploadedFile.getName().replace(".pdf", "");

        FileConverter convert = new FileConverter(context);

        for(int z = 1; z < 5; z++){
            String data = convert.extractTextFromPDF(uploadedFile.getPath(), z);

            for(int x = 0; x < numberOfCat; x++){
                if(data.toLowerCase().contains(mainCat[x].printName().toLowerCase()) || uploadedFile.getName().toLowerCase().contains(mainCat[x].printName().toLowerCase())) {
                    SubCategory[] children = mainCat[x].children();
                    filledArticle.setMainCategoryIndex(x);

                    for (int y = 0; y < mainCat[x].size(); y++) {
                        if (data.toLowerCase().contains(children[y].printName().toLowerCase())
                                || uploadedFile.getName().toLowerCase().contains(children[y].printName().toLowerCase())
                                || data.toLowerCase().contains(checkModifiedWord(children[y].printName().toLowerCase()))) {

                            filledArticle.setMainCategoryIndex(x);
                            filledArticle.setSubCategoryIndex(y);
                            filledArticle = addExtraData(filledArticle, data, context);

                            return filledArticle;
                        }
                    }
                }
            }
        }

        return filledArticle;
    }

    private static ArticleInfo addExtraData(ArticleInfo articleInfo, String data, Context context)
    {
        FileConverter convert = new FileConverter(context);
        articleInfo.setDoiNumber(convert.getDoiFromText(data));
        articleInfo.setAbstractText(convert.getAbstractFromText(data));
        articleInfo.setAuthor(convert.getAuthorFromText(data));

        return articleInfo;
    }

    private static String checkModifiedWord(String word){
        if(word.contains("ology")){
            word = word.substring(0, word.indexOf("ology"));
            word += "ological";

        }


        if(word.contains("s")){
            word = word.substring(0, word.lastIndexOf("s"));
        }


        if(word.contains("aphy")){
            word = word.substring(0, word.indexOf("aphy"));
            word += "aphical";
        }
        if(word.contains(" ")){
            word.replace(" ", "");
        }
        return word;

    }
    /** Takes a pdf file and turns it into a file of the designated type. Returns null on error. */
    public static File convertFromPDF( File inputFile, int fileType, Context context )
    {
        // Just linking Kelsey's system to her public class.
        return (new FileConverter(context)).convertFromPDF( inputFile, fileType);
    }

    /** Takes a file of a supported type and turns it into a pdf.  Returns null on error or unsupported filetype. */
    public static File convertToPDF( File inputFile, Context context )
    {
        // Just linking Kelsey's system to her public class.
        return (new FileConverter(context)).convertToPDF( inputFile );
    }
}
