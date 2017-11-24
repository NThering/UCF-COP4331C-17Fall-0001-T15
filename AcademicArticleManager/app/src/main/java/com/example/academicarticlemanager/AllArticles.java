package com.example.academicarticlemanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

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
        addCategories();

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

    // Testing method until I can pull categories from database
    public void addCategories() {
        categories.add("CS");
        categories.add("Math");
        categories.add("Reading");
        categories.add("History");
        categories.add("Literature");
        categories.add("Science");
        categories.add("Astronomy");
        categories.add("Philosophy");
        categories.add("Databases");
        categories.add("Life");
        categories.add("Help");
        categories.add("Out");
        categories.add("Of");
        categories.add("Ideas");
        categories.add("Send");
        categories.add("Help");
        categories.add("Please");
        adapter.notifyDataSetChanged();
    }
}
