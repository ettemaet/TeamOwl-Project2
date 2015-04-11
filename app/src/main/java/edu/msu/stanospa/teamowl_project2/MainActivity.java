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

public class MainActivity extends ActionBarActivity {

    private Game game;
    private final static String PREFERENCES = "preferences";
    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";
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
        boolean checked = ((CheckBox) view).isChecked();
        if (checked == true) {
            username = ((EditText)findViewById(R.id.editUsername)).getText().toString();
            password = ((EditText)findViewById(R.id.editPassword)).getText().toString();
            writePreferences();
        }
    }

    private void readPreferences() {
        SharedPreferences settings = getSharedPreferences(PREFERENCES, MODE_PRIVATE);

        String putUsername = settings.getString(USERNAME, "");
        String putPassword = settings.getString(PASSWORD, "");

        ((EditText)findViewById(R.id.editUsername)).setText(putUsername);
        ((EditText)findViewById(R.id.editPassword)).setText(putPassword);

    }

    private void writePreferences() {
        SharedPreferences settings = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);

        editor.commit();
    }

    public void onLogin(final View view) {

        // Need to code to verify login credentials

        final String username = ((EditText)findViewById(R.id.editUsername)).getText().toString();
        final String password = ((EditText)findViewById(R.id.editPassword)).getText().toString();



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
                                Intent intent = new Intent(getBaseContext(), WaitingActivity.class);
                                intent.putExtra("username", username);
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
