package com.remaclek.kelcamer.academicarticlemanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        FileConverter convert = new FileConverter();
        String test = convert.extractTextFromPDF("Biodiversity.pdf", 1, 1);
        toast(test);

       // DefaultCategories cat = new DefaultCategories();            // constructor sets the ids accordingly

        // Problem:  1)  Large PDF Files take way longer to read
        //           2)  Cannot read certain types of pdf
        //


/*        for(int x = 0; x < cat.size(); x++){

          //  toast(cat.getDefaultCategories()[x].getID() + "");
            toast(cat.getDefaultCategories()[x].getName() + " " + cat.getDefaultCategories()[x].getID());

        }*/




//  TextView output=(TextView) findViewById(R.id.myTextbox);
        //  output.setText(result);

    }


    public void toast(String s){
        Toast.makeText(getApplicationContext(), s , Toast.LENGTH_LONG).show();
    }




    //Returns a list to the client of all articles in a given category/sub-category,
    // this includes their ID number on the server.
    String[] getArticlesFromCategory( int articleCategory, int subCategory ){


        /*
            Plan:

            1) Figure out how to convert files to pdf, doc, txt
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







}
