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
import android.widget.Toast;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import networking.Public;
import team15.articlemanagerclient.R;
import am_utils.ArticleInfo;
import am_utils.CUtils;
import am_utils.DefaultCategories;
import am_utils.MainCategory;
import am_utils.SubCategory;

// Format for categories that's easier to enumerate through.
class CatContainer implements Serializable {
    public String name;
    public Integer id;
    public ArrayList<SubCatContainer> subcategories;
}

// Format for sub-categories that's easier to enumerate through.
// Only really need this so we can have ID without using null entries.
class SubCatContainer implements Serializable {
    public String name;
    public Integer id;
}

public class AllArticles extends AppCompatActivity {
    // UI variables
    ListView lv;
    ArrayList<String> categories = new ArrayList<>();
    ArrayList<CatContainer> finalCatList = new ArrayList<CatContainer>();
    ArrayAdapter<String> adapter;
    String data;
    Button downloadAllArticles;
    ProgressDialog prog;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_articles);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        userName = bundle.getString("uName");

        // Initialize ProgressDialog
        prog = new ProgressDialog(AllArticles.this);
        prog.setMessage("loading");
        prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prog.setCancelable(false);
        prog.setCanceledOnTouchOutside(false);

        // Dynamic list view
        lv = (ListView) findViewById(R.id.list);
        // Adapter to make it dynamic
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories);
        lv.setAdapter(adapter);
        browseCategories();

        // Send title of category to next activity when clicked
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                data = (String)parent.getItemAtPosition(position);
                Intent newActivity = new Intent(AllArticles.this, SubCategories.class);
                newActivity.putExtra("message", data);
                newActivity.putExtra("final_cat_list", finalCatList);
                newActivity.putExtra("uName", userName);
                startActivity(newActivity);
            }
        });

        downloadAllArticles = (Button) findViewById(R.id.downloadAll);

        // Listener for downloadAll button
        downloadAllArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Download EVERYTHING!!!!!!!
                new Thread() {
                    public void run() {
                        try {
                            prog.show();
                            File file = list_builder.Public.BuildDatabaseOverview(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    prog.dismiss();
                                }
                            });
                        } catch(final Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Something happened...", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }.start();

                Toast.makeText(getApplicationContext(), "Yes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Adds category to the list
    public void addCategories(String catName) {
        categories.add(catName);
        adapter.notifyDataSetChanged();
    }

    public void browseCategories() {
        DefaultCategories defaultCat = new DefaultCategories();
        convertToArrayLists(defaultCat.getDefaultCategories(), defaultCat.size());
    }

    void convertToArrayLists(MainCategory[] categoryList, int categoryListSize) {
        for(int i = 0; i < categoryListSize; i++) {
            if ( categoryList[i] == null )
                continue;

            CatContainer newCat = new CatContainer();
            newCat.name = categoryList[i].getName();
            newCat.subcategories = new ArrayList<SubCatContainer>();
            newCat.id = i;
            finalCatList.add(newCat);
            addCategories(categoryList[i].getName());

            for(int j = 0; j < categoryList[i].size(); j++) {
                // Same story as before.
                if (categoryList[i].children()[j] == null)
                    continue;

                SubCatContainer newSubCat = new SubCatContainer();
                newSubCat.name = categoryList[i].children()[j].printName();
                newSubCat.id = j;

                newCat.subcategories.add(newSubCat);
            }
        }


    }
}
