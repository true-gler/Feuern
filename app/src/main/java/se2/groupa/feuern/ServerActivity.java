package se2.groupa.feuern;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.net.InetAddress;
import java.util.concurrent.ExecutionException;

import se2.groupa.feuern.controller.ServerController;
import se2.groupa.feuern.network.IPAddressTask;
import se2.groupa.feuern.network.ListenerThread;


public class ServerActivity extends Activity {

    private EditText textViewServername;
    private Switch switchStartStopServer;
    private TextView tvServerIpAddress;
    private ServerController serverController;


    private Thread serverThread = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        tvServerIpAddress = (TextView) findViewById(R.id.tvServerIpAddress);

        try {
            tvServerIpAddress.setText("Your IP: " + new IPAddressTask().execute().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        switchStartStopServer = (Switch) findViewById(R.id.switchStartStopServer);
        switchStartStopServer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    textViewServername.setEnabled(false);

                    serverController = new ServerController(textViewServername.getText().toString());

                    // start server
                    serverThread = new Thread(new ListenerThread(textViewServername.getText().toString(), serverController));
                    serverThread.start();

                    // TODO: update ui

                } else {
                    textViewServername.setEnabled(true);

                    if (serverThread != null)
                        serverThread.interrupt();
                }
            }
        });

        textViewServername = (EditText)findViewById(R.id.textServername);
        textViewServername.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {

                if (s != null && s.length() > 0)
                    switchStartStopServer.setEnabled(true);
                else
                    switchStartStopServer.setEnabled(false);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
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

    public void startGame(View view){
        Intent intent= new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}
