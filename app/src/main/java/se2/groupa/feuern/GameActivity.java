package se2.groupa.feuern;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.FloatMath;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import se2.groupa.feuern.controller.ApplicationController;
import se2.groupa.feuern.controller.GameController;
import se2.groupa.feuern.model.Card;
import se2.groupa.feuern.model.GameState;
import se2.groupa.feuern.model.Player;
import se2.groupa.feuern.network.classes.CommunicationCommand;
import se2.groupa.feuern.network.classes.Operations;
import se2.groupa.feuern.network.threads.ClientThread;
import se2.groupa.feuern.network.threads.ListenerThread;

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

public class GameActivity extends Activity implements SensorEventListener  {

    private TextView img_nowTurnPlayer;
    private TextView img_nextTurnPlayer;
    private TextView img_points;
    private TextView swapCards;
    private TextView textView_publicCards;
    private TextView textView_nowPlayerLabel;
    private TextView textView_nextPlayerLabel;
    private ImageButton btn_publicCardsRight;
    private ImageButton btn_publicCardsMiddle;
    private ImageButton btn_publicCardsLeft;
    private ImageButton btn_ownCardsRight;
    private ImageButton btn_ownCardsMiddle;
    private ImageButton btn_ownCardsLeft;
    private Button btn_next;
    private Button btn_KeepCardsYes;
    private Button btn_KeepCardsNo;
    private GameController gameController;
    private Card ownCardSwitch;
    private Card publicCardSwitch;
    private boolean moveDone;
    private boolean stop;
    private int stopPosition;
    private double cardPoints;
    private Player currentPlayer;

    protected ListenerThread listenerThread;
    protected ClientThread clientThread;
    protected Handler uiHandler;

    private SensorManager sensorMan;
    private Sensor accelerometer;

    private static boolean flag = true;
    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

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



    public void returnGameStateToServer() {
        clientThread.executeCommand(CommunicationCommand.ReturnGameStateToServer, gameController.getGameState());
    }


    public void updateButtons() {



        cardPoints = gameController.getGameState().getNowTurnPlayer().getCardPoints();

         img_nowTurnPlayer =  (TextView) findViewById(R.id.TextViewNowTurnPlayer);
         img_nextTurnPlayer = (TextView) findViewById(R.id.TextViewNextTurnPlayer);
         btn_publicCardsRight = (ImageButton) findViewById(R.id.publicCardsRight);
        btn_publicCardsMiddle = (ImageButton) findViewById(R.id.publicCardsMiddle);
         btn_publicCardsLeft = (ImageButton) findViewById(R.id.publicCardsLeft);
         btn_ownCardsRight = (ImageButton) findViewById(R.id.ownCardsRight);
         btn_ownCardsMiddle = (ImageButton) findViewById(R.id.ownCardsMiddle);
         btn_ownCardsLeft = (ImageButton) findViewById(R.id.ownCardsLeft);
         btn_next = (Button) findViewById(R.id.buttonNext);
         img_points = (TextView) findViewById(R.id.textView_points);

        if(gameController.getGameState().isStop() && gameController.getGameState().getStopPosition() == gameController.getGameState().getCounter()){
            btn_next.setText("Spielende");

            int playerIndex = 0;
            double points = 0;

            for(Player p:gameController.getGameState().getPlayers() ){
                if(p.getCardPoints() > points) {
                    points = p.getCardPoints();
                    playerIndex = gameController.getGameState().getPlayers().indexOf(p);
                }
            }

            img_points.setText(gameController.getGameState().getPlayers().get(playerIndex).getName()
                    + " hat gewonnen!");
            returnGameStateToServer();

            Intent intent = new Intent(this, ResultActivity.class);
            startActivity(intent);

        }


        img_nowTurnPlayer.setText(gameController.getGameState().getNowTurnPlayer().getName());
        img_nextTurnPlayer.setText(gameController.getGameState().getNextTurnPlayer().getName());

        if(this.gameController.getGameState().getNowTurnPlayer().getName().equals(this.currentPlayer.getName())) {
            btn_ownCardsRight.setImageResource(gameController.getGameState().getNowTurnPlayer().getCards()[0].getDrawable());
            btn_ownCardsMiddle.setImageResource(gameController.getGameState().getNowTurnPlayer().getCards()[1].getDrawable());
            btn_ownCardsLeft.setImageResource(gameController.getGameState().getNowTurnPlayer().getCards()[2].getDrawable());
            img_points.setText(""+cardPoints);
        }


        if (this.gameController.getGameState().getCounter() != 0){
            btn_publicCardsRight.setImageResource(gameController.getGameState().getPublicCards()[0].getDrawable());
            btn_publicCardsMiddle.setImageResource(gameController.getGameState().getPublicCards()[1].getDrawable());
            btn_publicCardsLeft.setImageResource(gameController.getGameState().getPublicCards()[2].getDrawable());
        }

        //cardPoints = gameController.getGameState().getNowTurnPlayer().getCardPoints();
        //img_points.setText(""+cardPoints);
    }


    public void firstDeal(){

         img_nowTurnPlayer =  (TextView) findViewById(R.id.TextViewNowTurnPlayer);
         img_nextTurnPlayer = (TextView) findViewById(R.id.TextViewNextTurnPlayer);
         textView_publicCards = (TextView) findViewById(R.id.textViewPublicCards);
         textView_nowPlayerLabel = (TextView) findViewById(R.id.textViewCaptionNowTurnPlayer);
         textView_nextPlayerLabel = (TextView) findViewById(R.id.textViewCaptionNextTurnPlayer);
         img_points = (TextView) findViewById(R.id.textView_points);
         btn_publicCardsRight = (ImageButton) findViewById(R.id.publicCardsRight);
         btn_publicCardsMiddle = (ImageButton) findViewById(R.id.publicCardsMiddle);
         btn_publicCardsLeft = (ImageButton) findViewById(R.id.publicCardsLeft);
         btn_ownCardsRight = (ImageButton) findViewById(R.id.ownCardsRight);
         btn_ownCardsMiddle = (ImageButton) findViewById(R.id.ownCardsMiddle);
         btn_ownCardsLeft = (ImageButton) findViewById(R.id.ownCardsLeft);
         btn_next = (Button) findViewById(R.id.buttonNext);
         btn_KeepCardsYes = (Button) findViewById(R.id.buttonKeepCardsYes);
         btn_KeepCardsNo = (Button) findViewById(R.id.buttonKeepCardsNo);
         swapCards = (TextView) findViewById(R.id.textView_wantToKeepCards);

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
        btn_ownCardsLeft.setClickable(false);
        btn_ownCardsRight.setClickable(false);
        btn_ownCardsMiddle.setClickable(false);
        btn_publicCardsLeft.setClickable(false);
        btn_publicCardsMiddle.setClickable(false);
        btn_publicCardsRight.setClickable(false);

        gameController.dealingOutCards();
        returnGameStateToServer();


        if(this.gameController.getGameState().getNowTurnPlayer().getName().equals(this.currentPlayer.getName())){
            btn_ownCardsRight.setImageResource(gameController.getGameState().getNowTurnPlayer().getCards()[0].getDrawable());
            btn_ownCardsMiddle.setImageResource(gameController.getGameState().getNowTurnPlayer().getCards()[1].getDrawable());
            btn_ownCardsLeft.setImageResource(gameController.getGameState().getNowTurnPlayer().getCards()[2].getDrawable());
        }
        else{
            btn_KeepCardsYes.setVisibility(View.INVISIBLE);
            btn_KeepCardsNo.setVisibility(View.INVISIBLE);
            swapCards.setVisibility(View.INVISIBLE);

            for (Player i : this.gameController.getGameState().getPlayers()){
                if(i.getName().equals(currentPlayer.getName())){
                    btn_ownCardsRight.setImageResource(i.getCards()[0].getDrawable());
                    btn_ownCardsMiddle.setImageResource(i.getCards()[1].getDrawable());
                    btn_ownCardsLeft.setImageResource(i.getCards()[2].getDrawable());
                }
            }

        }

    }


    public void switchCardsOwn(View v){
        if(!moveDone && !allOrNothing()) {
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

    public void showResult (View v){
        Intent intent = new Intent(this, ResultActivity.class );
        startActivity(intent);
    }

    public void switchCardsPublic(View v){
        if(!moveDone && !allOrNothing()) {
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

         img_nowTurnPlayer =  (TextView) findViewById(R.id.TextViewNowTurnPlayer);
         img_nextTurnPlayer = (TextView) findViewById(R.id.TextViewNextTurnPlayer);
         img_points = (TextView) findViewById(R.id.textView_points);
         btn_publicCardsRight = (ImageButton) findViewById(R.id.publicCardsRight);
         btn_publicCardsMiddle = (ImageButton) findViewById(R.id.publicCardsMiddle);
         btn_publicCardsLeft = (ImageButton) findViewById(R.id.publicCardsLeft);
         btn_ownCardsRight = (ImageButton) findViewById(R.id.ownCardsRight);
         btn_ownCardsMiddle = (ImageButton) findViewById(R.id.ownCardsMiddle);
         btn_ownCardsLeft = (ImageButton) findViewById(R.id.ownCardsLeft);
         btn_next = (Button) findViewById(R.id.buttonNext);
        RelativeLayout textView_GameActivity = (RelativeLayout) findViewById(R.id.textView_GameActivity);


        if(     stop==true && gameController.getGameState().getPlayers().
                indexOf(gameController.getGameState().getNextTurnPlayer()) == stopPosition
                //gameController.getGameState().getStopPlayer() != null &&
                //gameController.getGameState().getStopPlayer().getName().equals(gameController.getGameState().getNextTurnPlayer())
                ){

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
            //textView_GameActivity.setClickable(true);
            returnGameStateToServer();

        }
        else {
            gameController.doStep();
            moveDone = false;
            updateButtons();
            gameController.getGameState().incCounter();
            returnGameStateToServer();
        }
    }

    public boolean allOrNothing (){

//TODO: alles >=24 ist alles oder Nichts
        if((gameController.getGameState().getPublicCards()[0].getColor()==
                 gameController.getGameState().getPublicCards()[1].getColor())  &&
           (gameController.getGameState().getPublicCards()[1].getColor() ==
                gameController.getGameState().getPublicCards()[2].getColor())
                ){
            return true;
        }
        return false;

    }

    protected void onResume() {
        super.onResume();
        sensorMan.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
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
        //test
        RelativeLayout textView_GameActivity = (RelativeLayout) findViewById(R.id.textView_GameActivity);
        textView_GameActivity.setClickable(false);

        String playerName = getIntent().getStringExtra("playerName");
        gameController = new GameController((GameState) getIntent().getSerializableExtra("gameState"));

        currentPlayer = gameController.getGameState().getPlayerByName(playerName);
        FeuernHelper.gameController = this.gameController;

        // if listenerThread == null then client else server
        listenerThread = ApplicationController.getListenerThread();
        clientThread = ApplicationController.getClientThread();

        sensorMan = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        initializeUiHandler();

        firstDeal();



        btn_publicCardsRight.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!moveDone && allOrNothing()) {
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
                if (!moveDone && gameController.getGameState().getCounter() >=
                        gameController.getGameState().getPlayers().size()) {
                    stop = true;
                    //gameController.getGameState().setStopPlayer(currentPlayer);
                    stopPosition = gameController.getGameState().getCounter() + gameController.getGameState().getPlayers().size();
                    gameController.getGameState().setStopPosition(stopPosition);
                    //stopPosition = gameController.stop();
                    gameController.getGameState().setStop(true);
                    btn_next.setText("Stop gedrückt!");
                    returnGameStateToServer();
                }
                return true;
            }
        });

    }

    public void KeepCardsYes(View v){

         swapCards = (TextView) findViewById(R.id.textView_wantToKeepCards);
         img_nowTurnPlayer =  (TextView) findViewById(R.id.TextViewNowTurnPlayer);
         img_nextTurnPlayer = (TextView) findViewById(R.id.TextViewNextTurnPlayer);
         textView_publicCards = (TextView) findViewById(R.id.textViewPublicCards);
         textView_nowPlayerLabel = (TextView) findViewById(R.id.textViewCaptionNowTurnPlayer);
         textView_nextPlayerLabel = (TextView) findViewById(R.id.textViewCaptionNextTurnPlayer);
         img_points = (TextView) findViewById(R.id.textView_points);
         btn_publicCardsRight = (ImageButton) findViewById(R.id.publicCardsRight);
         btn_publicCardsMiddle = (ImageButton) findViewById(R.id.publicCardsMiddle);
         btn_publicCardsLeft = (ImageButton) findViewById(R.id.publicCardsLeft);
         btn_ownCardsRight = (ImageButton) findViewById(R.id.ownCardsRight);
         btn_ownCardsMiddle = (ImageButton) findViewById(R.id.ownCardsMiddle);
         btn_ownCardsLeft = (ImageButton) findViewById(R.id.ownCardsLeft);
         btn_next = (Button) findViewById(R.id.buttonNext);
         btn_KeepCardsYes = (Button) findViewById(R.id.buttonKeepCardsYes);
         btn_KeepCardsNo = (Button) findViewById(R.id.buttonKeepCardsNo);

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
        returnGameStateToServer();

    }

    public void KeepCardsNo(View v){

         swapCards = (TextView) findViewById(R.id.textView_wantToKeepCards);
         img_nowTurnPlayer =  (TextView) findViewById(R.id.TextViewNowTurnPlayer);
         img_nextTurnPlayer = (TextView) findViewById(R.id.TextViewNextTurnPlayer);
         textView_publicCards = (TextView) findViewById(R.id.textViewPublicCards);
         textView_nowPlayerLabel = (TextView) findViewById(R.id.textViewCaptionNowTurnPlayer);
         textView_nextPlayerLabel = (TextView) findViewById(R.id.textViewCaptionNextTurnPlayer);
         img_points = (TextView) findViewById(R.id.textView_points);
         btn_publicCardsRight = (ImageButton) findViewById(R.id.publicCardsRight);
         btn_publicCardsMiddle = (ImageButton) findViewById(R.id.publicCardsMiddle);
         btn_publicCardsLeft = (ImageButton) findViewById(R.id.publicCardsLeft);
         btn_ownCardsRight = (ImageButton) findViewById(R.id.ownCardsRight);
         btn_ownCardsMiddle = (ImageButton) findViewById(R.id.ownCardsMiddle);
         btn_ownCardsLeft = (ImageButton) findViewById(R.id.ownCardsLeft);
         btn_next = (Button) findViewById(R.id.buttonNext);
         btn_KeepCardsYes = (Button) findViewById(R.id.buttonKeepCardsYes);
         btn_KeepCardsNo = (Button) findViewById(R.id.buttonKeepCardsNo);

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
        returnGameStateToServer();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        sensorMan = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

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
        if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeUiHandler()
    {
        uiHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == Operations.UpdateGameState.getValue()) {
                    updateGameState((GameState) msg.obj);
                }
               /* else if (msg.what == Operations.MakeToast.getValue()) {
                    if ((String)msg.obj != null)
                        //Toast.makeText(getApplicationContext(), (String)msg.obj, Toast.LENGTH_SHORT).show();
                }*/

                super.handleMessage(msg);
            }
        };

        clientThread.updateUIHandler(uiHandler);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
            if (!moveDone && gameController.getGameState().getCounter() >=
                    gameController.getGameState().getPlayers().size()) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    mGravity = event.values.clone();
                    // Shake detection
                    float x = mGravity[0];
                    float y = mGravity[1];
                    float z = mGravity[2];
                    mAccelLast = mAccelCurrent;
                    mAccelCurrent = FloatMath.sqrt(x * x + y * y + z * z);
                    float delta = mAccelCurrent - mAccelLast;
                    mAccel = mAccel * 0.9f + delta;
                    // Make this higher or lower according to how much
                    // motion you want to detect

                    if (mAccel > 8 && flag) {
                        flag = false; // dont go in again
                        stop = true;
                        //gameController.getGameState().setStopPlayer(currentPlayer);
                        stopPosition = gameController.getGameState().getCounter() + gameController.getGameState().getPlayers().size();
                        gameController.getGameState().setStopPosition(stopPosition);
                        //stopPosition = gameController.stop();
                        gameController.getGameState().setStop(true);
                        btn_next.setText("Stop durch Geste!");
                        Toast.makeText(getApplicationContext(), "Stop durch Geste!", Toast.LENGTH_SHORT).show();

                        this.next(null);
                    }
                }
            }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void updateGameState(GameState gameState)
    {
        // TODO: update UI according to new gamestate object

        this.gameController.setGamestate(gameState);

        TextView img_nowTurnPlayer =  (TextView) findViewById(R.id.TextViewNowTurnPlayer);
        TextView img_nextTurnPlayer = (TextView) findViewById(R.id.TextViewNextTurnPlayer);
        TextView img_captionnextTurnPlayer = (TextView) findViewById(R.id.textViewCaptionNextTurnPlayer);
        TextView img_captionnowTurnPlayer = (TextView) findViewById(R.id.textViewCaptionNowTurnPlayer);
        TextView txt_publicCards = (TextView) findViewById(R.id.textViewPublicCards);
        TextView txt_ownCards = (TextView) findViewById(R.id.textViewOwnCards);
        ImageButton btn_publicCardsRight = (ImageButton) findViewById(R.id.publicCardsRight);
        ImageButton btn_publicCardsMiddle = (ImageButton) findViewById(R.id.publicCardsMiddle);
        ImageButton btn_publicCardsLeft = (ImageButton) findViewById(R.id.publicCardsLeft);
        ImageButton btn_ownCardsRight = (ImageButton) findViewById(R.id.ownCardsRight);
        ImageButton btn_ownCardsMiddle = (ImageButton) findViewById(R.id.ownCardsMiddle);
        ImageButton btn_ownCardsLeft = (ImageButton) findViewById(R.id.ownCardsLeft);
        Button btn_next = (Button) findViewById(R.id.buttonNext);
        TextView img_points = (TextView) findViewById(R.id.textView_points);

        //current player
        if(this.gameController.getGameState().getNowTurnPlayer().getName().equals(this.currentPlayer.getName())){
            btn_ownCardsRight.setVisibility(View.VISIBLE);
            btn_ownCardsMiddle.setVisibility(View.VISIBLE);
            btn_ownCardsLeft.setVisibility(View.VISIBLE);
            btn_publicCardsRight.setVisibility(View.VISIBLE);
            btn_publicCardsMiddle.setVisibility(View.VISIBLE);
            btn_publicCardsLeft.setVisibility(View.VISIBLE);
            img_points.setVisibility(View.VISIBLE);
            btn_next.setVisibility(View.VISIBLE);
            img_nowTurnPlayer.setVisibility(View.VISIBLE);
            img_nextTurnPlayer.setVisibility(View.VISIBLE);
            img_captionnextTurnPlayer.setVisibility(View.VISIBLE);
            img_captionnowTurnPlayer.setVisibility(View.VISIBLE);
            txt_ownCards.setVisibility(View.VISIBLE);
            txt_publicCards.setVisibility(View.VISIBLE);
            btn_publicCardsRight.setClickable(true);
            btn_publicCardsMiddle.setClickable(true);
            btn_publicCardsLeft.setClickable(true);
            btn_ownCardsRight.setClickable(true);
            btn_ownCardsMiddle.setClickable(true);
            btn_ownCardsLeft.setClickable(true);
            btn_next.setClickable(true);
        }
        //another player
        else{
            btn_ownCardsRight.setVisibility(View.VISIBLE);
            btn_ownCardsMiddle.setVisibility(View.VISIBLE);
            btn_ownCardsLeft.setVisibility(View.VISIBLE);
            btn_publicCardsRight.setVisibility(View.VISIBLE);
            btn_publicCardsMiddle.setVisibility(View.VISIBLE);
            btn_publicCardsLeft.setVisibility(View.VISIBLE);
            img_points.setVisibility(View.VISIBLE);
            img_nowTurnPlayer.setVisibility(View.VISIBLE);
            img_nextTurnPlayer.setVisibility(View.VISIBLE);
            img_captionnextTurnPlayer.setVisibility(View.VISIBLE);
            img_captionnowTurnPlayer.setVisibility(View.VISIBLE);
            txt_ownCards.setVisibility(View.VISIBLE);
            txt_publicCards.setVisibility(View.VISIBLE);
            btn_publicCardsRight.setClickable(false);
            btn_publicCardsMiddle.setClickable(false);
            btn_publicCardsLeft.setClickable(false);
            btn_ownCardsRight.setClickable(false);
            btn_ownCardsMiddle.setClickable(false);
            btn_ownCardsLeft.setClickable(false);
            btn_next.setClickable(false);
            if(gameState.isStop()){
                btn_next.setText("Stop gedrückt!");
            }

        }
        updateButtons();
    }
}