package se2.groupa.feuern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
//import se2.groupa.feuern.controller.GameController;
//import se2.groupa.feuern.controller.GameState;
import se2.groupa.feuern.others.Card;
import se2.groupa.feuern.others.CardDeck;
import se2.groupa.feuern.others.Player;

/**
 * Displays your own hand when its your turn and let you choose to change one card or to
 * take the whole three cards of the mid
 * (Evtl könnten wir einbauen ob die App im Background ist -> dann notification)
 * <p/>
 * reacts   on callbacks for "handy hinunterlegen" -> obegehn
 * on callbacks for "handy schütteln" -> weiter
 * <p/>
 * Also provides to look in the cards of another user :)
 */

public class GameActivity extends Activity {

    protected TextView img_nowTurnPlayer;
    protected TextView img_nextTurnPlayer;
    protected ImageButton btn_publicCardsRight;
    protected ImageButton btn_publicCardsMiddle;
    protected ImageButton btn_publicCardsLeft;
    protected ImageButton btn_ownCardsRight;
    protected ImageButton btn_ownCardsMiddle;
    protected ImageButton btn_ownCardsLeft;
    protected Button btn_next;
    //protected GameController gameController;
    protected Card ownCardSwitch;
    protected Card publicCardSwitch;
    protected boolean moveDone;
    protected boolean stop;
    protected int stopPosition;



    public Card getOwnCardSwitch(){
        return this.ownCardSwitch;
    }
    public void setOwnCardSwitch(Card card){
        this.ownCardSwitch = card;
    }

    public Card getPublicCardSwitch(){
        return this.publicCardSwitch;
    }
    public void setPublicCardSwitch(Card card){
        this.publicCardSwitch = card;
    }



    public void updateButtons(){

        TextView img_nowTurnPlayer =  (TextView) findViewById(R.id.TextViewNowTurnPlayer);
        TextView img_nextTurnPlayer = (TextView) findViewById(R.id.TextViewNextTurnPlayer);
        ImageButton btn_publicCardsRight = (ImageButton) findViewById(R.id.publicCardsRight);
        ImageButton btn_publicCardsMiddle = (ImageButton) findViewById(R.id.publicCardsMiddle);
        ImageButton btn_publicCardsLeft = (ImageButton) findViewById(R.id.publicCardsLeft);
        ImageButton btn_ownCardsRight = (ImageButton) findViewById(R.id.ownCardsRight);
        ImageButton btn_ownCardsMiddle = (ImageButton) findViewById(R.id.ownCardsMiddle);
        ImageButton btn_ownCardsLeft = (ImageButton) findViewById(R.id.ownCardsLeft);
        Button btn_next = (Button) findViewById(R.id.buttonNext);
/*
        img_nowTurnPlayer.setText(gameController.getGameState().getNowTurnPlayer().getName());
        img_nextTurnPlayer.setText(gameController.getGameState().getNextTurnPlayer().getName());

        btn_ownCardsRight.setImageResource(gameController.getGameState().getNowTurnPlayer().getCards()[0].getDrawable());
        btn_ownCardsMiddle.setImageResource(gameController.getGameState().getNowTurnPlayer().getCards()[1].getDrawable());
        btn_ownCardsLeft.setImageResource(gameController.getGameState().getNowTurnPlayer().getCards()[2].getDrawable());

        btn_publicCardsRight.setImageResource(gameController.getGameState().getPublicCards()[0].getDrawable());
        btn_publicCardsMiddle.setImageResource(gameController.getGameState().getPublicCards()[1].getDrawable());
        btn_publicCardsLeft.setImageResource(gameController.getGameState().getPublicCards()[2].getDrawable());
        */
    }


    public void switchCardsOwn(View v){
        if(!moveDone) {
            Card card = null;
            if (v.getId() == R.id.ownCardsRight) {
                //card = gameController.getGameState().getNowTurnPlayer().getCards()[0];
            }
            if (v.getId() == R.id.ownCardsMiddle) {
                //card = gameController.getGameState().getNowTurnPlayer().getCards()[1];
            }
            if (v.getId() == R.id.ownCardsLeft) {
                //card = gameController.getGameState().getNowTurnPlayer().getCards()[2];
            }
            setOwnCardSwitch(card);

            if (getOwnCardSwitch() != null && getPublicCardSwitch() != null) {
                //gameController.swapCards(getOwnCardSwitch(), getPublicCardSwitch());
                updateButtons();
                setOwnCardSwitch(null);
                setPublicCardSwitch(null);
                moveDone=true;
            }
        }

    }

    public void switchCardsPublic(View v){
        if(!moveDone) {
            Card card = null;
            if (v.getId() == R.id.publicCardsRight) {
                //card = gameController.getGameState().getPublicCards()[0];
            }
            if (v.getId() == R.id.publicCardsMiddle) {
                //card = gameController.getGameState().getPublicCards()[1];
            }
            if (v.getId() == R.id.publicCardsLeft) {
                //card = gameController.getGameState().getPublicCards()[2];
            }
            setPublicCardSwitch(card);

            if (getOwnCardSwitch() != null && getPublicCardSwitch() != null) {
                //gameController.swapCards(getOwnCardSwitch(), getPublicCardSwitch());
                updateButtons();
                setOwnCardSwitch(null);
                setPublicCardSwitch(null);
                moveDone=true;
            }
        }

    }

    public void next(View view){
        TextView img_nowTurnPlayer =  (TextView) findViewById(R.id.TextViewNowTurnPlayer);
        TextView img_nextTurnPlayer = (TextView) findViewById(R.id.TextViewNextTurnPlayer);
        ImageButton btn_publicCardsRight = (ImageButton) findViewById(R.id.publicCardsRight);
        ImageButton btn_publicCardsMiddle = (ImageButton) findViewById(R.id.publicCardsMiddle);
        ImageButton btn_publicCardsLeft = (ImageButton) findViewById(R.id.publicCardsLeft);
        ImageButton btn_ownCardsRight = (ImageButton) findViewById(R.id.ownCardsRight);
        ImageButton btn_ownCardsMiddle = (ImageButton) findViewById(R.id.ownCardsMiddle);
        ImageButton btn_ownCardsLeft = (ImageButton) findViewById(R.id.ownCardsLeft);
        Button btn_next = (Button) findViewById(R.id.buttonNext);
/*
        if(stop==true && gameController.getGameState().getPlayers().
                indexOf(gameController.getGameState().getNextTurnPlayer()) == stopPosition){


            btn_next.setText("Spielende!");
            btn_publicCardsRight.setClickable(false);
            btn_publicCardsMiddle.setClickable(false);
            btn_publicCardsLeft.setClickable(false);
            btn_ownCardsRight.setClickable(false);
            btn_ownCardsMiddle.setClickable(false);
            btn_ownCardsLeft.setClickable(false);
            btn_next.setClickable(false);

        }
        else {
            gameController.doStep();
            moveDone = false;
            updateButtons();
        }
        */
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        /*
        TextView img_nowTurnPlayer =  (TextView) findViewById(R.id.TextViewNowTurnPlayer);
        TextView img_nextTurnPlayer = (TextView) findViewById(R.id.TextViewNextTurnPlayer);
        ImageButton btn_publicCardsRight = (ImageButton) findViewById(R.id.publicCardsRight);
        ImageButton btn_publicCardsMiddle = (ImageButton) findViewById(R.id.publicCardsMiddle);
        ImageButton btn_publicCardsLeft = (ImageButton) findViewById(R.id.publicCardsLeft);
        ImageButton btn_ownCardsRight = (ImageButton) findViewById(R.id.ownCardsRight);
        ImageButton btn_ownCardsMiddle = (ImageButton) findViewById(R.id.ownCardsMiddle);
        ImageButton btn_ownCardsLeft = (ImageButton) findViewById(R.id.ownCardsLeft);
        Button btn_next = (Button) findViewById(R.id.buttonNext);

        //Hier werden jetzt zu Testzwecken einfach vier Spieler-Objekte initialisiert
        int number = 4;

        ArrayList<Player> players = new ArrayList<Player>(number);
        for (int i = 1; i <= number; i++) {
            players.add(new Player(i, "player" + i));
        }
        gameController = new GameController(players);
        gameController.dealingOutCards();
        updateButtons();

        btn_publicCardsRight.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!moveDone) {
                    gameController.swapAllCards();
                    updateButtons();
                    moveDone = true;
                }
                return true;
            }
        });

        btn_publicCardsLeft.setOnLongClickListener(new View.OnLongClickListener() {
            Button btn_next = (Button) findViewById(R.id.buttonNext);
            @Override
            public boolean onLongClick(View v) {

                    stop = true;
                    stopPosition = gameController.stop();
                    btn_next.setText("Stop gedrückt!");


                return true;
            }
        });
        */

    }

    //ah ah ah 13zoller 15zoller is the best
    //das ist ein test


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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

}