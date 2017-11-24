package com.example.academicarticlemanager;

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

public class ArticlesPage extends AppCompatActivity {

    // UI Variables
    ListView lv;
    ArrayList<String> subcategories = new ArrayList<>();
    ArrayAdapter<String> adapter;
    TextView title;
    Button downloadAllArticles, downloadUnderCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_page);

        // Pull the category passed in through intent
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String message = bundle.getString("messageSub");

        title = (TextView) findViewById(R.id.articleTextView);
        title.setText(message + " Articles");

        // Dynamic list
        lv = (ListView) findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subcategories);
        lv.setAdapter(adapter);
        addCategories();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent newActivity = new Intent(ArticlesPage.this, ArticlePreview.class);
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
                // Downloads the articles under this subcategory
            }
        });
    }

    // Testing method until I can pull categories from database
    public void addCategories() {
        subcategories.add("Dog");
        subcategories.add("Cat");
        subcategories.add("Cow");
        subcategories.add("Pig");
        subcategories.add("Chicken");
        subcategories.add("Creeper");
        subcategories.add("Spider");
        subcategories.add("Animal");
        subcategories.add("Default");
        adapter.notifyDataSetChanged();
    }
}
