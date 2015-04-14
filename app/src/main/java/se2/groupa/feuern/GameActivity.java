package se2.groupa.feuern;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import se2.groupa.feuern.controller.GameController;
import se2.groupa.feuern.model.Card;
import se2.groupa.feuern.model.Player;

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
    protected TextView img_points;
    protected ImageButton btn_publicCardsRight;
    protected ImageButton btn_publicCardsMiddle;
    protected ImageButton btn_publicCardsLeft;
    protected ImageButton btn_ownCardsRight;
    protected ImageButton btn_ownCardsMiddle;
    protected ImageButton btn_ownCardsLeft;
    protected Button btn_next;
    protected Button btn_KeepCardsYes;
    protected Button btn_KeepCardsNo;
    protected GameController gameController;
    protected Card ownCardSwitch;
    protected Card publicCardSwitch;
    protected boolean moveDone;
    protected boolean stop;
    protected int stopPosition;
    protected double cardPoints;



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
        TextView img_points = (TextView) findViewById(R.id.textView_points);

        img_nowTurnPlayer.setText(gameController.getGameState().getNowTurnPlayer().getName());
        img_nextTurnPlayer.setText(gameController.getGameState().getNextTurnPlayer().getName());

        btn_ownCardsRight.setImageResource(gameController.getGameState().getNowTurnPlayer().getCards()[0].getDrawable());
        btn_ownCardsMiddle.setImageResource(gameController.getGameState().getNowTurnPlayer().getCards()[1].getDrawable());
        btn_ownCardsLeft.setImageResource(gameController.getGameState().getNowTurnPlayer().getCards()[2].getDrawable());

        btn_publicCardsRight.setImageResource(gameController.getGameState().getPublicCards()[0].getDrawable());
        btn_publicCardsMiddle.setImageResource(gameController.getGameState().getPublicCards()[1].getDrawable());
        btn_publicCardsLeft.setImageResource(gameController.getGameState().getPublicCards()[2].getDrawable());

        cardPoints = gameController.getGameState().getNowTurnPlayer().getCardPoints();
        img_points.setText(""+cardPoints);
    }

    public void firstDeal(){
        TextView img_nowTurnPlayer =  (TextView) findViewById(R.id.TextViewNowTurnPlayer);
        TextView img_nextTurnPlayer = (TextView) findViewById(R.id.TextViewNextTurnPlayer);
        TextView textView_publicCards = (TextView) findViewById(R.id.textViewPublicCards);
        TextView textView_nowPlayerLabel = (TextView) findViewById(R.id.textViewCaptionNowTurnPlayer);
        TextView textView_nextPlayerLabel = (TextView) findViewById(R.id.textViewCaptionNextTurnPlayer);
        TextView img_points = (TextView) findViewById(R.id.textView_points);
        ImageButton btn_publicCardsRight = (ImageButton) findViewById(R.id.publicCardsRight);
        ImageButton btn_publicCardsMiddle = (ImageButton) findViewById(R.id.publicCardsMiddle);
        ImageButton btn_publicCardsLeft = (ImageButton) findViewById(R.id.publicCardsLeft);
        ImageButton btn_ownCardsRight = (ImageButton) findViewById(R.id.ownCardsRight);
        ImageButton btn_ownCardsMiddle = (ImageButton) findViewById(R.id.ownCardsMiddle);
        ImageButton btn_ownCardsLeft = (ImageButton) findViewById(R.id.ownCardsLeft);
        Button btn_next = (Button) findViewById(R.id.buttonNext);


        btn_publicCardsLeft.setVisibility(View.INVISIBLE);
        btn_publicCardsMiddle.setVisibility(View.INVISIBLE);
        btn_publicCardsRight.setVisibility(View.INVISIBLE);
        img_nowTurnPlayer.setVisibility(View.INVISIBLE);
        img_nextTurnPlayer.setVisibility(View.INVISIBLE);
        img_points.setVisibility(View.INVISIBLE);
        btn_next.setVisibility(View.INVISIBLE);
        textView_publicCards.setVisibility(View.INVISIBLE);
        textView_nextPlayerLabel.setVisibility(View.INVISIBLE);
        textView_nowPlayerLabel.setVisibility(View.INVISIBLE);


        gameController.dealingOutCards();

        btn_ownCardsRight.setImageResource(gameController.getGameState().getNowTurnPlayer().getCards()[0].getDrawable());
        btn_ownCardsMiddle.setImageResource(gameController.getGameState().getNowTurnPlayer().getCards()[1].getDrawable());
        btn_ownCardsLeft.setImageResource(gameController.getGameState().getNowTurnPlayer().getCards()[2].getDrawable());


    }


    public void switchCardsOwn(View v){
        if(!moveDone) {
            Card card = null;
            if (v.getId() == R.id.ownCardsRight) {
                card = gameController.getGameState().getNowTurnPlayer().getCards()[0];
            }
            if (v.getId() == R.id.ownCardsMiddle) {
                card = gameController.getGameState().getNowTurnPlayer().getCards()[1];
            }
            if (v.getId() == R.id.ownCardsLeft) {
                card = gameController.getGameState().getNowTurnPlayer().getCards()[2];
            }
            setOwnCardSwitch(card);

            if (getOwnCardSwitch() != null && getPublicCardSwitch() != null) {
                gameController.swapCards(getOwnCardSwitch(), getPublicCardSwitch());
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
                card = gameController.getGameState().getPublicCards()[0];
            }
            if (v.getId() == R.id.publicCardsMiddle) {
                card = gameController.getGameState().getPublicCards()[1];
            }
            if (v.getId() == R.id.publicCardsLeft) {
                card = gameController.getGameState().getPublicCards()[2];
            }
            setPublicCardSwitch(card);

            if (getOwnCardSwitch() != null && getPublicCardSwitch() != null) {
                gameController.swapCards(getOwnCardSwitch(), getPublicCardSwitch());
                updateButtons();
                setOwnCardSwitch(null);
                setPublicCardSwitch(null);
                moveDone=true;
            }
        }

    }

    public void next(View view){
        int playerIndex = 0;
        double points = 0;

        TextView img_nowTurnPlayer =  (TextView) findViewById(R.id.TextViewNowTurnPlayer);
        TextView img_nextTurnPlayer = (TextView) findViewById(R.id.TextViewNextTurnPlayer);
        TextView img_points = (TextView) findViewById(R.id.textView_points);
        ImageButton btn_publicCardsRight = (ImageButton) findViewById(R.id.publicCardsRight);
        ImageButton btn_publicCardsMiddle = (ImageButton) findViewById(R.id.publicCardsMiddle);
        ImageButton btn_publicCardsLeft = (ImageButton) findViewById(R.id.publicCardsLeft);
        ImageButton btn_ownCardsRight = (ImageButton) findViewById(R.id.ownCardsRight);
        ImageButton btn_ownCardsMiddle = (ImageButton) findViewById(R.id.ownCardsMiddle);
        ImageButton btn_ownCardsLeft = (ImageButton) findViewById(R.id.ownCardsLeft);
        Button btn_next = (Button) findViewById(R.id.buttonNext);

        if(stop==true && gameController.getGameState().getPlayers().
                indexOf(gameController.getGameState().getNextTurnPlayer()) == stopPosition){

            for(Player p:gameController.getGameState().getPlayers() ){
                if(p.getCardPoints() > points) {
                    points = p.getCardPoints();
                    playerIndex = gameController.getGameState().getPlayers().indexOf(p);
                }
            }

            img_points.setText(gameController.getGameState().getPlayers().get(playerIndex).getName()
                                + " hat gewonnen!");
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
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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
            players.add(new Player("player" + i));
        }
        gameController = new GameController(players);
        firstDeal();
        //gameController.dealingOutCards();
        //updateButtons();

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

    }

    public void KeepCardsYes(View v){

        TextView swapCards = (TextView) findViewById(R.id.textView_wantToKeepCards);
        TextView img_nowTurnPlayer =  (TextView) findViewById(R.id.TextViewNowTurnPlayer);
        TextView img_nextTurnPlayer = (TextView) findViewById(R.id.TextViewNextTurnPlayer);
        TextView textView_publicCards = (TextView) findViewById(R.id.textViewPublicCards);
        TextView textView_nowPlayerLabel = (TextView) findViewById(R.id.textViewCaptionNowTurnPlayer);
        TextView textView_nextPlayerLabel = (TextView) findViewById(R.id.textViewCaptionNextTurnPlayer);
        TextView img_points = (TextView) findViewById(R.id.textView_points);
        ImageButton btn_publicCardsRight = (ImageButton) findViewById(R.id.publicCardsRight);
        ImageButton btn_publicCardsMiddle = (ImageButton) findViewById(R.id.publicCardsMiddle);
        ImageButton btn_publicCardsLeft = (ImageButton) findViewById(R.id.publicCardsLeft);
        ImageButton btn_ownCardsRight = (ImageButton) findViewById(R.id.ownCardsRight);
        ImageButton btn_ownCardsMiddle = (ImageButton) findViewById(R.id.ownCardsMiddle);
        ImageButton btn_ownCardsLeft = (ImageButton) findViewById(R.id.ownCardsLeft);
        Button btn_next = (Button) findViewById(R.id.buttonNext);
        Button btn_KeepCardsYes = (Button) findViewById(R.id.buttonKeepCardsYes);
        Button btn_KeepCardsNo = (Button) findViewById(R.id.buttonKeepCardsNo);

        swapCards.setVisibility(View.INVISIBLE);
        btn_KeepCardsYes.setVisibility(View.INVISIBLE);
        btn_KeepCardsNo.setVisibility(View.INVISIBLE);

        btn_publicCardsLeft.setVisibility(View.VISIBLE);
        btn_publicCardsMiddle.setVisibility(View.VISIBLE);
        btn_publicCardsRight.setVisibility(View.VISIBLE);
        img_nowTurnPlayer.setVisibility(View.VISIBLE);
        img_nextTurnPlayer.setVisibility(View.VISIBLE);
        btn_next.setVisibility(View.VISIBLE);
        textView_publicCards.setVisibility(View.VISIBLE);
        textView_nextPlayerLabel.setVisibility(View.VISIBLE);
        textView_nowPlayerLabel.setVisibility(View.VISIBLE);
        img_points.setVisibility(View.VISIBLE);

        gameController.KeepCardsFinishDealing();
        updateButtons();
    }

    public void KeepCardsNo(View v){
        TextView swapCards = (TextView) findViewById(R.id.textView_wantToKeepCards);
        TextView img_nowTurnPlayer =  (TextView) findViewById(R.id.TextViewNowTurnPlayer);
        TextView img_nextTurnPlayer = (TextView) findViewById(R.id.TextViewNextTurnPlayer);
        TextView textView_publicCards = (TextView) findViewById(R.id.textViewPublicCards);
        TextView textView_nowPlayerLabel = (TextView) findViewById(R.id.textViewCaptionNowTurnPlayer);
        TextView textView_nextPlayerLabel = (TextView) findViewById(R.id.textViewCaptionNextTurnPlayer);
        TextView img_points = (TextView) findViewById(R.id.textView_points);
        ImageButton btn_publicCardsRight = (ImageButton) findViewById(R.id.publicCardsRight);
        ImageButton btn_publicCardsMiddle = (ImageButton) findViewById(R.id.publicCardsMiddle);
        ImageButton btn_publicCardsLeft = (ImageButton) findViewById(R.id.publicCardsLeft);
        ImageButton btn_ownCardsRight = (ImageButton) findViewById(R.id.ownCardsRight);
        ImageButton btn_ownCardsMiddle = (ImageButton) findViewById(R.id.ownCardsMiddle);
        ImageButton btn_ownCardsLeft = (ImageButton) findViewById(R.id.ownCardsLeft);
        Button btn_next = (Button) findViewById(R.id.buttonNext);
        Button btn_KeepCardsYes = (Button) findViewById(R.id.buttonKeepCardsYes);
        Button btn_KeepCardsNo = (Button) findViewById(R.id.buttonKeepCardsNo);

        swapCards.setVisibility(View.INVISIBLE);
        btn_KeepCardsYes.setVisibility(View.INVISIBLE);
        btn_KeepCardsNo.setVisibility(View.INVISIBLE);

        btn_publicCardsLeft.setVisibility(View.VISIBLE);
        btn_publicCardsMiddle.setVisibility(View.VISIBLE);
        btn_publicCardsRight.setVisibility(View.VISIBLE);
        img_nowTurnPlayer.setVisibility(View.VISIBLE);
        img_nextTurnPlayer.setVisibility(View.VISIBLE);
        btn_next.setVisibility(View.VISIBLE);
        textView_publicCards.setVisibility(View.VISIBLE);
        textView_nextPlayerLabel.setVisibility(View.VISIBLE);
        textView_nowPlayerLabel.setVisibility(View.VISIBLE);
        img_points.setVisibility(View.VISIBLE);

        gameController.DontKeepCardsFinishDealing();
        updateButtons();
    }


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