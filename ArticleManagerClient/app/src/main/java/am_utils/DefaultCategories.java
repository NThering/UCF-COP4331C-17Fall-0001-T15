package com.remaclek.kelcamer.academicarticlemanager;

import android.os.Environment;

import java.io.File;

/**
 * Created by Kelsey on 10/31/17.
 */

public class DefaultCategories {

     private MainCategory[] mainCat;
     private int index;
     String test;
     private String mainCategories[] = {

             "Biology",
             "kenya",
            "Food",
             "Mailing",
            "Artificial Intelligence",
            "Machine Learning",
            "Visible Light Communication",
            "Infrared",
             "People",
             "hello",
            "Blockchain",
            "Object Detection",
            "Transistors",
            "Nanotechnology",
            "Quadcopters",



    };

    DefaultCategories(){
        index = -1;
        mainCat = new MainCategory[50];
        createCategories();

    }
    private void createCategories() {

       for(int x = 0; x < mainCategories.length; x++){

            mainCat[x] = new MainCategory(mainCategories[x]);
            index++;
        }
        categorize();



    }

        public void categorize(){

            if(mainCat == null) return;
            if(mainCat[0] == null)  return;
            if(mainCat[0].equals(""))  return;

            FileConverter convert = new FileConverter();

            String root = Environment.getExternalStorageDirectory().toString() + "/Article Manager/";
            File directory = new File(root);

            File[] allFiles = directory.listFiles();

            // loop for all files in the Article Manager directory
            for(int y = 0; y < allFiles.length; y++){

                String name = allFiles[y].getName();
                File convertedText = convert.convertFromPDF(allFiles[y], 1);
                
                String result = convert.readFile(convertedText.getName());

                // loop through string array of categories and assign an id index
                for(int z = 0; z < mainCategories.length; z++){
                    test = result;
                    if(result.contains(mainCategories[z])){

                        mainCat[y].setID(z);
                        mainCat[y].setName(mainCategories[z]);


                    }




                }



            }



        }
        public int size(){
            return index;
        }

    public MainCategory[] getDefaultCategories(){
        return mainCat;
    }

}
