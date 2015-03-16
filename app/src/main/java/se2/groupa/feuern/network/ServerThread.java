package se2.groupa.feuern.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Lukas on 16.03.15.
 */
public class ServerThread implements Runnable {


    private String servername;
    private ServerSocket serverSocket;
    private static final int SERVERPORT = 8888;

    public ServerThread(String servername) {

        this.servername = servername;
    }

    public void run() {

        Socket socket = null;

        try {
            serverSocket = new ServerSocket(SERVERPORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!Thread.currentThread().isInterrupted()) {

            try {

                /*
                To use telnet from localhost:

                configuration (tcp redirect to emulator):
                    telnet localhost 5554
                    redir add tcp:8888:8888
                    quit

                connect to server:
                    telnet localhost 8888
                 */

                socket = serverSocket.accept();
                PlayerThread client = new PlayerThread(socket, servername);

                new Thread(client).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
