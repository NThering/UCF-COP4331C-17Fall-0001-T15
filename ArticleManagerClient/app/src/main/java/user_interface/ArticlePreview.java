package user_interface;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import am_utils.ArticleInfo;
import in.gauriinfotech.commons.Commons;
import team15.articlemanagerclient.R;
import am_utils.DefaultCategories;
//import networking.Public;
import proccessing.FileConverter;

public class ArticlePreview extends AppCompatActivity {

    // UI variables
    TextView title, categories, authors, dateUploaded, uploader;
    Button download, viewButton, deleteButton, reupload;
    private static final int fileSelectCode = 42; // For filepicker, can be any number I believe
    String fullpath, message; // Universal so I can call from the inner classes/methods
    EditText filePath; // Universal so I can call from the inner classes/methods
    Integer mainId, subId;
    DefaultCategories defaultCat = new DefaultCategories();
    ArrayList<ArticleInfo> articleInformation;
    int downloadFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_preview);

        // Pull the category passed in through intent
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        message = bundle.getString("titleMessage");
        mainId = bundle.getInt("mainCatId");
        subId = bundle.getInt("subCatId");

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
        title.setText(message);
        categories.setText(getMainCategoryName(mainId) + ", " + getSubCategoryName(mainId, subId));
        //articleInformation = Public.getArticlesFromCategory(mainId, subId, false); NEEDS NETWORKING TO WORK
        //authors.setText(getAuthorName());
        //dateUploaded.setText("Date Uploaded: " + getUploadDate().format(date));
        //uploader.setText("Uploaded by: " + getUploaderName());

        // If you uploaded the article, get access to these two buttons
        /*if(getUploaderName().equals(INSERT USER NAME HERE)) {
            deleteButton.setVisibility(View.VISIBLE);
            reupload.setVisibility(View.VISIBLE);
        }

        // Otherwise you can't see the buttons
        else {
            deleteButton.setVisibility(View.INVISIBLE);
            reupload.setVisibility(View.INVISIBLE);
        } */

        // Listener for the download button
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call download popup
                downloadFlag = -1;
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
        final Button htmlButton = (Button) popupView.findViewById(R.id.htmlArticleButton);
        final Button pdfButton = (Button) popupView.findViewById(R.id.pdfArticleButton);
        final Button odtButton = (Button) popupView.findViewById(R.id.odtArticleButton);
        final Button txtButton = (Button) popupView.findViewById(R.id.txtArticleButton);

        window.setFocusable(true);
        window.update();

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(downloadFlag != 1 && downloadFlag != 2 && downloadFlag != 3 && downloadFlag != 4)
                    Toast.makeText(getApplicationContext(), "Select a file type", Toast.LENGTH_SHORT).show();

                else {
                    window.dismiss();
                    htmlButton.setBackgroundResource(R.drawable.popupbutton);
                    pdfButton.setBackgroundResource(R.drawable.popupbutton);
                    odtButton.setBackgroundResource(R.drawable.popupbutton);
                    txtButton.setBackgroundResource(R.drawable.popupbutton);

                    /*int ID = getArticleID();

                    if(ID != -1) {
                        switch (downloadFlag) {
                            case 1:
                                // FileConverter.convertFromPDF(Public.downloadArticle(ID), 3)
                                break;

                            case 2:
                                // FILE path = Public.downloadArticle(ID)
                                break;

                            case 3:
                                // FileConverter.convertFromPDF(Public.downloadArticle(ID), 2)
                                break;

                            case 4:
                                // FileConverter.convertFromPDF(Public.downloadArticle(ID), 1)
                                break;
                        }
                    } */
                }
            }
        });

        htmlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFlag = 1;
                htmlButton.setBackgroundResource(R.drawable.downloadpressed);
                pdfButton.setBackgroundResource(R.drawable.popupbutton);
                odtButton.setBackgroundResource(R.drawable.popupbutton);
                txtButton.setBackgroundResource(R.drawable.popupbutton);
            }
        });

        pdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFlag = 2;
                htmlButton.setBackgroundResource(R.drawable.popupbutton);
                pdfButton.setBackgroundResource(R.drawable.downloadpressed);
                odtButton.setBackgroundResource(R.drawable.popupbutton);
                txtButton.setBackgroundResource(R.drawable.popupbutton);
            }
        });

        odtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFlag = 3;
                htmlButton.setBackgroundResource(R.drawable.popupbutton);
                pdfButton.setBackgroundResource(R.drawable.popupbutton);
                odtButton.setBackgroundResource(R.drawable.downloadpressed);
                txtButton.setBackgroundResource(R.drawable.popupbutton);
            }
        });

        txtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFlag = 4;
                htmlButton.setBackgroundResource(R.drawable.popupbutton);
                pdfButton.setBackgroundResource(R.drawable.popupbutton);
                odtButton.setBackgroundResource(R.drawable.popupbutton);
                txtButton.setBackgroundResource(R.drawable.downloadpressed);
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

    public String getMainCategoryName(int mID) {
        if (mID < 0)
            return null;

        return defaultCat.getDefaultCategories()[mID].printName();
    }

    public String getSubCategoryName(int mID, int sID) {
        if (mID < 0 || sID < 0)
            return null;

        return defaultCat.getDefaultCategories()[mID].children()[sID].printName();
    }

    public String getAuthorName() {
        for(ArticleInfo listyList : articleInformation) { //WON'T WORK WITHOUT NETWORKING
            if(listyList.printName.equals(message))
                return listyList.author;
        }

        return "";
    }

    public Date getUploadDate() {
        for(ArticleInfo listyList : articleInformation) { //WON'T WORK WITHOUT NETWORKING
            if(listyList.printName.equals(message))
                return listyList.uploadTime;
        }

        return null;
    }

    public String getUploaderName() {
        for(ArticleInfo listyList : articleInformation) { //WON'T WORK WITHOUT NETWORKING
            if(listyList.printName.equals(message))
                return listyList.owner;
        }

        return "";
    }

    public int getArticleID() {
        for(ArticleInfo listyList : articleInformation) { //WON'T WORK WITHOUT NETWORKING
            if (listyList.printName.equals(message))
                return listyList.getArticleID();
        }

        return -1;
    }
}
