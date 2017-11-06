package am_utils;

/**
 * Created by Kelsey on 10/31/17.
 */

public class MainCategory {


    /** Name of category as it appears in the GUI */
    private String printName;
    private int arrayMax = 30;
    /** List of all subcategories that belong to this category */
    private SubCategory[] children;
    int index;
    int categoryID;

    MainCategory( String printName, SubCategory[] children )
    {
        this.printName = printName;
        this.children = children;
        index = 0;
        children = new SubCategory[arrayMax];
        categoryID = 0;
    }
    MainCategory(String printName) {
        this.printName = printName;
        children = new SubCategory[arrayMax];
        index = 0;
        categoryID = 0;


    }
    MainCategory(){
        this.printName = "Undefined";
        children = null;
        index = 0;
        children = new SubCategory[arrayMax];
        categoryID = 0;


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
    public void setID(int num){
        this.categoryID = num;
    }
    public int getID(){
        return categoryID;
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
