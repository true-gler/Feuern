package se2.groupa.feuern;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import se2.groupa.feuern.adapters.PlayerAdapter;
import se2.groupa.feuern.controller.ApplicationController;
import se2.groupa.feuern.controller.ServerController;
import se2.groupa.feuern.model.DynamicListView;
import se2.groupa.feuern.model.GameState;
import se2.groupa.feuern.model.Player;
import se2.groupa.feuern.network.threads.ClientThread;
import se2.groupa.feuern.network.classes.CommunicationCommand;
import se2.groupa.feuern.network.threads.ListenerThread;
import se2.groupa.feuern.network.classes.NetworkHelper;
import se2.groupa.feuern.network.classes.Operations;


public class ServerActivity extends Activity {

    private EditText textViewServername;
    private Switch switchStartStopServer;
    private TextView tvServerIpAddress;
    private ServerController serverController;
    //private ListView listViewPlayers;
    private DynamicListView listViewPlayers;
    private ArrayList<Player> currentPlayers;
    private PlayerAdapter listViewPlayerAdapter;
    private Button btnStartGame;
    private String playerName;

    private Handler handler;
    private Thread parentThread = null;
    private ListenerThread listenerThread;
    private Thread parentClientThread = null;
    private ClientThread clientThread;

    private boolean serverIsRunning = false;

    private GameState gameState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        tvServerIpAddress = (TextView) findViewById(R.id.tvServerIpAddress);
        //listViewPlayers = (ListView) findViewById(R.id.listViewPlayers);
        listViewPlayers = (DynamicListView) findViewById(R.id.dynListView);
        btnStartGame = (Button) findViewById(R.id.btnStartGame);
        btnStartGame.setEnabled(false);

        tvServerIpAddress.setText("Share your IP: " + NetworkHelper.getIPAddress());

        currentPlayers = new ArrayList<Player>();
        listViewPlayerAdapter = new PlayerAdapter(this, currentPlayers);
        listViewPlayers.setAdapter(listViewPlayerAdapter);
        listViewPlayers.setCheeseList(currentPlayers);
        listViewPlayers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                playerName = null;
            } else {
                playerName = extras.getString("PlayerName");
            }
        } else {
            playerName = (String) savedInstanceState.getSerializable("PlayerName");
        }

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == Operations.AddPlayer.getValue()) {
                    if ((Player) msg.obj != null) {
                        addPlayer((Player) msg.obj);
                    }
                }
                else if (msg.what == Operations.RemovePlayer.getValue()) {
                    if ((String) msg.obj != null) {
                        removePlayer((String) msg.obj);
                    }
                }
                else if (msg.what == Operations.StartGame.getValue()) {
                    ShowGameActivity();
                }
                else if (msg.what == Operations.MakeToast.getValue()) {
                   // if ((String)msg.obj != null)
                     //   Toast.makeText(getApplicationContext(), (String)msg.obj, Toast.LENGTH_SHORT).show();
                }
                else if (msg.what == Operations.StopServer.getValue()) {
                    stopServer();
                }

                super.handleMessage(msg);
            }
        };

        switchStartStopServer = (Switch) findViewById(R.id.switchStartStopServer);
        switchStartStopServer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    startServer();
                } else {
                    stopServer();
                }
            }
        });

        textViewServername = (EditText) findViewById(R.id.textServername);
        textViewServername.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s != null && s.length() > 0)
                    switchStartStopServer.setEnabled(true);
                else
                    switchStartStopServer.setEnabled(false);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }

    private void startServer()
    {
        // TODO: move ListenerThread into ServerController? or can be passed ListenerThread to GameActivity
        textViewServername.setEnabled(false);

        serverController = new ServerController(textViewServername.getText().toString());
        listenerThread = new ListenerThread(textViewServername.getText().toString(), serverController, handler);
        // start server
        parentThread = new Thread(listenerThread);
        parentThread.start();

        clientThread = new ClientThread(NetworkHelper.getIPAddress(), NetworkHelper.getPort(), textViewServername.getText().toString(), playerName, handler, true);
        parentClientThread = new Thread(clientThread);
        parentClientThread.start();

        serverIsRunning = true;
    }

    private void stopServer()
    {
        textViewServername.setEnabled(true);
        switchStartStopServer.setChecked(false);

        if (parentClientThread != null && parentClientThread.isAlive() && clientThread != null) {
            clientThread.shutdown();
        }

        if (parentThread != null && parentThread.isAlive() && listenerThread != null) {
            listenerThread.shutdown();
        }

        listViewPlayerAdapter.clear();
        serverIsRunning = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_server, menu);
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
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//NO TITLE :)
            dialog.setContentView(R.layout.about_dialog);
            dialog.setCancelable(true);
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    // fired by button click
    public void startGame(View view) {

        if (parentThread != null && parentThread.isAlive() && listenerThread != null) {
            gameState = new GameState(currentPlayers);
            gameState.setNowTurnPlayer(currentPlayers.get(0));
            gameState.setNextTurnPlayer(currentPlayers.get(1));

            listenerThread.broadcastCommand(CommunicationCommand.StartGame, gameState);
        }
    }

    public void ShowGameActivity()
    {
        Intent intent = new Intent(this, GameActivity.class);

        ApplicationController.setListenerThread(listenerThread);
        ApplicationController.setClientThread(clientThread);
        intent.putExtra("gameState", gameState);
        intent.putExtra("playerName", playerName);

        startActivity(intent);
    }

    public void addPlayer(Player player) {
        listViewPlayerAdapter.add(player);
        updateEnabledOfStartGameButton();
        updatePlayersListOnClients();
    }

    public void removePlayer(String playerName) {

        Player playerToRemove = null;

        for (Player activePlayer : currentPlayers) {
            if (activePlayer.getName().equals(playerName))
                playerToRemove = activePlayer;
        }

        listViewPlayerAdapter.remove(playerToRemove);
        updateEnabledOfStartGameButton();
        updatePlayersListOnClients();
    }

    private void updateEnabledOfStartGameButton()
    {
        if (currentPlayers != null && currentPlayers.size() > 1) {
            btnStartGame.setEnabled(true);
        }
        else {
            btnStartGame.setEnabled(false);
        }
    }

    private void updatePlayersListOnClients()
    {
        if (parentThread != null && parentThread.isAlive() && listenerThread != null) {
            listenerThread.broadcastCommand(CommunicationCommand.UpdatePlayers, currentPlayers);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if(serverIsRunning){

                Toast.makeText(getApplicationContext(), "You need to stop your server first", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed()
    {
        if(serverIsRunning){

            Toast.makeText(getApplicationContext(), "You need to stop your server first", Toast.LENGTH_SHORT).show();
            return;
        }

        super.onBackPressed();
    }
}