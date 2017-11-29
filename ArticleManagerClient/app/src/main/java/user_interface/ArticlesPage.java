package user_interface;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import team15.articlemanagerclient.R;
import am_utils.ArticleInfo;
//import networking.Public;
import list_builder.Public;

public class ArticlesPage extends AppCompatActivity {

    // UI Variables
    ListView lv;
    ArrayList<String> articles = new ArrayList<>();
    ArrayList<ArticleInfo> articleTitles;
    ArrayAdapter<String> adapter;
    TextView title;
    Button downloadAllArticles, downloadUnderCategory;
    Integer mainId, subId;
    String data, message;
    ProgressDialog prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_page);

        // Pull the category passed in through intent
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        message = bundle.getString("messageSub");
        mainId = bundle.getInt("mainCatId");
        subId = bundle.getInt("subCatId");

        // Initialize ProgressDialog
        prog = new ProgressDialog(ArticlesPage.this);
        prog.setMessage("loading");
        prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prog.setCancelable(false);
        prog.setCanceledOnTouchOutside(false);

        title = (TextView) findViewById(R.id.articleTextView);
        title.setText(message + " Articles");

        // Dynamic list
        lv = (ListView) findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, articles);
        lv.setAdapter(adapter);

      /*  new Thread() {
            public void run() {
                try {
                    prog.show();
                    //articleTitles = Public.getArticlesFromCategory(mainId, subId, false); NEEDS NETWORKING TO WORK
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            prog.dismiss();
                        }
                    });
                } catch(final Exception e) {

                }
            }
        }.start(); */

        addCategories();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                data = (String)parent.getItemAtPosition(position);
                Intent newActivity = new Intent(ArticlesPage.this, ArticlePreview.class);
                newActivity.putExtra("titleMessage", data);
                newActivity.putExtra("mainCatId", mainId);
                newActivity.putExtra("subCatId", subId);
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
                /*  new Thread() {
            public void run() {
                try {
                    prog.show();
                    Public.BuildDatabaseOverview;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            prog.dismiss();
                        }
                    });
                } catch(final Exception e) {

                }
            }
        }.start(); */
            }
        });

        // Listener for button
        downloadUnderCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Downloads the articles under this subcategory
                /*  new Thread() {
            public void run() {
                try {
                    prog.show();
                    Public.BuildDetailedCategoryListing(); // FIX PARAMS AFTER NOAH DOES
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            prog.dismiss();
                        }
                    });
                } catch(final Exception e) {

                }
            }
        }.start(); */
            }
        });
    }

    // Testing method until I can pull categories from database
    public void addCategories() {
       /* for(ArticleInfo listyList : articleTitles) { WON'T WORK WITHOUT NETWORKING
            articles.add(listyList.printName);
        } */

        articles.add("Dog");
        articles.add("Cat");
        articles.add("Cow");
        articles.add("Pig");
        articles.add("Chicken");
        articles.add("Creeper");
        articles.add("Spider");
        articles.add("Animal");
        articles.add("Default");
        adapter.notifyDataSetChanged();
    }
}
