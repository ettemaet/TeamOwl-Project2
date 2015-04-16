package edu.msu.stanospa.teamowl_project2;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class WaitingActivity extends ActionBarActivity {

    private Game game;
    String Userid;
    String Username;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_waiting);
        if(bundle != null) {
            game = (Game)bundle.getSerializable(getString(R.string.game_state));
        }
        else {
            game = (Game)getIntent().getExtras().getSerializable(getString(R.string.game_state));
        }
        Intent intent = getIntent();

        Userid = intent.getStringExtra("userid");
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

    public void isGameReady() {

        new Thread(new Runnable() {
            public void run() {
                Boolean running = true;
                Bundle bundle = new Bundle();
                game.saveInstanceState(bundle, getBaseContext());
                //Check to see if player is waiting
                Cloud cloud = new Cloud();
                String[] playerWaiting = new String[2];
                String result = "";
                while (running) {
                    result = cloud.isPlayerWaiting(Userid);

                    if (result != null) {
                        playerWaiting = result.split(",");
                    }
                    //Bundle bundle = new Bundle();
                    //game.saveInstanceState(bundle, getBaseContext());

                    if (playerWaiting[0].equals("yes")) {
                        if (playerWaiting[1].equals("found")) {
                            // Player 2 joined
                            running = false;
                            game.setLocalPlayer(Username);
                            //game.setPlayer2Name(Username);
                            game.saveInstanceState(bundle, getBaseContext());
                            Intent selection = new Intent(getBaseContext(), SelectionActivity.class);
                            selection.putExtras(bundle);
                            startActivity(selection);
                        } else if (playerWaiting[1].equals("ready")) {
                            // Player 1 created game
                            running = false;
                            //game.setPlayer1Name(Username);
                            game.setLocalPlayer(Username);
                            game.setGameId(playerWaiting[2]);
                            game.saveInstanceState(bundle, getBaseContext());
                            Intent selection = new Intent(getBaseContext(), SelectionActivity.class);
                            selection.putExtras(bundle);
                            startActivity(selection);
                        } else if (playerWaiting[1].equals("create")) {
                            // Game created
                            final TextView waitingText = (TextView) findViewById(R.id.textWaiting);
                            waitingText.post(new Runnable() {
                                @Override
                                public void run() {
                                    waitingText.setText(R.string.waiting_for_player);
                                }
                            });
                            game.setGameId(playerWaiting[2]);
                            game.saveInstanceState(bundle, getBaseContext());
                            //checkWaitingStatus();
                        } else if (playerWaiting[1].equals("waiting")) {
                            // Waiting
                        }
                    } else if (playerWaiting.equals("no")) {
                        Toast.makeText(getBaseContext(), playerWaiting[1], Toast.LENGTH_SHORT).show();
                    }
                    if (result != null) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            // handle the exception...
                        }
                    }
                }
            }

        }).start();
    }
}
