package edu.msu.stanospa.teamowl_project2;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * Custom view class for our Game
 */
public class GameView extends View {

    /**
     * The actual game
     */
    private Game game;

    public GameView(Context context) {
        super(context);
        init(null, 0);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        game = new Game(getContext());
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean inSelectionState() {
        return game.inSelectionState();
    }

    public boolean inGameOverState() {
        return game.inGameOverState();
    }

    public void setGameId(String gameId) {game.setGameId(gameId);}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return game.onTouchEvent(this, event);
    }

    public void onPlaceBird() {
        game.confirmBirdPlacement();
        invalidate();
    }

    public void startTimer(){
        game.StartGameTimer();
    }

    public void endTime(){
        game.StopTimer();
    }

    public void reloadBirds() {
        game.reloadBirds(getContext());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

       game.draw(canvas);
    }

    public void saveInstanceState(Bundle bundle, Context context) { game.saveInstanceState(bundle, context); }

    public void loadInstanceState(Bundle bundle, Context context) { setGame((Game)bundle.getSerializable(context.getString(R.string.game_state))); }

}
