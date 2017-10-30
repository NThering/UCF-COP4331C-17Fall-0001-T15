package com.remaclek.kelcamer.academicarticlemanager;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String root = Environment.getExternalStorageDirectory().toString();

        File pdffile = convertToPDF(createNewFile("helloworld2.txt"));
        convertFromPDF(pdffile,3);

        //   Toast.makeText(getApplicationContext(),pdffile.getAbsolutePath(), Toast.LENGTH_LONG).show();
//  TextView output=(TextView) findViewById(R.id.myTextbox);
        // Assuming that 'output' is the id of your TextView

        listFiles();
        //  output.setText(result);

    }


    public void listFiles(){
       // String root = "file";

      //  String result = readFile(root + ".txt");


      //  if(result == null){
        //    createNewFile(root);
       //     result = readFile(root + ".txt");
        //}


       CategoryEnums.MainCategory categoryMain = new CategoryEnums.MainCategory("Animals");
        categoryMain.addNewSubcategory("Dogs");
        categoryMain.addNewSubcategory("Cats");
        categoryMain.addNewSubcategory("Birds");

        CategoryEnums.SubCategory[] children = categoryMain.children();
        toast(categoryMain.printName() + "");           // prints Animals

        for(int q = 0; q < categoryMain.size(); q++){
            toast(children[q].printName());
            // prints Dogs, Cats, Birds
        }

    }
    public void toast(String s){
        Toast.makeText(getApplicationContext(), s , Toast.LENGTH_LONG).show();
    }

    public File convertToPDF(File inputFile ){

        if(!inputFile.exists()){
            return null;
        }

        String ext = getFileExt(inputFile.getName());
        if(ext.contains( "pdf")){
            return inputFile;
        }
        else if(ext.contains("txt")){

            String content = readFile(inputFile.getName());
            File pdf = createPDFFromString(content, inputFile.getName());
            return pdf;
        }
        else if(ext.contains("odt")){
            String content = readFile(inputFile.getName());
            File pdf = createPDFFromString(content, inputFile.getName());
            return pdf;
        }
        else if(ext.contains("html")){
            String content = readFile(inputFile.getName());
            File pdf = createPDFFromString(content, inputFile.getName());
            return pdf;

        }
        else{
            return null;
        }

    }


    /*
    1 = convert to .txt
    2 = convert to .odt
    3 = convert to .html
    default converts to .txt

     */
    public File convertFromPDF(File inputFile, int filetype){
        String parsedText="";

        try {

            PdfReader reader = new PdfReader(inputFile.getAbsolutePath());
            int n = reader.getNumberOfPages();
            for (int i = 0; i <n ; i++) {
                parsedText   = parsedText+ PdfTextExtractor.getTextFromPage(reader, i+1).trim()+"\n"; //Extracting the content from the different pages

            }

            reader.close();
        } catch (Exception e) {
            System.out.println(e);
        }


        if(!inputFile.exists()){
            return null;
        }


        // awesome, the text is now grabbed


        switch(filetype){
            case 1:
                // text file
                createNewFile(getFileWithoutExt(inputFile.getName()) + ".txt");
                break;
            case 2:
                createNewFile(getFileWithoutExt(inputFile.getName()) + ".odt");
                break;
            case 3:
                createNewFile(getFileWithoutExt(inputFile.getName()) + ".html");
                break;
            default:
                createNewFile(getFileWithoutExt(inputFile.getName()) + ".txt");
                break;
            case 4:
                break;



        }


        return inputFile;
    }


    public static String getFileExt(String fileName) {

        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }

    public static String getFileWithoutExt(String fileName) {

        return fileName.substring(0, fileName.lastIndexOf("."));
    }
    public File createNewDirectory(String nameOfDir){
        // get the path to sdcard
        File sdcard = Environment.getExternalStorageDirectory();
        // to this path add a new directory path
        File dir = new File(sdcard.getAbsolutePath() + "/" + nameOfDir + "/");

        if(!dir.exists())
        {
            // create this directory if not already created
            dir.mkdir();
        }


        return dir;
    }

    public File createNewFile(String name){
        // get the path to sdcard
        File sdcard = Environment.getExternalStorageDirectory();
        // to this path add a new directory path
        File dir = createNewDirectory("Article Manager");

        // create the file in which we will write the contents
        File file = new File(dir, name);

        String data = "hello world kelsey";


        try {
            FileOutputStream os = new FileOutputStream(file);

            os.write(data.getBytes());
            os.close();
        }
        catch(IOException e){
            return null;
        }

        Toast.makeText(getApplicationContext(),file.getAbsolutePath() + " created.", Toast.LENGTH_LONG).show();

        return file;
    }

    public File createPDFFromString(String data, String filename) {
        File dir = createNewDirectory("Article Manager");

        if(filename.contains(".")){
            filename = filename.substring(0, filename.indexOf("."));
        }
        String root = dir.getAbsolutePath() + "/" +filename+ ".pdf";
        try {

            File file = new File(root);

            file.createNewFile();



            FileOutputStream fOut = new FileOutputStream(file);


            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new
                    PdfDocument.PageInfo.Builder(100, 100, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();

            // look up how to use default fonts in paint and make sure font sizes look good
            paint.setLetterSpacing((float)0.1);
            paint.setTextSize((float)3.0);
            paint.getFontMetrics();
            canvas.drawText(data, 10, 10, paint);


            document.finishPage(page);
            document.writeTo(fOut);
            document.close();
            return file;


        } catch (IOException e) {
           // Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();

            Log.i("error", e.getLocalizedMessage());
        }
        return null;
    }
    //Returns a list to the client of all articles in a given category/sub-category,
    // this includes their ID number on the server.
    String[] getArticlesFromCategory( int articleCategory, int subCategory ){


        /*
            Plan:

            1) Figure out how to convert files to docx, pdf, doc, txt
            1) Use Key Value Pair Hash Tables to Map the Title of Each Article to a Particular category.
            2) Key = name of article, value = which category
            3) Allow multiple keys?
            4) Create preset categories and subcategories that each correspond to a single number.
            5) The server knows which number corresponds to which category and can accordingingly
            store the files in their respective subfolders.
            6) To search article, just extract the text data, store in string, and check if
            string.contains()
            7) UI of displaying this all in a List View

         */



        return null;
    }

    public String readFile(String filename){


        File dir = createNewDirectory("Article Manager");
        String root = dir.getAbsolutePath() + "/" +filename;

        File sdcard = new File(root);
      //  Toast.makeText(getApplicationContext(), "Reading file " + sdcard.getPath(), Toast.LENGTH_LONG).show();

        //ConvertToPDF(sdcard);
        StringBuilder text = new StringBuilder();
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(sdcard));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
                result+=line;
            }
        }
        catch (IOException e) {
         //   Toast.makeText(getApplicationContext(),"Error reading file!",Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return null;
        }
        return result;
    }






}
