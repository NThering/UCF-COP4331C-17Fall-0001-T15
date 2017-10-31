package com.remaclek.kelcamer.academicarticlemanager;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Kelsey on 10/31/17.
 * // File pdffile = convertToPDF(createNewFile("helloworld2.txt"));
 //convertFromPDF(pdffile,3);
 */

public class FileConverter extends AppCompatActivity {


    FileConverter(){


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
         //   paint.setLetterSpacing((float)0.1);
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

      //  Toast.makeText(getApplicationContext(),file.getAbsolutePath() + " created.", Toast.LENGTH_LONG).show();

        return file;
    }
}
