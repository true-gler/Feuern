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
public class ServerThread implements Runnable {

    private String servername;
    private Socket clientSocket;
    private ServerController serverController;
    private Handler uiHandler;
    private String currentPlayerName;

    private BufferedReader input;
    private BufferedWriter output;

    private boolean isRunning;

    public ServerThread(Socket clientSocket, String servername, ServerController serverController, Handler uiHandler) {

        this.servername = servername;
        this.clientSocket = clientSocket;
        this.serverController = serverController;
        this.uiHandler = uiHandler;

        try {

            this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            this.output = new BufferedWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {


        try {
            isRunning = true;
            //output.write("--------------------------------------\n");
            //output.write("Welcome to FEUERN\n");
            //output.write("Server: " + servername + "\n");
            //output.write("IP-Address: " + clientSocket.getInetAddress().getHostAddress() + "\n");
            //output.write("--------------------------------------\n");
            output.write("[OK] connection established");
            output.flush();

            while (isRunning) {

                String read = input.readLine();

                if (read.equals("server"))
                {
                    output.write("[OK] " + servername);
                }
                else if (read.startsWith("registerAsPlayer"))
                {
                    String[] str = read.split(" ");
                    String playerName = str[1];

                    if (serverController.addPlayer(playerName)) {
                        output.write("[OK] player successfully registered");

                        Message msg = uiHandler.obtainMessage();
                        msg.what = ServerControllerOperation.AddPlayer.getValue();
                        msg.obj = serverController.getPlayer(playerName);
                        uiHandler.sendMessage(msg);

                        currentPlayerName = playerName;
                    }
                    else
                    {
                        output.write("[ERR] playername already exists");
                    }
                }
                else if (read.startsWith("unregister"))
                {
                    String playerName = currentPlayerName;

                    if (playerName == null || playerName.isEmpty()) {
                        String[] str = read.split(" ");
                        playerName = str[1];
                    }

                    if (serverController.deletePlayer(playerName)) {
                        output.write("[OK] successfully unregistered");

                        Message msg = uiHandler.obtainMessage();
                        msg.what = ServerControllerOperation.RemovePlayer.getValue();
                        msg.obj = playerName;
                        uiHandler.sendMessage(msg);
                    }
                    else
                    {
                        output.write("[ERR] could not unregister");
                    }
                }
                else if (read.equals("bye") || read.equals("quit"))
                {
                    isRunning = false;
                    output.write("[OK] Bye");
                }
                else if (read.equals("help") || read.equals("?"))
                {
                    output.write("[OK] available commands: server, bye, help");
                }
                else
                {
                    output.write("[ERR] invalid command");
                }

                output.write("\n");
                output.newLine();
                output.flush();
            }

            // close connections
            input.close();
            output.close();
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            input = null;
            output = null;
            clientSocket = null;
        }
    }
}