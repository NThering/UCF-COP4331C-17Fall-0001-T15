package com.remaclek.kelcamer.myapplication;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listFiles();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void listFiles(){

       // File sdcard = new File(root);

     /*   try {


            if (!sdcard.exists()) {
                sdcard.createNewFile();
                Toast.makeText(getApplicationContext(),"Creating new file.",Toast.LENGTH_LONG).show();
                FileWriter writer = new FileWriter(sdcard);
                writer.append("hello world kelsey");
                writer.flush();
                writer.close();

            }


        }   catch(IOException ex) {

        }*/

       String result = readFile("helloworld.docx");

        TextView output=(TextView) findViewById(R.id.myTextbox);
        // Assuming that 'output' is the id of your TextView


        if(result.contains("hello")){
            output.setText("hello");
        }
       // output.setText(text);



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

        String root = Environment.getExternalStorageDirectory() + "/" + filename;

        File sdcard = new File(root);
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
            Toast.makeText(getApplicationContext(),"Error reading file!",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
