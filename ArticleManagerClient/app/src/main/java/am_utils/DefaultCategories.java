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

             "Physics",             // 0
             "Chemistry",           // 1
             "Ecology",             // 2
             "Geology",             // 3
             "Meteorology",         // 4
             "Biology",             // 5
             "Botany",              // 6
             "Mathematics",         // 7
             "Engineering",         // 8
             "Medicine",            // 9
             "Computer Science",    // 10
             "Anthropology",        // 11
             "Business",            // 12
             "Linguistics",         // 13
             "Political Science",   // 14
             "Psychology",          // 15
             "Health",              // 16
             "Other",               // 17

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
        String[] physicsList = {

                "Acoustics",
                "Agrophysics",
                "Astrophysics",
                "Astronony",
                "Mechanics",
                "Nuclear physics",
                "Particle physics",
                "Quantum physics",
                "Statics",
                "Theoretical physics",

        };
        mainCat[0].addNewSubcategory(physicsList);


        // food list
       String[] chemList = {
               "Physical Chemistry",
               "Ocean Chemistry",
               "Organic Chemistry",
               "Inorganic Chemistry",
               "Nuclear Chemistry",
               "Analytical Chemistry",
       };

        mainCat[1].addNewSubcategory(chemList);


        // People
        String[] ecologyList = {
                "Animals",
                "Insects",
                "Microbial",
                "Paleoecology",
                "Plants",

        };
        mainCat[2].addNewSubcategory(ecologyList);

        // technology
        String[] geologyList = {
                "Geochemistry",
                "Mineralogy",
                "Paleontology",
                "Volcanology",
                "Stratigraphy",
                "Geomorphology",
                "Geophysics",
                "Marine Geology",

        };
        mainCat[3].addNewSubcategory(geologyList);


        // Animals
        String[] metList = {
                "Microscale meteorology",
                "Mesoscale meteorology",
                "Synoptic scale meteorology",

        };
        mainCat[4].addNewSubcategory(metList);


        // Social

        String[] bioList = {

                "Anatomy",
                "Biomechanics",
                "Biochemistry",
                "Biogeography",
                "Biophysics",
                "Biotechnology",
                "Chronobiology",
                "Cryobiology",
                "Ecology",
                "Genetics",
                "Microbiology",
                "Physiology",
                "Marine",
        };
        mainCat[5].addNewSubcategory(bioList);


        // Travel

        String[] botanyList={

                "Agronomy",
                "Bryology",
                "Dendrology",
                "Ethnobotany",
                "Lichenology",
                "Mycology",
                "Palynology",
                "Phycology",
                "Phytosociology",
                "Pteridology",

        };
        mainCat[6].addNewSubcategory(botanyList);



        // Life
        String[] mathList = {
                "Arithmetic",
                "Geometry",
                "Number theory",
                "Trigonometry",
                "Topology",
                "Calculus",
                "Differential Equations",
                "Analysis",
                "Category theory",
                "Set theory",
                "Type theory",
                "Combinatorics",
                "Cryptography",

        };
        mainCat[7].addNewSubcategory(mathList);



        String[] engList = {
                "Computer Engineering",
                "Chemical Engineering",
                "Civil Engineering",
                "Electrical Engineering",
                "Photonics Engineering",
                "Mechanical Engineering",
                "Systems Engineering",

        };
        mainCat[8].addNewSubcategory(engList);


        String[] medList = {
                "Angiology",
                "Cardiology",
                "Endocrinology",
                "Gastroenterology",
                "Geriatrics",
                "Hematology",
                "Hepatology",
                "Infectious disease",
                "Nephrology",
                "Neurology",
                "Oncology",
                "Pediatrics",
                "Pulmonology",
                "Rheumatology",
                "Sports Medicine",
        };
        mainCat[9].addNewSubcategory(engList);


        String[] csList = {
                "Data structures",
                "Algorithms",
                "Theory",
                "Information",
                "Programming",
                "Formal",
                "Computer architecture",
                "Computer performance",
                "Concurrent, parallel and distributed systems",
                "Computer networks",
                "Computer security",
                "Cryptography",
                "Databases",
                "Graphics",
                "Human-computer interaction",
                "Scientific computing",
                "Artificial intelligence",
        };
        mainCat[10].addNewSubcategory(csList);



        String[] anthroList={

                "Archaeology",
                "Ethnobiology",
                "Ethnobotany",
                "Ethnology",
                "Zooarchaeology",
                "Anthrozoology",

        };
        mainCat[11].addNewSubcategory(anthroList);



        String[] busList={

                "Accountancy",
                "Finance",
                "Management",
                "Marketing",

        };
        mainCat[12].addNewSubcategory(busList);


        String[] economList={

                "Macroeconomics",
                "Microeconomics",
                "Bioeconomics",
                "Public Finance",
                "Socioeconomics",
                "Neuroeconomics",

        };
        mainCat[13].addNewSubcategory(economList);


        String[] lingList={

                "Cognitive",
                "Dialectology",
                "Etymology",
                "Geolinguistics",
                "Morphology",
                "Neurolinguistics",
                "Phonetics",
                "Pragmatics",
                "Semantics",

        };
        mainCat[14].addNewSubcategory(lingList);


        String[] politList={

                "Game theory",
                "Geopolitics",
                "Ideology",
                "Psephology",
                "Public administration",

        };
        mainCat[15].addNewSubcategory(politList);


        String[] psychList={

                "Psychometrics",
                "Clinical",
                "Neuropsychology",
                "Psychophysics",

        };
        mainCat[16].addNewSubcategory(psychList);


        String[] healthList={
                "Health",
                "Nutrition",
                "Excersise",

        };
        mainCat[17].addNewSubcategory(healthList);
        // Other has no subcategories and will be defined separately
        String other = "Other";
        mainCat[8].addNewSubcategory(other);






        // Always Update
        index = 8;




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
