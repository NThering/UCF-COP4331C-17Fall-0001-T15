package com.remaclek.kelcamer.academicarticlemanager;

/**
 * Created by Kelsey on 10/31/17.
 */

public class SubCategory
{
    /** Name of subcategory as it appears in the GUI */
    private String printName;
    private int categoryID;
    SubCategory( String printName )
    {
        this.printName = printName;
        categoryID = 0;

    }

    SubCategory(){

        this.printName = "";
        categoryID = 0;

    }
    public void setID(int num){
        this.categoryID = num;
    }
    public int getID(){
        return categoryID;
    }
    public String printName()
    {
        return printName;
    }
    public void setName(String name) {
        this.printName = name;
    }
}