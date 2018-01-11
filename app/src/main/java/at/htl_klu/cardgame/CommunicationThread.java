package at.htl_klu.cardgame;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by electronics on 06.01.2018.
 */

public class CommunicationThread extends Thread  {
    private String ip;
    private int port;
    private boolean part;
    private Socket socket;
    private ServerSocket serverSocket;
    private DataOutputStream out;
    private DataInputStream in;
    private String command;
    private boolean connected = false;


    public CommunicationThread(String ip, int port) {
        this.ip = ip;
        this.port = port;
       // this.part = part;
    }

    public void setCommand(String command) {
        this.command = command;
    }
    public void setPart(Boolean part) {
        this.part = part;
    }

    public void sendCommand() {
        try {
            if (this.command.equals("0") ) {
            //    out.write(this.command);

                out.flush();
             //   this.command = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        connect();
        while (true) {
            try {
                Thread.sleep(500);
                //sendCommand();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void connect() {
        try {
            if(part) {
                Log.d("Peta", "Trying to connect to " + ip + " on port " + port);
                socket = new Socket(ip, port);
                Log.d("Peta", "Connection established");
                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());
                this.setConnected(true);
            }else if (part == false){
                serverSocket = new ServerSocket(port);
                socket = serverSocket.accept();
                Log.d("Peta", "Connection established");
                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());
                this.setConnected(true);

            }
        } catch (IOException e) {
            Log.d("Peta", "Failed to connect");
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
