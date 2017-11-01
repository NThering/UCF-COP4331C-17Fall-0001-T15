package networking;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;
import android.content.Intent;
import java.util.HashMap;

public class SessionManager {

    private static final String SHARED_PREF_ID = "shared_pref";
    private static final String LOGGED_IN = "is_logged_in";
    public static final String KEY_USER = "username";
    public static final String KEY_PASS = "password";

    public SharedPreferences instance;
    private Context context;
    private Editor editor;
    int PRIVATE_MODE = 0;

    public SessionManager(Context contxt)
    {
        this.context = contxt;
        instance = contxt.getSharedPreferences(SHARED_PREF_ID, PRIVATE_MODE);
        editor = instance.edit();
    }


    public void createSession(String username, String password)
    {
        editor.putBoolean(LOGGED_IN, true);
        //set String values in editor
        editor.putString(KEY_USER, username);
        editor.putString(KEY_PASS, password);

        //commit changes from editor to object being edited
        editor.apply();
    }

    public void logoutSession()
    {
        //clear user data from shared preferences
        editor.clear();
        editor.commit();

        //intent to redirect user to login screen
        Intent redirectIntent = new Intent(context, LoginActivity.class);

        //close activities on stack
        redirectIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //add flag to start new activity
        redirectIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(redirectIntent);
    }

    public boolean checkLoginStatus()
    {
        if (!this.isLoggedIn()) {
            //intent to redirect user to login screen
            Intent redirectIntent = new Intent(context, LoginActivity.class);

            //close activities on stack
            redirectIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //add flag to start new activity
            redirectIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(redirectIntent);
            return false;
        }
        else {
            return true;
        }
    }

    public HashMap<String, String> getUserDetails()
    {
        HashMap<String, String> userDetails = new HashMap<String, String>();

        //add user's details to hash map
        userDetails.put(KEY_USER, instance.getString(KEY_USER, null));
        userDetails.put(KEY_PASS, instance.getString(KEY_PASS, null));

        return userDetails;
    }

}