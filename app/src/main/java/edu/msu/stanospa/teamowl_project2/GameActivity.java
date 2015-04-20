package edu.msu.stanospa.teamowl_project2;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class GameActivity extends ActionBarActivity {


    private GameView gameView;

    private String gId;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_game);
        gameView = (GameView)findViewById(R.id.gameView);

        if(bundle != null) {
            gameView.loadInstanceState(bundle, this);
        }
        else {
            Game game = (Game)getIntent().getExtras().getSerializable(getString(R.string.game_state));
            gameView.setGame(game);
        }
        gId = getIntent().getStringExtra("gameId");
        gameView.setGameId(gId);
        gameView.startTimer();
        TextView tv = (TextView)findViewById(R.id.placementText);
        tv.setText(getString(R.string.bird_placement_info) +
                gameView.getGame().getCurrentPlayerName());

        gameView.reloadBirds();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_activity, menu);
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
                gameView.getGame().iLost();
                gameView.getGame().declareWinner();
                gameView.getGame().saveInstanceState(newBundle, getBaseContext());
                Cloud cloud = new Cloud();
                cloud.ExitGame(gId, gameView.getGame().GetToken());
                Intent intent = new Intent(getBaseContext(), FinalScoreActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtras(newBundle);
                startActivity(intent);
            }
        }).start();
    }

    public void onPlaceBird(View view) {
        gameView.getGame().StopTimer();
        gameView.onPlaceBird();
        

        Bundle bundle = new Bundle();
        gameView.getGame().saveInstanceState(bundle, this);

        if (gameView.inGameOverState()) {

            Intent intent = new Intent(this, FinalScoreActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }

        else if (gameView.inSelectionState()) {

            Intent intent = new Intent(this, SelectionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("gameId", gId);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }

        TextView tv = (TextView)findViewById(R.id.placementText);
        tv.setText(String.format(getString(R.string.bird_placement_info),
                gameView.getGame().getCurrentPlayerName()));


    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        gameView.saveInstanceState(bundle, this);
    }
}
