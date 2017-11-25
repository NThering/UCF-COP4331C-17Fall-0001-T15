package user_interface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import in.gauriinfotech.commons.Commons;
import team15.articlemanagerclient.R;

public class MyArticles extends AppCompatActivity {

    ListView lv;
    ArrayList<String> subcategories = new ArrayList<>();
    ArrayAdapter<String> adapter;
    TextView username, userTitle;
    Button upload, logout;
    private static final int fileSelectCode = 42; // For filepicker, can be any number I believe
    String fullpath; // Universal so I can call from the inner classes/methods
    EditText filePath; // Universal so I can call from the inner classes/methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_articles);

        username = (TextView) findViewById(R.id.usernameTextView);
        userTitle = (TextView) findViewById(R.id.userTitleTextView);

        // Hard code until I can pull from database
        username.setText("Ian");
        userTitle.setText("Da God");

        lv = (ListView) findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subcategories);
        lv.setAdapter(adapter);
        addCategories();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newActivity = new Intent(MyArticles.this, ArticlePreview.class);
                startActivity(newActivity);
            }
        });

        upload = (Button) findViewById(R.id.uploadArticle);
        logout = (Button) findViewById(R.id.logoutButton);

        // Upload article
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPopup(v);
            }
        });

        // Logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyArticles.this, TitleScreenLoggedOut.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                SharedPreferences preference = getSharedPreferences("loggedIn", MODE_PRIVATE);
                preference.edit().remove("logged").commit();
                startActivity(intent);
            }
        });
    }

    // Hard coded categories
    public void addCategories() {
        subcategories.add("The");
        subcategories.add("memes");
        subcategories.add("are");
        subcategories.add("too");
        subcategories.add("spicy");
        subcategories.add("for");
        subcategories.add("this");
        adapter.notifyDataSetChanged();
    }

    // Calls the upload window
    public void callPopup(View v) {
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_upload_screen, null);

        final PopupWindow window = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        window.showAtLocation(v, Gravity.CENTER, 0, 0);

        // Instance variables
        TextView title = (TextView) popupView.findViewById(R.id.fileTextView);
        filePath = (EditText) popupView.findViewById(R.id.filepath);
        Button browse = (Button) popupView.findViewById(R.id.browseButton);
        Button upload = (Button) popupView.findViewById(R.id.uploadButton);

        window.setFocusable(true);
        window.update();

        // Listener for the browse button
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open file picker
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*"); // All file types -- will specify later
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "Select a file to upload"), fileSelectCode);
            }
        });

        // Listener for the upload button
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Will call the upload file method from networking I believe
                // Also need to learn more about our server
            }
        });
    }

    // Method to set filepath once a file is picked
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) { // Prevents crashing when back button is pressed
            Uri path = data.getData();

            fullpath = Commons.getPath(path, getApplicationContext());
            filePath.setText(fullpath);
        }
    }

}
