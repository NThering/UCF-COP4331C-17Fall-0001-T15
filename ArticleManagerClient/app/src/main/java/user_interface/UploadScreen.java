package user_interface;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import team15.articlemanagerclient.R;

public class UploadScreen extends AppCompatActivity {

    // ALL POPUP CODE IS DONE IN THE CLASS THAT CALLS THE POPUP!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_screen);
    }
}
