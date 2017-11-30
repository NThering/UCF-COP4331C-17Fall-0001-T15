package user_interface;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen_logged_out);

        // Shared variable to determine if previously logged in -- return false if not and do nothing
        logged = getSharedPreferences("loggedIn", MODE_PRIVATE).getBoolean("logged", false);

        if(logged) // If true, instant login -- CHANGE LOGIC WHEN WE HAVE MORE THAN ONE USER! THIS IS FOR TESTING
            loginSuccess();

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
            new Thread() {
                public void run() {
                    try {
                    prog.show();
                        String un = username.getText().toString();
                        String pw = password.getText().toString();
                        int logSuccess = -1;

                        if(un != null && !un.isEmpty() && pw != null && !pw.isEmpty())
                            logSuccess = Public.login(un, pw);

                        if(logSuccess == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
                                }
                            });
                            loginSuccess();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                prog.dismiss();
                            }
                        });
                    } catch (final Exception e) {

                        }
                }
            }.start();
            }
        });

        // Listener for register button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String un = username.getText().toString();
                String pw = password.getText().toString();
                int regSuccess = -1;
                int logSuccess = -1;

                if(un != null && !un.isEmpty() && pw != null && !pw.isEmpty())
                    regSuccess = Public.register(un, pw);

                if(regSuccess == 0) {
                    Toast.makeText(getApplicationContext(), "Register successful, logging in...", Toast.LENGTH_SHORT).show();
                    logSuccess = Public.login(un, pw);
                    if(logSuccess == 0)
                        loginSuccess();
                }
            }
        });
    }

    public void loginSuccess() {
        // Remembers that someone is logged in and will take them to the next screen on startup
        getSharedPreferences("loggedIn", MODE_PRIVATE).edit().putBoolean("logged", true).commit();
        Intent intent = new Intent(this, TitleScreenLoggedIn.class);
        startActivity(intent);
        finish(); // Prevents going back to login page
    }
}
