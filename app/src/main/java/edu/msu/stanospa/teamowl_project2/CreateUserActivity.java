package edu.msu.stanospa.teamowl_project2;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.msu.stanospa.teamowl_project2.R;

public class CreateUserActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCreateUser(final View view) {
        // Code to create a new user
        final String username = ((EditText)findViewById(R.id.editUsername)).getText().toString();
        final String password = ((EditText)findViewById(R.id.editPassword)).getText().toString();
        final String password1 = ((EditText)findViewById(R.id.editPasswordAgain)).getText().toString();


        new Thread(new Runnable() {
            @Override
            public void run() {
                Cloud cloud = new Cloud();
                final String test = cloud.CreateOnCloud(username, password, password1);

                view.post(new Runnable() {
                    @Override
                    public void run() {
                        String[] ParsedTest = test.split(",");
                        if(ParsedTest[0].equals("yes") ){
                            Toast.makeText(view.getContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
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
}
