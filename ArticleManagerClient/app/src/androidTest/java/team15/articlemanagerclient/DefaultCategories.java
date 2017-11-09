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
             "Food",
             "People",
             "Technology",
             "Animals",
             "Social",
             "Travel",
             "Life",
             "Other",


             // Under technology




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

        // Biology
        String[] biologyList = {
                "Biodiversity",
                "Molecular",
                "Human",
                "Chemical",
                "Cell"

        };
        mainCat[0].addNewSubcategory(biologyList);


        // food list
       String[] foodList = {"Meat", "Turkey",
                "Chicken",
                "Seafood",
                "Eggs",
                "Milk", "Cheese", "Dairy",
                "Fruits", "Vegetables"};
        mainCat[1].addNewSubcategory(foodList);


        // People
        String[] peopleList = {

                "White",
                "Hispanic",
                "Black and African Americans",
                "Asian Americans",
                "Native Americans and Alaska Natives",


        };
        mainCat[2].addNewSubcategory(peopleList);

        // technology
        String[] technologyList = {
                   "Artificial Intelligence",
                   "Machine Learning",
                  "Visible Light Communication",
                   "Infrared",
                   "Blockchain",
                   "Object Detection",
                    "Transistors",
                    "Nanotechnology",
                    "Quadcopters",
                   "Programming",

        };
        mainCat[3].addNewSubcategory(technologyList);


        // Animals
        String[] animalList = {
                "Dogs",
                "Cats",
                "Bears",
                "Lions",
                "Tigers",
                "Octopus",
                "Pig",
                "Cow",
                "Chicken"


        };
        mainCat[4].addNewSubcategory(animalList);


        // Social

        String[] socialList = {

                "Facebook",
                "Twitter",
                "Snapchat",
                "Instagram",
                "Skype",
                "MySpace"
        };
        mainCat[5].addNewSubcategory(socialList);


        // Travel

        String[] travelList={
            "Airplane",
                "Bike",
                "Segway",
                "Hoverboard",
                "Skateboard",
                "Car",
                "Helicopter"

        };
        mainCat[6].addNewSubcategory(travelList);


        // Life

        String[] lifeList = {
                "Tools",
                "Kitchen",
                "Living",
                "School",
                "Work",
                "Philosophy"

        };
        mainCat[7].addNewSubcategory(lifeList);



        // Other has no subcategories and will be defined separately

    }

        public void categorize(File uploadedFile){




        }
        public int size(){
            return index;
        }

    public MainCategory[] getDefaultCategories(){
        return mainCat;
    }

}
