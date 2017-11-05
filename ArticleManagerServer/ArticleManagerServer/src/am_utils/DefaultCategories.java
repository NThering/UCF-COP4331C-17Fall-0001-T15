package com.remaclek.kelcamer.academicarticlemanager;

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
             "USPS",
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

        public void categorize(File file){




        }
        public int size(){
            return index;
        }

    public MainCategory[] getDefaultCategories(){
        return mainCat;
    }

}
