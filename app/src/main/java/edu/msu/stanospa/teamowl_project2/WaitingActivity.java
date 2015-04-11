package edu.msu.stanospa.teamowl_project2;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class WaitingActivity extends ActionBarActivity {

    String Username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        Intent intent = getIntent();

        Username = intent.getStringExtra("username");

        isGameReady();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dummy, menu);
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

    public void isGameReady(){

        final Timer waitingTimer = new Timer();
        waitingTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                new Thread(new Runnable() {
                    public void run() {
                        //Check to see if player is waiting
                        Cloud cloud = new Cloud();
                        final String playerWaiting = cloud.isPlayerWaiting(Username);

                        if (playerWaiting.equals("found")) {
                            Intent selection = new Intent(getBaseContext(), SelectionActivity.class);
                            startActivity(selection);
                            waitingTimer.cancel();
                        } else if (playerWaiting.equals("create")){
                            TextView waitingText = (TextView) findViewById(R.id.textWaiting);
                            waitingText.setText(R.string.waiting_for_player);
                        }
                    }
                }).start();
            }
        },0,1000); // mention time interval after which your xml will be hit.
    }
}
