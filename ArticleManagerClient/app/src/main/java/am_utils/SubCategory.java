package com.remaclek.kelcamer.academicarticlemanager;

/**
 * Created by Kelsey on 10/31/17.
 */

public class SubCategory
{
    /** Name of subcategory as it appears in the GUI */
    private String printName;
    private int categoryID;
    private int index;
    private MainCategory parent;

    private ArticleInfo[] listOfArticles;
    SubCategory( String printName )
    {
        this.parent = null;
        this.printName = printName;
        categoryID = 0;
        listOfArticles = new ArticleInfo[50];
        index = 0;
    }

    SubCategory(){
        this.parent = null;
        this.printName = "";
        categoryID = 0;
        listOfArticles = new ArticleInfo[50];
        index = 0;

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
    public void addArticle(ArticleInfo article){

        listOfArticles[index] = article;
        index++;

    }
    public int size(){
        return index;
    }
    public void setParent(MainCategory par){
        this.parent = par;
    }
}