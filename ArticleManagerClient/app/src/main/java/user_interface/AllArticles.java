package user_interface;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import team15.articlemanagerclient.R;
import am_utils.ArticleInfo;
import am_utils.CUtils;
import am_utils.DefaultCategories;
import am_utils.MainCategory;
import am_utils.SubCategory;


public class AllArticles extends AppCompatActivity {
    // UI variables
    ListView lv;
    ArrayList<String> categories = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String data;
    Button downloadAllArticles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_articles);

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
                startActivity(newActivity);
            }
        });

        downloadAllArticles = (Button) findViewById(R.id.downloadAll);

        // Listener for downloadAll button
        downloadAllArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Download EVERYTHING!!!!!!!
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
        convertToArrayLists( defaultCat.getDefaultCategories(), defaultCat.size());
    }

    void convertToArrayLists( MainCategory[] categoryList, int categoryListSize ) {
        for(int i = 0; i < categoryListSize; i++) {
            if ( categoryList[i] == null )
                continue;

            addCategories(categoryList[i].getName());
        }
    }
}
