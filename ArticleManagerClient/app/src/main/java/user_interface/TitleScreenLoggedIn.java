package user_interface;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import team15.articlemanagerclient.R;

public class TitleScreenLoggedIn extends AppCompatActivity {

    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen_logged_in);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        userName = bundle.getString("uName");

        Button myArticlesButton = (Button) findViewById(R.id.myArticlesButton);
        Button viewArticlesButton = (Button) findViewById(R.id.viewArticlesButtonLogged);

        // View My Articles
        myArticlesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity2 = new Intent(TitleScreenLoggedIn.this, MyArticles.class);
                newActivity2.putExtra("uName", userName);
                startActivity(newActivity2);
            }
        });

        // View all articles
        viewArticlesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(TitleScreenLoggedIn.this, AllArticles.class);
                newActivity.putExtra("uName", userName);
                startActivity(newActivity);
            }
        });
    }
}
