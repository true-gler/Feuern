package se2.groupa.feuern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import se2.groupa.feuern.adapters.PlayerAdapter;
import se2.groupa.feuern.controller.ServerController;
import se2.groupa.feuern.model.Player;
import se2.groupa.feuern.network.ClientThread;
import se2.groupa.feuern.network.CommunicationCommand;
import se2.groupa.feuern.network.ListenerThread;


public class ServerActivity extends Activity {

    private EditText textViewServername;
    private Switch switchStartStopServer;
    private TextView tvServerIpAddress;
    private ServerController serverController;
    private ListView listViewPlayers;
    private ArrayList<Player> currentPlayers;
    private PlayerAdapter listViewPlayerAdapter;
    private Button btnStartGame;
    private String playerName;


    private Thread parentThread = null;
    private ListenerThread listenerThread;
    private Thread parentClientThread = null;
    private ClientThread clientThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        tvServerIpAddress = (TextView) findViewById(R.id.tvServerIpAddress);
        listViewPlayers = (ListView) findViewById(R.id.listViewPlayers);
        btnStartGame = (Button) findViewById(R.id.btnStartGame);
        btnStartGame.setEnabled(false);

        tvServerIpAddress.setText("Share your IP: " + getIpAddress());

        currentPlayers = new ArrayList<Player>();
        listViewPlayerAdapter = new PlayerAdapter(this, currentPlayers);
        listViewPlayers.setAdapter(listViewPlayerAdapter);

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

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ServerControllerOperation.AddPlayer.getValue()) {
                    if ((Player) msg.obj != null) {
                        addPlayer((Player) msg.obj);
                    }
                }
                if (msg.what == ServerControllerOperation.RemovePlayer.getValue()) {
                    if ((String) msg.obj != null) {
                        removePlayer((String) msg.obj);
                    }
                }
                super.handleMessage(msg);
            }
        };

        switchStartStopServer = (Switch) findViewById(R.id.switchStartStopServer);
        switchStartStopServer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    textViewServername.setEnabled(false);

                    serverController = new ServerController(textViewServername.getText().toString());
                    listenerThread = new ListenerThread(textViewServername.getText().toString(), serverController, handler);
                    // start server
                    parentThread = new Thread(listenerThread);
                    parentThread.start();

                    clientThread = new ClientThread(getIpAddress(), 8888, textViewServername.getText().toString(), playerName, handler);
                    parentClientThread = new Thread(clientThread);
                    parentClientThread.start();

                } else {
                    textViewServername.setEnabled(true);

                    if (parentThread != null && parentThread.isAlive() && listenerThread != null) {
                        listenerThread.shutdown();
                        listViewPlayerAdapter.clear();
                    }
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startGame(View view) {


        if (parentThread != null && parentThread.isAlive() && listenerThread != null) {
            listenerThread.broadcastCommand(CommunicationCommand.StartGame);
        }

        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void addPlayer(Player player) {
        listViewPlayerAdapter.add(player);
        updateEnabledOfStartGameButton();
    }

    public void removePlayer(String playerName) {

        Player playerToRemove = null;

        for (Player activePlayer : currentPlayers) {
            if (activePlayer.getName().equals(playerName))
                playerToRemove = activePlayer;
        }

        listViewPlayerAdapter.remove(playerToRemove);
        updateEnabledOfStartGameButton();
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

    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += inetAddress.getHostAddress();
                    }

                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }
}