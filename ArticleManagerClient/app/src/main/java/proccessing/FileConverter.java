package proccessing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

import am_utils.CUtils;


public class FileConverter extends AppCompatActivity {
    public static String toast = "";
    HashSet<String> englishWordSet;

    public FileConverter()
    {
        BufferedReader wordList = null;
        try
        {
            // IAN PLEASE FIGURE OUT HOW TO OPEN THIS FILE ON ANDROID.
            // From what I've read, "getAssets().open("english_words.txt") works but I can't test it with unit tests.
            // If that doesn't work you might need to call it from a context but that needs to be created in the higher levels or something.

            // To use unit tests, just link "english_words" to its location on your computer.
            wordList = new BufferedReader(new FileReader("english_words.txt"));
        }
        catch(Exception e)
        {
            CUtils.warning("FAILED TO LOCATE ENGLISH_WORDS.TXT!!!\n\n" + e.getMessage());
            return; // We've failed to build a useful word database.
        }

        englishWordSet = new HashSet<String>(400000); // Yes the dictionary is that big.

        try
        {
            // Insert all of our words into the "dictionary" for quick access times.
            String word;
            while ((word = wordList.readLine()) != null)
            {
                if (Character.isUpperCase(word.charAt(0)))  // Root out any names that might be in there.
                {
                    int upperCount = 0;
                    for (char character : word.toCharArray())
                    {
                        if (Character.isUpperCase(character))
                            upperCount++;
                    }


                    if (upperCount == 1 && word.length() != 1) // If there's more than one capital letter it's probably just an abbreviation.
                        continue;
                }

                englishWordSet.add(word.toLowerCase());
            }
        }
        catch(Exception e)
        {
            CUtils.warning("FAILED TO PARSE ENGLISH_WORDS.TXT!!!\n\n" + e.getMessage());
        }

    }

    public boolean isWord(String word)
    {
        return englishWordSet.contains(word.toLowerCase());
    }

    public boolean isName(String word)
    {
        String simpleWord = word.replaceAll("\\P{Print}", "");

        if ( simpleWord.length() < 4 ) // Too short to be a name.  Sorry Sue and Tom.
            return false;

        if ( simpleWord.matches(".*\\d+.*") ) // If our string has digits, it's probably not a name.
            return false;

        if ( Character.isLowerCase(simpleWord.charAt(0)) ) // If we don't start with an upper case letter, we're not a name!
            return false;

        if ( isWord( simpleWord ) ) // If it's a word it's probably not a name.
            return false;

        return true;
    }

    public String getAuthorFromText( String inputText )
    {
        String authors = "";
        String[] lines =  inputText.split("\n");

        if ( lines.length > 50 )
            lines = java.util.Arrays.copyOf(lines, 50);

        lines[0] = ""; // No authors have appeared on the first line in our examinations.

        // If an author is mentioned they probably came up in the first few lines.
        // Don't want to scan too many because our dictionary is kind of bad and
        // the chance of random non-words popping up past the first few gets higher and higher.
        for ( String line : lines )
        {
            String[] words =  line.split(" ");

            int lineScore = 0;

            for ( String word : words )
            {
                if (isName(word))
                    lineScore++;
            }

            // If we've got 4 unidentified words, or every word on the line is unidentified, we probably hit our target.
            if (lineScore > 4 || (lineScore == line.length() && lineScore > 1))
                return line;
        }

        return null;
    }

    public String getAbstractFromText(String text)
    {
        String[] lines = text.split("\n");
        boolean collectAbstract = false;
        boolean collectingAbstract = false;
        String abstractLines = "";

        for (String line : lines)
        {
            String simpleLine = line.toLowerCase().replaceAll("\\P{Print}", "");

            //CUtils.msg(simpleLine);

            // We've already found the Abstract tag and want to get the actual abstract.
            if ( collectAbstract )
            {
                // Just a blank line.
                if ( simpleLine.replaceAll(" ", "").isEmpty() )
                {
                    if (collectingAbstract)
                    {
                        break;
                    }
                    else
                        continue;
                }

                collectingAbstract = true;
                abstractLines += line;
                continue;
            }

            if ( simpleLine.startsWith("abstract") ) // "abstract" is a pretty uncommon word to start a line with, unless specifying one.
            {
                collectAbstract = true;
                // That was just the word "abstract" by itself pretty much so the next paragraph is probably the abstract.
                if (simpleLine.length() < 10)
                    continue;

                // Otherwise add this line, minus the word "abstract" of course.
                if ( line.length() > 8)
                    abstractLines += line.substring(8, line.length());
            }
        }


        if (!abstractLines.isEmpty())
        {
            if (abstractLines.length() < 5000)
                return abstractLines;
            else
                return abstractLines.substring(0, 4997) + "...";
        }
        else
            return null; // Failed to locate abstract!
    }

    public String getDoiFromText(String text)
    {
        // first search for 10. and then skip the first / and substring on the following space
        if (text.contains("10."))
        {
            text = text.substring(text.indexOf("10."));

            if (text.contains("/"))
            {
                try
                {
                    if(text.contains("\n"))
                    {
                        text = text.substring(0, text.indexOf('\n'));
                    }
                    if(text.contains(" "))
                    {
                        text = text.substring(0, text.indexOf(" "));
                    }
                }
                catch (Exception e)
                {
                    CUtils.warning("Messed up DOI number parsing somehow!!!");
                }
            }
            else
                return null; // This isn't a DOI number, probably a page number or something.
        }
        else
            return null; // Doesn't have a DOI number so don't just return the entire text as one.

        return text;
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


    public String extractTextFromPDF(String filePath, int pageNumber){
        String result = "";

        try
        {
            PdfReader reader = new PdfReader(filePath);

            result = PdfTextExtractor.getTextFromPage(reader, pageNumber);
            reader.close();
        }
        catch(Exception e)
        {
            result = "fail";
        }

        return result;
    }

    public String setPath(String filepath){
        String correctedPath = "";



        if(!filepath.contains("Article Manager")){
            correctedPath = Environment.getExternalStorageDirectory() + "/Article Manager/" + filepath;
        }
        else{
            return filepath;
        }
        return correctedPath;

    }

    /*
    1 = convert to .txt
    2 = convert to .odt
    3 = convert to .html
    default converts to .txt
    */
    public File convertFromPDF(String filename, int filetype)
    {
        String result = " ";
        String nextline = "";
        int x = 1;
        while(nextline != "fail"){
            result += nextline;
            toast = nextline;
            try {
                nextline = extractTextFromPDF(filename, x);
            }
            catch (Exception e){

            }

            x++;
        }


        toast = getFileWithoutExt(filename) + ".txt";

        // List of our supported extensions, the index of the array being the ID.
        // Add more extensions at will to the end of the array!
        // of course, createNewFile will have to support them first.
        String[] supportedExtensions = {".txt", ".txt", ".odt", ".html"};

        if ( filetype >= supportedExtensions.length || filetype < 0)
        {
            CUtils.warning("Tried to convert to invalid filetype!");
            return null;
        }

        return createNewFile(getFileWithoutExt(filename) + supportedExtensions[filetype], result);
    }


    public String readFile(String filename){


        File dir = createNewDirectory("Article Manager");
        String root = dir.getAbsolutePath() + "/" +filename;

        File sdcard = new File(root);

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
            String removeDot = "";

        if(fileName.contains(".")) {
             removeDot = fileName.substring(0, fileName.lastIndexOf("."));
        }

        return removeDot;
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

    public File createNewFile(String name, String data){
        // get the path to sdcard
        File sdcard = Environment.getExternalStorageDirectory();

        // to this path add a new directory path
        File dir = createNewDirectory("Article Manager");

        // create the file in which we will write the contents
        File file = new File(dir, name);


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
}
