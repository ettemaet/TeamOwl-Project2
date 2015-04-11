package edu.msu.stanospa.teamowl_project2;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        game = new Game(this);
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
