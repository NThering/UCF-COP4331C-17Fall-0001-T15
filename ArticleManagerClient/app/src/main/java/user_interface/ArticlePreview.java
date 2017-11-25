package user_interface;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import in.gauriinfotech.commons.Commons;
import team15.articlemanagerclient.R;

public class ArticlePreview extends AppCompatActivity {

    // UI variables
    TextView title, categories, authors, dateUploaded, uploader;
    Button download, viewButton, deleteButton, reupload;
    private static final int fileSelectCode = 42; // For filepicker, can be any number I believe
    String fullpath; // Universal so I can call from the inner classes/methods
    EditText filePath; // Universal so I can call from the inner classes/methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_preview);

        // Initialize the elements
        title = (TextView) findViewById(R.id.articleTitleTextView);
        categories = (TextView) findViewById(R.id.articleCategoriesTextView);
        authors = (TextView) findViewById(R.id.authorsTextView);
        dateUploaded = (TextView) findViewById(R.id.dateUploadedTextView);
        uploader = (TextView) findViewById(R.id.uploaderTextView);

        download = (Button) findViewById(R.id.downloadArticleButton);
        viewButton = (Button) findViewById(R.id.viewArticleButton);
        deleteButton = (Button) findViewById(R.id.deleteArticleButton);
        reupload = (Button) findViewById(R.id.reuploadArticleButton);

        // Hard code until I get real code
        title.setText("Why Ian is the best");
        categories.setText("Philosophy, Life");
        authors.setText("Ian Holdeman");
        dateUploaded.setText("Date Uploaded: 10/28/17");
        uploader.setText("Uploaded by: admin");

        // If you uploaded the article, get access to these two buttons
        if(uploader.getText().toString().equals("Uploaded by: admin")) {
            deleteButton.setVisibility(View.VISIBLE);
            reupload.setVisibility(View.VISIBLE);
        }

        // Otherwise you can't see the buttons
        else {
            deleteButton.setVisibility(View.INVISIBLE);
            reupload.setVisibility(View.INVISIBLE);
        }

        // Listener for the download button
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call download popup
                callDownloadPopup(v);
            }
        });

        // Listener to call article view
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // View button
            }
        });

        // Listener for delete button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete
            }
        });

        // Listener for upload button
        reupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call upload popup
                callUploadPopup(v);
            }
        });
    }

    // Download popup
    public void callDownloadPopup(View v) {
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_download_screen, null);

        final PopupWindow window = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        window.showAtLocation(v, Gravity.CENTER, 0, 0);

        // Instance variables -- Haven't done the individual file format buttons yet
        Button downloadButton = (Button) popupView.findViewById(R.id.downloadArticleButton);

        window.setFocusable(true);
        window.update();

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Works", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Upload popup
    public void callUploadPopup(View v) {
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_upload_screen, null);

        final PopupWindow window = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        window.showAtLocation(v, Gravity.CENTER, 0, 0);

        // Instance variables
        Button browseButton = (Button) popupView.findViewById(R.id.browseButton);
        Button uploadButton = (Button) popupView.findViewById(R.id.uploadButton);
        filePath = (EditText) popupView.findViewById(R.id.filepath);

        window.setFocusable(true);
        window.update();

        // Listener for the browse button
        browseButton.setOnClickListener(new View.OnClickListener() {
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
        uploadButton.setOnClickListener(new View.OnClickListener() {
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
