package com.remaclek.kelcamer.academicarticlemanager;

/**
 * Created by Kelsey on 10/27/17.
 *
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
 *
 *
 *
 *
 *
 *
 *
 * Functions:
 * 1) You can create a new Maincategory by creating an instance of MainCategory.
 * 2) You can create a new subcategory by calling mainCategory.addNewSubcategory().
 * 3) You can call children() to get an array of the children
 * 4) you can call size() to get the size of children in the mainCategory
 *
 *
 */

public class CategoryEnums {


        public static class SubCategory
        {
            /** Name of subcategory as it appears in the GUI */
            private String printName;

            SubCategory( String printName )
            {
                this.printName = printName;
            }

            SubCategory(){

                this.printName = "";
            }
            public String printName()
            {
                return printName;
            }
            public void setName(String name) {
                this.printName = name;
            }
        }

        public static class MainCategory {


            /** Name of category as it appears in the GUI */
            private String printName;
            private int arrayMax;
            /** List of all subcategories that belong to this category */
            private SubCategory[] children;
            int index;

            MainCategory( String printName, SubCategory[] children )
            {
                this.printName = printName;
                this.children = children;
                index = 0;
                arrayMax = 30;
                children = new SubCategory[30];
            }
            MainCategory(String printName) {
                this.printName = printName;
                children = new SubCategory[arrayMax];
                index = 0;
                arrayMax = 30;
                children = new SubCategory[30];

            }
            MainCategory(){
                this.printName = "Undefined";
                children = null;
                index = 0;
                arrayMax = 30;
                children = new SubCategory[30];

            }
            public String printName()
            {
                return printName;
            }

            public SubCategory[] children()
            {
                return children;
            }

            public void setName(String name) {

                this.printName = name;
            }
            public String getName(){

                return this.printName;
            }

            public void addNewSubcategory(SubCategory sub) {
                children[index] = sub;
                index++;
                if(index == arrayMax) {
                    return;
                }
            }
            public void addNewSubcategory(String name) {
                children[index] = new SubCategory(name);
                index++;
                if(index == arrayMax) {
                    return;
                }
            }
            public int size(){
                return index;
            }


        }
    }


