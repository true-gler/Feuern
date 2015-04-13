package se2.groupa.feuern;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.ImageButton;

import java.util.ArrayList;

import se2.groupa.feuern.GameActivity;
import se2.groupa.feuern.controller.GameController;
import se2.groupa.feuern.others.Card;
import se2.groupa.feuern.others.CardDeck;
import se2.groupa.feuern.others.Player;

/**
 * Created by Markus on 31.03.2015.
 */
public class GameActivityTest extends ActivityInstrumentationTestCase2<GameActivity> {

    //GameActivity gameactivity;


    public GameActivityTest() {
        super(GameActivity.class);

    }


    /*
    protected void setUp() throws Exception {
        super.setUp();
        //setActivityInitialTouchMode(false);
        gameactivity = getActivity();

    }
    */



    /*
    @SmallTest
    public void test1 (){
        this.gameactivity =  new GameActivity();
        gameactivity = getActivity();
    }
    */

    @SmallTest
    public void test_doStep(){
        GameActivity gameactivity = new GameActivity();
        gameactivity = getActivity();

    }




    /*
    @SmallTest
    public void testDealingOutCards(){
        this.gameactivity = new GameActivity();
        gameactivity = getActivity();
        Player player1 = new Player(1, "playerOne");
        Player player2 = new Player(2, "playerTwo");
        Player player3 = new Player(3, "playerThree");
        Player player4 = new Player(4, "playerFour");
        ArrayList<Player> players = new ArrayList<Player>(4);
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        GameController gameController = new GameController(players);
        gameController.dealingOutCards();

    }
    */


}
