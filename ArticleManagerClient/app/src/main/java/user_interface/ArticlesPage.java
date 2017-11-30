package user_interface;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.os.StrictMode;
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
import team15.articlemanagerclient.R;
import am_utils.ArticleInfo;
//import networking.Public;
import list_builder.Public;

public class ArticlesPage extends AppCompatActivity {

    // UI Variables
    ListView lv;
    ArrayList<String> articles = new ArrayList<>();
    ArrayList<ArticleInfo> articleTitles = new ArrayList<>();
    ArrayAdapter<String> adapter;
    TextView title;
    Button downloadAllArticles, downloadUnderCategory;
    Integer mainId, subId;
    String data, message, userName;
    ProgressDialog prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_page);

        // Pull the category passed in through intent
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        message = bundle.getString("messageSub");
        mainId = bundle.getInt("mainCatId");
        subId = bundle.getInt("subCatId");
        userName = bundle.getString("uName");

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

        articleTitles = networking.Public.getArticlesFromCategory(mainId, subId, false);


        addCategories();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                data = (String)parent.getItemAtPosition(position);
                Intent newActivity = new Intent(ArticlesPage.this, ArticlePreview.class);
                newActivity.putExtra("titleMessage", data);
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
            // Downloads the articles under this subcategory
            new Thread() {
                public void run() {
                    try {
                        prog.show();
                        File file = list_builder.Public.BuildDetailedCategoryListing(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(), mainId);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                prog.dismiss();
                            }
                        });
                    } catch(final Exception e) {

                    }
                }
            }.start();
            }
        });
    }

    public void addCategories() {
        for(ArticleInfo listyList : articleTitles) {
            articles.add(listyList.printName);
        }
    }
}
