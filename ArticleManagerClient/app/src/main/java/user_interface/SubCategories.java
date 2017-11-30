package user_interface;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import am_utils.SubCategory;
import team15.articlemanagerclient.R;
import networking.Public;

public class SubCategories extends AppCompatActivity {

    // UI variables
    ListView lv;
    ArrayList<String> subcategories = new ArrayList<>();
    ArrayList<CatContainer> finalCatList;
    ArrayAdapter<String> adapter;
    TextView title;
    String data, message, userName;
    Button downloadAllArticles, downloadUnderCategory;
    Integer mainId, subId;
    ProgressDialog prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categories);

        // Get category passed in on intent
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        message = bundle.getString("message");
        userName = bundle.getString("uName");
        finalCatList = (ArrayList<CatContainer>) bundle.getSerializable("final_cat_list");

        // Initialize ProgressDialog
        prog = new ProgressDialog(SubCategories.this);
        prog.setMessage("loading");
        prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prog.setCancelable(false);
        prog.setCanceledOnTouchOutside(false);

        title = (TextView) findViewById(R.id.subTextView);
        title.setText(message + " Sub Categories");
        mainId = getMainId(message);

        // Dynamic list
        lv = (ListView) findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subcategories);
        lv.setAdapter(adapter);
        addCategories();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                data = (String)parent.getItemAtPosition(position);
                subId = getSubId(message, data);
                Intent newActivity = new Intent(SubCategories.this, ArticlesPage.class);
                newActivity.putExtra("messageSub", data);
                newActivity.putExtra("mainCatId", mainId);
                newActivity.putExtra("subCatId", subId);
                newActivity.putExtra("uName", userName);
                startActivity(newActivity);
            }
        });

        downloadAllArticles = (Button) findViewById(R.id.downloadAll);
        downloadUnderCategory = (Button) findViewById(R.id.downloadArticles);

        // Listener for button
        downloadAllArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // Downloads ALL THE THINGS!!!!!

            File file = list_builder.Public.BuildDatabaseOverview(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());

            }
        });

        // Listener for button
        downloadUnderCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // Downloads the articles under this category

            File file = list_builder.Public.BuildDetailedCategoryListing(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(), mainId);

            }
        });
    }

    // Testing method until I can pull categories from database
    public void addCategories() {
        for(CatContainer category : finalCatList) {
            if(!category.name.equals(message))
                continue;

            for(SubCatContainer subCategory : category.subcategories) {
                subcategories.add(subCategory.name);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public int getMainId(String mainCat) {
        for(CatContainer category : finalCatList) {
            if (!category.name.equals(mainCat))
                continue;

            if(category.name.equals(mainCat))
                return category.id;
        }

        return -1; // Shouldn't ever hit -1; that's the assumption
    }

    public int getSubId(String mainCat, String subCat) {
        for(CatContainer category : finalCatList) {
            if(!category.name.equals(mainCat))
                continue;

            for(SubCatContainer subCategory : category.subcategories) {
                if(!subCategory.name.equals(subCat))
                    continue;

                if(subCategory.name.equals(subCat))
                    return subCategory.id;
            }
        }

        return -1; // Shouldn't ever hit -1; that's the assumption
    }
}
