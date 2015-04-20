package se2.groupa.feuern.network.threads;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

import se2.groupa.feuern.network.classes.CommunicationCommand;
import se2.groupa.feuern.network.classes.NetworkMessage;
import se2.groupa.feuern.network.classes.Operations;

/**
 * Created by Lukas on 16.03.15.
 *
 * One instance of this thread will run on each client (also on that client where the server is hosted)
 */
public class ClientThread implements Runnable {

    private String serverName;
    private Socket clientSocket;
    private Handler clientUiHandler;
    private String currentPlayerName;
    private String ipAddress;
    private int port;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private boolean isRunning;
    private boolean isAlsoServer;

    public ClientThread(String ipAddress, int port, String serverName, String currentPlayerName, Handler clientUiHandler, boolean isAlsoServer) {

        this.ipAddress = ipAddress;
        this.port = port;
        this.serverName = serverName;
        this.clientUiHandler = clientUiHandler;
        this.currentPlayerName = currentPlayerName;
        this.isAlsoServer = isAlsoServer;
    }

    @Override
    public void run() {

        try {
            if (isAlsoServer) { // without sleep client could connect to server to early --> server not started yet --> connection refused
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            clientSocket = new Socket(ipAddress, port);

            output = new ObjectOutputStream(clientSocket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(clientSocket.getInputStream());

            NetworkMessage read = (NetworkMessage) input.readObject();

            if (read.toString().startsWith("[OK]")) // connection established
            {
                output.writeObject(new NetworkMessage(NetworkMessage.Status.REQUEST, CommunicationCommand.Register.toString() + " " + currentPlayerName, null));
                output.flush();

                read = (NetworkMessage) input.readObject();

                if (read.toString().startsWith("[OK]")) // registration succeeded
                {
                    if (!isAlsoServer)
                    {
                        Message msg = clientUiHandler.obtainMessage();
                        msg.what = Operations.SetConnected.getValue();
                        msg.obj = null;
                        clientUiHandler.sendMessage(msg);
                    }

                    isRunning = true;

                    while (isRunning)
                    {
                        try {
                            read = (NetworkMessage) input.readObject();

                            if (read == null) // server is unavailable
                            {
                                isRunning = false;
                            } else if (read.toString().startsWith("[" + NetworkMessage.Status.REQUEST.toString() + "] " + CommunicationCommand.StartGame.toString())) {
                                Message msg = clientUiHandler.obtainMessage();
                                msg.what = Operations.StartGame.getValue();
                                msg.obj = null;
                                clientUiHandler.sendMessage(msg);
                            } else if (read.toString().startsWith("[" + NetworkMessage.Status.REQUEST.toString() + "] " + CommunicationCommand.UpdatePlayers.toString())) {
                                Message msg = clientUiHandler.obtainMessage();
                                msg.what = Operations.UpdatePlayers.getValue();
                                msg.obj = read.getParameter();
                                clientUiHandler.sendMessage(msg);
                            } else { // bad command

                            }
                        } catch (Exception e){
                            e.printStackTrace();

                            Message msg = clientUiHandler.obtainMessage();
                            msg.what = Operations.MakeToast.getValue();
                            msg.obj = e.getMessage();
                            clientUiHandler.sendMessage(msg);
                        }
                    }
                }
                else
                {
                    // could not register
                }
            }
            else
            {
                // could not connect to server
            }

            if (input != null)
                input.close();
            if (output != null)
                output.close();
            if (clientSocket != null)
                clientSocket.close();
        } catch (ConnectException e) {
            e.printStackTrace();

            Message msg = clientUiHandler.obtainMessage();
            msg.what = Operations.MakeToast.getValue();
            msg.obj = e.getMessage();
            clientUiHandler.sendMessage(msg);

            if (isAlsoServer) // close Server because local player could not join
            {
                try {
                    Thread.sleep(500); // just for slider in UI
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                Message m = clientUiHandler.obtainMessage();
                m.what = Operations.StopServer.getValue();
                m.obj = null;
                clientUiHandler.sendMessage(m);
            }

        } catch (IOException e) {
            e.printStackTrace();

            Message msg = clientUiHandler.obtainMessage();
            msg.what = Operations.MakeToast.getValue();
            msg.obj = e.getMessage();
            clientUiHandler.sendMessage(msg);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        isRunning = false;

        try {
            if (clientSocket != null)
                clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();

            Message msg = clientUiHandler.obtainMessage();
            msg.what = Operations.MakeToast.getValue();
            msg.obj = e.getMessage();
            clientUiHandler.sendMessage(msg);
        }
    }

    public void executeCommand(CommunicationCommand command, Object parameter)
    {
        try {
            if (output != null) {
                output.writeObject(new NetworkMessage(NetworkMessage.Status.REQUEST, command.toString(), parameter));
                output.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();

            Message msg = clientUiHandler.obtainMessage();
            msg.what = Operations.MakeToast.getValue();
            msg.obj = e.getMessage();
            clientUiHandler.sendMessage(msg);
        }
    }
}

