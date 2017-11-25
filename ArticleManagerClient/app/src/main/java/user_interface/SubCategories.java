package user_interface;

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

public class SubCategories extends AppCompatActivity {

    // UI variables
    ListView lv;
    ArrayList<String> subcategories = new ArrayList<>();
    ArrayAdapter<String> adapter;
    TextView title;
    String data;
    Button downloadAllArticles, downloadUnderCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categories);

        // Get category passed in on intent
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String message = bundle.getString("message");

        title = (TextView) findViewById(R.id.subTextView);
        title.setText(message + " Sub Categories");

        // Dynamic list
        lv = (ListView) findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subcategories);
        lv.setAdapter(adapter);
        addCategories();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                data = (String)parent.getItemAtPosition(position);
                Intent newActivity = new Intent(SubCategories.this, ArticlesPage.class);
                newActivity.putExtra("messageSub", data);
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
            }
        });

        // Listener for button
        downloadUnderCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Downloads the articles under this category
            }
        });
    }

    // Testing method until I can pull categories from database
    public void addCategories() {
        subcategories.add("Math");
        subcategories.add("Reading");
        subcategories.add("History");
        subcategories.add("CS1");
        subcategories.add("CS2");
        subcategories.add("OOP");
        subcategories.add("Intro to C");
        subcategories.add("Databases");
        subcategories.add("OS");
        subcategories.add("Programming Languages");
        subcategories.add("Meow");
        subcategories.add("I give up");
        adapter.notifyDataSetChanged();
    }
}
