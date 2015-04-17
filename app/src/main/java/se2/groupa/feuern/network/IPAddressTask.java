package se2.groupa.feuern.network;

import android.os.AsyncTask;

import java.net.InetAddress;

/**
 * Created by Lukas on 13.04.15.
 */
public class IPAddressTask extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... strings) {
        InetAddress addr;

        try {
            addr = InetAddress.getLocalHost();
            return addr.getHostAddress();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
