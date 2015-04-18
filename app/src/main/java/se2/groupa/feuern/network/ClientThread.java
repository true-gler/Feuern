package se2.groupa.feuern.network;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import se2.groupa.feuern.ServerControllerOperation;
import se2.groupa.feuern.controller.ServerController;

/**
 * Created by Lukas on 16.03.15.
 */
public class ClientThread implements Runnable {

    private String serverName;
    private Socket clientSocket;
    private Handler clientUiHandler;
    private String currentPlayerName;
    private String ipAddress;
    private int port;

    private BufferedReader input;
    private BufferedWriter output;

    private boolean isRunning;

    public ClientThread(String ipAddress, int port, String serverName, String currentPlayerName, Handler clientUiHandler) {

        this.ipAddress = ipAddress;
        this.port = port;
        this.serverName = serverName;
        this.clientUiHandler = clientUiHandler;
        this.currentPlayerName = currentPlayerName;
    }

    @Override
    public void run() {

        try {
            clientSocket = new Socket(ipAddress, port);

            input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()));


            String read = input.readLine();

            if (read.startsWith("[OK]")) // connection established
            {
                output.write(CommunicationCommand.Register.toString() + " " + currentPlayerName + "\n");
                output.flush();

                read = input.readLine();


                if (read.startsWith("[OK]")) // registration succeeded
                {
                    read = "juhu";
                }
            }


            // retrieve reply from server
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        isRunning = false;

        try {
            if (output != null) {
                output.write("[IF] Server has been closed\n");
                output.flush();
            }

            if (clientSocket != null)
                clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void executeCommand(CommunicationCommand command)
    {
        try {
            if (output != null) {

                if (command == CommunicationCommand.StartGame)
                {
                    output.write("[RQ] " + command.toString() + "\n");
                    output.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

