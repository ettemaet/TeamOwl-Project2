package edu.msu.stanospa.teamowl_project2;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.prefs.Preferences;

public class MainActivity extends ActionBarActivity {

    private Game game;
    private final static String PREFERENCES = "preferences";
    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";
    private final static String REMEMBER = "remember";
    private boolean checked = false;
    private String username = "";
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        game = new Game(this);
        readPreferences();
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        checked = ((CheckBox) view).isChecked();
        if (checked == true) {
            username = ((EditText)findViewById(R.id.editUsername)).getText().toString();
            password = ((EditText)findViewById(R.id.editPassword)).getText().toString();
            writePreferences();
        }
    }

    private void writePreferences() {
        SharedPreferences settings = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.putBoolean(REMEMBER, true);

        editor.commit();
    }

    private void readPreferences() {
            SharedPreferences settings = getSharedPreferences(PREFERENCES, MODE_PRIVATE);

            String setUsername = settings.getString(USERNAME, "");
            String setPassword = settings.getString(PASSWORD, "");
            boolean remember = settings.getBoolean(REMEMBER, false);

            ((EditText)findViewById(R.id.editUsername)).setText(setUsername);
            ((EditText)findViewById(R.id.editPassword)).setText(setPassword);
    }

    public void onLogin(final View view) {

        // Need to code to verify login credentials

        username = ((EditText)findViewById(R.id.editUsername)).getText().toString();
        password = ((EditText)findViewById(R.id.editPassword)).getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                    Cloud cloud = new Cloud();
                    final String test = cloud.LogOnCloud(username, password);

                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] ParsedTest = test.split(",");
                            if(ParsedTest[0].equals("yes") ){
                                Toast.makeText(view.getContext(), "Successful Login", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getBaseContext(), DummyActivity.class);
                                startActivity(intent);
                            }
                            else if (ParsedTest[1]!= null) {
                                Toast.makeText(view.getContext(), ParsedTest[1], Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

        }).start();


    }

    public void onCreateNewUser(View view) {
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivity(intent);
    }
}
