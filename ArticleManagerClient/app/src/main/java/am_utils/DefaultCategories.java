package am_utils;

import java.io.File;
/**
 * Created by Kelsey on 10/31/17.
 */

public class DefaultCategories {

     private MainCategory[] mainCat;
     private int index;
     String test = "";
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
             "Economics",           // 13
             "Linguistics",         // 14
             "Political Science",   // 15
             "Psychology",          // 16
             "Health",              // 17
             "Other",               // 18

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


        String[] physicsList = {

                "Acoustics",
                "Agrophysics",
                "Astrophysics",
                "Astronomy",
                "Mechanics",
                "Nuclear",
                "Particle",
                "Quantum",
                "Statics",
                "Theoretical",

        };
        mainCat[0].addNewSubcategory(physicsList);


        // food list
       String[] chemList = {
               "Physical",
               "Ocean",
               "Organic",
               "Inorganic",
               "Nuclear",
               "Analytical",
       };

        mainCat[1].addNewSubcategory(chemList);



        String[] ecologyList = {
                "Animals",
                "Insects",
                "Microbial",
                "Paleoecology",
                "Plants",

        };
        mainCat[2].addNewSubcategory(ecologyList);


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



        String[] metList = {
                "Microscale",
                "Mesoscale",
                "Synoptic",

        };
        mainCat[4].addNewSubcategory(metList);




        String[] bioList = {

                "Anatomy",
                "Biomechanics",
                "Biodiversity",
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
                "Computer",
                "Chemical",
                "Civil",
                "Electrical",
                "Photonics",
                "Mechanical",
                "Systems",

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
                "Artificial Intelligence",
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

                "Macro",
                "Micro",
                "Bio",
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
                "Public Administration",

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


        String[] otherList={
                "Other",

        };
        mainCat[18].addNewSubcategory(otherList);



        // Always Update
        index = 18;




    }

        public ArticleInfo categorize(File uploadedFile){

             ArticleInfo filledArticle = new ArticleInfo(0);

            FileConverter convert = new FileConverter();
            File pdfFile = convert.convertToPDF(uploadedFile);


            for(int z = 1; z < 5; z++){
            String data = convert.extractTextFromPDF(pdfFile.getName(), z);


            for(int x = 0; x < this.index; x++){
               if(data.contains(mainCat[x].printName()) || uploadedFile.getName().contains(mainCat[x].printName())) {
                   SubCategory[] children = mainCat[x].children();
                   filledArticle.setMainCategoryIndex(x);

                   for (int y = 0; y < mainCat[x].size(); y++) {
                       if (data.contains(children[y].printName())
                               || uploadedFile.getName().contains(children[y].printName())
                               || data.contains(checkModifiedWord(children[y].printName()))) {

                           test = mainCat[x].printName() + " " + children[y].printName();
                           filledArticle.setSubCategoryIndex(y);

                           filledArticle = addExtraData(filledArticle, data);
                           return filledArticle;

                       }


                   }


               }
                }


            }



            return filledArticle;
        }
        private ArticleInfo addExtraData(ArticleInfo article, String data){

            FileConverter convert = new FileConverter();
            article.setDoiNumber(convert.getDoiFromText(data));
            article.setAbstractText(convert.getAbstractFromText(data));
            article.setAuthor(convert.getAuthorFromText(data));
            return article;
        }

        public int size(){
            return index;
        }
        private String checkModifiedWord(String word){
            if(word.contains("ology")){
                word = word.substring(0, word.indexOf("ology"));
                word += "ological";

            }


            if(word.contains("s")){
                word = word.substring(0, word.lastIndexOf("s"));
            }


            if(word.contains("aphy")){
                word = word.substring(0, word.indexOf("aphy"));
                word += "aphical";
            }
            if(word.contains(" ")){
                word.replace(" ", "");
            }
            return word;

        }
    public MainCategory[] getDefaultCategories(){
        return mainCat;
    }

}
