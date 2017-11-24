package com.example.academicarticlemanager;

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

public class TitleScreenLoggedOut extends AppCompatActivity {

    Button loginButton, viewArticlesButton;
    Boolean logged;

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
                if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
                    loginSuccess();
                }

                else {
                    Toast.makeText(getApplicationContext(), "Wrong username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Listener for register button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // register
                Toast.makeText(getApplicationContext(), "Register works", Toast.LENGTH_SHORT).show();
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
