package user_interface;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import in.gauriinfotech.commons.Progress;
import team15.articlemanagerclient.R;
import networking.Public;

public class TitleScreenLoggedOut extends AppCompatActivity {

    Button loginButton, viewArticlesButton;
    Boolean logged;
    ProgressDialog prog;
    String userName;
    String unLog, pwLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen_logged_out);

        // Shared variable to determine if previously logged in -- return false if not and do nothing
        logged = getSharedPreferences("loggedIn", MODE_PRIVATE).getBoolean("logged", false);
        userName = getSharedPreferences("oname", MODE_PRIVATE).getString("onamewa", "");

        if(logged) // If true, instant login
            loginSuccess(userName);

        // Initialize buttons
        loginButton = (Button) findViewById(R.id.loginButton);
        viewArticlesButton = (Button) findViewById(R.id.viewArticlesButton);

        // Initialize ProgressDialog
        prog = new ProgressDialog(TitleScreenLoggedOut.this);
        prog.setMessage("loading");
        prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prog.setCancelable(false);
        prog.setCanceledOnTouchOutside(false);

        // Call popup when login is clicked
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPopup(v);
            }
        });

        // Go to articles view when button is clicked
        viewArticlesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(TitleScreenLoggedOut.this, AllArticles.class);
                newActivity.putExtra("uName", "guest");
                startActivity(newActivity);
            }
        });
    }

    // Method for login popup screen
    private void callPopup(View v) {
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_login_screen, null);

        final PopupWindow window = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        window.showAtLocation(v, Gravity.CENTER, 0, 0);

        // Instance variables
        final EditText username = (EditText) popupView.findViewById(R.id.username_field);
        final EditText password = (EditText) popupView.findViewById(R.id.password_field);
        Button register = (Button) popupView.findViewById(R.id.register);
        Button loginPopup = (Button) popupView.findViewById(R.id.loginButtonPopup);

        window.setFocusable(true);
        window.update();

        // Listener for login button
        loginPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            unLog = username.getText().toString();
            pwLog = password.getText().toString();
                int logSuccess = -1;

                if(unLog != null && !unLog.isEmpty() && pwLog != null && !pwLog.isEmpty())
                    logSuccess = Public.login(unLog, pwLog);

                if(logSuccess == 0) {
                    Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();

                    loginSuccess(unLog);
                }
            }
        });

        // Listener for register button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unLog = username.getText().toString();
                pwLog = password.getText().toString();

                int regSuccess = -1;
                int logSuccess = -1;

                if(unLog != null && !unLog.isEmpty() && pwLog != null && !pwLog.isEmpty())
                    regSuccess = Public.register(unLog, pwLog);

                if(regSuccess == 0) {
                    Toast.makeText(getApplicationContext(), "Register successful, logging in...", Toast.LENGTH_SHORT).show();
                    logSuccess = Public.login(unLog, pwLog);

                    if(logSuccess == 0)
                        loginSuccess(unLog);
                }
            }
        });
    }

    public void loginSuccess(String un) {
        // Remembers that someone is logged in and will take them to the next screen on startup
        getSharedPreferences("loggedIn", MODE_PRIVATE).edit().putBoolean("logged", true).commit();
        getSharedPreferences("oname", MODE_PRIVATE).edit().putString("onamewa", un).commit();
        Intent intent = new Intent(this, TitleScreenLoggedIn.class);
        intent.putExtra("uName", un);
        startActivity(intent);
        finish(); // Prevents going back to login page
    }
}
