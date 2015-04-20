package edu.msu.stanospa.teamowl_project2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class SelectionActivity extends ActionBarActivity {

    private Game game;

    private SelectionView selectionView;

    private TextView selectionText;

    private Toast noBirdToast;

    private Cloud cloud;

    private Boolean birdSelected = false;

    private Boolean stillMyTurn = true;

    private String gameId = "";

    /**
     * Local player - either 1 or 2
     */
    private int player;

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        selectionView.saveInstanceState(bundle);
        game.saveInstanceState(bundle, this);
    }

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_selection);

        cloud = new Cloud();

        if(bundle != null) {
            game = (Game)bundle.getSerializable(getString(R.string.game_state));
        }
        else {
            game = (Game)getIntent().getExtras().getSerializable(getString(R.string.game_state));
        }

        game.StartGameTimer();
        selectionView = (SelectionView)findViewById(R.id.selectionView);

        this.selectionText = (TextView) findViewById(R.id.playerNameLabel);
        setPlayerSelectionText();

        gameId = getIntent().getStringExtra("gameId");

        final Context context = getApplicationContext();
        CharSequence noBirdText = "Please select a bird!";
        int duration = Toast.LENGTH_SHORT;

        noBirdToast = Toast.makeText(context, noBirdText, duration);
        TextView v = (TextView) noBirdToast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.RED);

        boolean player1 = game.isPlayerOne();
        if (player1) {
            player = 1;
        } else { player = 2; }

        final Bundle newBundle = new Bundle();
        //game.saveInstanceState(bundle, this);
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (stillMyTurn) {
                    String[] temp = cloud.GetCurTurn(gameId,game.GetToken()).split(",");
                    if (temp[0].equals("gameover")) {
                        game.declareWinner();
                        game.saveInstanceState(newBundle, context);
                        Intent intent = new Intent(getBaseContext(), FinalScoreActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtras(newBundle);
                        startActivity(intent);
                        stillMyTurn = false;
                        finish();

                    } else {
                        int serverTurn = Integer.parseInt(temp[1]);
                        if(game.getLocalTurn() < serverTurn) {
                            //load the bird from server
                            String birdinfo = cloud.GetMovement(gameId,game.getLocalTurn(),game.GetToken());
                            String[] result = birdinfo.split(",");
                            if(!result[0].equals("yes"))
                            {
                                Log.i("getmovement","error");
                                continue;
                            }
                            else
                            {
                                if(result[4].equals("1"))
                                {
                                    //raise winning condition;
                                    game.declareWinner();
                                    //go to win activity

                                } else {
                                    Bird serverBird = new Bird(getBaseContext(), Integer.parseInt(result[1]), Float.parseFloat(result[2])
                                            , Float.parseFloat(result[3]));
                                    game.AddBird(serverBird);
                                }

                            }
                            //check if the other guy exit or dc
                            //cloud.IsGameOver(gameId) {
                            // goto final score and display opponent dc or exit
                            //}
                            //


                            //inc the turn number
                            game.AddLocalTurn();
                            continue;
                        }
                    }
                    //Log.i("player",""+player);
                    //if ((cloud.isMyTurn(game.getGameId(), Integer.toString(player))) && birdSelected) {
                    //Log.i("gameid, player, birdSelected", "values: " + gameId + Integer.toString(player) + birdSelected);
                    //Log.i("isMyTurn()", "value: " + cloud.isMyTurn(gameId, Integer.toString(player)));

                    if ((cloud.isMyTurn(gameId, Integer.toString(player),game.GetToken())) && birdSelected) {
                        Log.i("BIRD SELECTED IF: ", " Value" + birdSelected);
                        stillMyTurn = false;
                        game.saveInstanceState(newBundle, context);
                        Intent intent = new Intent(getBaseContext(), GameActivity.class);
                        intent.putExtra("gameId", gameId);
                        intent.putExtras(newBundle);
                        startActivity(intent);
                        finish();
                    //} else if ((cloud.isMyTurn(game.getGameId(), Integer.toString(player))) && !birdSelected) {
                    } else if ((cloud.isMyTurn(gameId, Integer.toString(player),game.GetToken())) && !birdSelected) {
                        setPlayerSelectionTextInThread();
                        Log.i("BIRD SELECTED IF ELSE: ", " Value" + birdSelected);
                        //Do nothing, is current player's turn but no bird is selected
                    } else {
                        Log.i("BIRD SELECTED ELSE: ", " Value" + birdSelected);
                        changeToWaitingText();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        // handle the exception...
                    }
                }
            }
        }).start();


        if (bundle != null){
            Log.i("onCreate()", "restoring state...");
            selectionView.loadInstanceState(bundle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selection_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_exit:
                endGame();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void endGame() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bundle newBundle = new Bundle();
                //Toast.makeText(getBaseContext(), "Exiting Game", Toast.LENGTH_SHORT).show();
                game.declareWinner();
                game.saveInstanceState(newBundle, getBaseContext());
                Cloud cloud = new Cloud();
                cloud.ExitGame(gameId,game.GetToken());
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        }).start();
    }

    private void changeToWaitingText() {
        selectionText.post(new Runnable() {
            @Override
            public void run() {
                selectionText.setText(R.string.selection_waiting_title);
            }
        });
        //selectionText.setText(R.string.selection_waiting_title);
    }

    /**
     * set the text at the top of the selection screen to the appropriate player
     */
    private void setPlayerSelectionText() {
        selectionText.setText(game.getLocalPlayerName() + " " + getString(R.string.player_select));
    }

    private void setPlayerSelectionTextInThread() {
        selectionText.post(new Runnable() {
            @Override
            public void run() {
                selectionText.setText(game.getLocalPlayerName() + " " + getString(R.string.player_select));
            }
        });
    }

    public void onConfirmSelection(View view) {
        Bundle bundle = new Bundle();
        game.saveInstanceState(bundle, this);

        if (selectionView.isSelected()) {
            birdSelected = true;

            game.StopTimer();
            selectionView.setPlayerSelection(game);

            /*if (!game.inSelectionState()){
                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
            else
                setPlayerSelectionText();
            */

        } else {
            noBirdToast.show();
            Log.i("onConfirmSelection", "bird not selected");
        }
    }

    public int checkPlayerTurn() {
        String result = cloud.GetCurTurn(game.getGameId(),game.GetToken());
        String[] parsed = new String[3];
        parsed = result.split(",");
        if(parsed[0].equals("error")) {
            return 0;
        } else if ( parsed[2].equals("player1")) {
            return 1;
        } else if (parsed[2].equals("player2")) {
            return 2;
        } else {
            return -1;
        }

    }

}
