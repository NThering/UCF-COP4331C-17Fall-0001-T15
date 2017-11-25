package proccessing;

import java.io.File;

import am_utils.ArticleInfo;
import am_utils.MainCategory;
import am_utils.SubCategory;

public class PublicUsage {

    /** Takes a reference to an article and returns a filled out ArticleInfo object.  This includes extracting the Abstract of the paper, but not assigning an ArticleID, Owner, or UploadTime as these are determined elsewhere.
     * Returns null on complete failure to parse article, Abstract  */
    public static ArticleInfo categorizeArticle( File articleFile )
    {
        return null;
    }



    public static ArticleInfo categorize(File uploadedFile, MainCategory[] mainCat, int numberOfCat){

        ArticleInfo filledArticle = new ArticleInfo(0);

        FileConverter convert = new FileConverter();
        File pdfFile = convert.convertToPDF(uploadedFile);


        for(int z = 1; z < 5; z++){
            String data = convert.extractTextFromPDF(pdfFile.getName(), z);


            for(int x = 0; x < numberOfCat; x++){
                if(data.contains(mainCat[x].printName()) || uploadedFile.getName().contains(mainCat[x].printName())) {
                    SubCategory[] children = mainCat[x].children();
                    filledArticle.setMainCategoryIndex(x);

                    for (int y = 0; y < mainCat[x].size(); y++) {
                        if (data.contains(children[y].printName())
                                || uploadedFile.getName().contains(children[y].printName())
                                || data.contains(checkModifiedWord(children[y].printName()))) {

                           // test = mainCat[x].printName() + " " + children[y].printName();
                            filledArticle.setSubCategoryIndex(y);

                            filledArticle = addExtraData(filledArticle, data);
                            return filledArticle;

                        }


                    }


                }
            }


        }



        return filledArticle;
    }

    private static ArticleInfo addExtraData(ArticleInfo article, String data){

        FileConverter convert = new FileConverter();
        article.setDoiNumber(convert.getDoiFromText(data));
        article.setAbstractText(convert.getAbstractFromText(data));
        // article.setAuthor(convert.getAuthorFromText(data));
        return article;
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
    public static File convertFromPDF( File inputFile, int fileType )
    {
        return null;
    }

    /** Takes a file of a supported type and turns it into a pdf.  Returns null on error or unsupported filetype. */
    public static File convertToPDF( File inputFile )
    {
        return null;
    }
}
