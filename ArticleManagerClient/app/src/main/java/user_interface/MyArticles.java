package user_interface;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import am_utils.ArticleInfo;
import am_utils.MainCategory;
import in.gauriinfotech.commons.Commons;
import team15.articlemanagerclient.R;
import networking.Public;
import proccessing.PublicUsage;
import am_utils.DefaultCategories;

public class MyArticles extends AppCompatActivity {

    ListView lv;
    ArrayList<String> subcategories = new ArrayList<>();
    ArrayAdapter<String> adapter;
    TextView username, userTitle;
    Button upload, logout;
    private static final int fileSelectCode = 42; // For filepicker, can be any number I believe
    private static final int permissionRequestCode = 43;
    String fullpath; // Universal so I can call from the inner classes/methods
    EditText filePath; // Universal so I can call from the inner classes/methods
    DefaultCategories defaultCat = new DefaultCategories();
    Uri path;
    ProgressDialog prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_articles);

        // Initialize ProgressDialog
        prog = new ProgressDialog(MyArticles.this);
        prog.setMessage("loading");
        prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prog.setCancelable(false);
        prog.setCanceledOnTouchOutside(false);

        username = (TextView) findViewById(R.id.usernameTextView);
        userTitle = (TextView) findViewById(R.id.userTitleTextView);

        // Hard code until I can pull from database -- DEAL WITH THIS IAN
        username.setText("Ian");

        lv = (ListView) findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subcategories);
        lv.setAdapter(adapter);
        addCategories();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = (String)parent.getItemAtPosition(position);
                Intent newActivity = new Intent(MyArticles.this, ArticlePreview.class);
                newActivity.putExtra("messageSub", data);
                newActivity.putExtra("mainCatId", 1); // CHANGE 1 WHEN WE TRACK USERNAME
                newActivity.putExtra("subCatId", 1); // CHANGE 1 WHEN WE TRACK USERNAME
                startActivity(newActivity);
            }
        });

        upload = (Button) findViewById(R.id.uploadArticle);
        logout = (Button) findViewById(R.id.logoutButton);

        // Upload article
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                    requestPermission();
                }
                else
                    callPopup(v);
            }
        });

        // Logout
        logout.setOnClickListener(new View.OnClickListener() {
            int log = -1;

            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        try {
                        prog.show();
                        log = Public.logout();
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

                if(log == 0) { // Might remove some of this when Networking is up TBD
                    Intent intent = new Intent(MyArticles.this, TitleScreenLoggedOut.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    SharedPreferences preference = getSharedPreferences("loggedIn", MODE_PRIVATE);
                    preference.edit().remove("logged").commit();
                    startActivity(intent);
                }

                else
                    Toast.makeText(getApplicationContext(), "Failed to log out", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat
                    .requestPermissions(MyArticles.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, permissionRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case permissionRequestCode:
                break;

            default: super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // Hard coded categories -- CHANGE WHEN NETWORKING IS UP
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

        // Listener for the browse button -- opens the file picker
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
                // Categorize the file then upload it -- TEST THESE
                final File file = new File(filePath.getText().toString());

                final ArticleInfo info = PublicUsage.categorize(file, GetMainCategoryArray(), GetMainCategoryArraySize(), MyArticles.this );

                new Thread() {
                    public void run() {
                        try {
                            prog.show();
                            Public.uploadArticle(file, info);
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

    // Method to set filepath once a file is picked -- set globals to file path when file is picked
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) { // Prevents crashing when back button is pressed
            path = data.getData();

            fullpath = Commons.getPath(path, getApplicationContext());
            filePath.setText(fullpath);
        }
    }

    public int GetMainCategoryArraySize() { return defaultCat.size(); }

    public MainCategory[] GetMainCategoryArray() { return defaultCat.getDefaultCategories(); }

}
