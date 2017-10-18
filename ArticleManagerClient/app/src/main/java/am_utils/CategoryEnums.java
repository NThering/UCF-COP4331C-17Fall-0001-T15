package am_utils;

import java.util.ArrayList;

public final class CategoryEnums {

    public enum SubCategory
    {
        // Fill out as needed.  Ensure synch between client and server!
        GEO_MOUNTAINS ( "Mountains" ),
        GEO_RIVERS ( "Rivers" ),
        GEO_LAKES ( "Lakes" ),
        MED_THERAPY ( "Therapy" ),
        MED_SURGERY ( "Surgery" ),
        MED_DENTAL ( "Dental" );

        /** Name of subcategory as it appears in the GUI */
        private final String printName;

        SubCategory( String printName )
        {
            this.printName = printName;
        }

        public String PrintName()
        {
            return printName;
        }
    }

    public enum MainCategory
    {
        GEOLOGY ( "Geology", new SubCategory[]{ SubCategory.GEO_MOUNTAINS, SubCategory.GEO_RIVERS, SubCategory.GEO_LAKES } ),
        MEDICINE ( "Medicine", new SubCategory[]{ SubCategory.MED_THERAPY, SubCategory.MED_SURGERY, SubCategory.MED_DENTAL } );


        /** Name of category as it appears in the GUI */
        private final String printName;

        /** List of all subcategories that belong to this category */
        private final SubCategory[] children;

        MainCategory( String printName, SubCategory[] children )
        {
            this.printName = printName;
            this.children = children;
        }

        public String PrintName()
        {
            return printName;
        }

        public SubCategory[] Children()
        {
            return children;
        }
    }
}