package at.htl_klu.cardgame;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private int command =0;
    private boolean connected = false;
    private boolean playerhasturn = false;
    private boolean enemyhasturn = false;
    private int incomigData = 0;
    private GamePanel gamepanel;
    private boolean test = false;

    public CommunicationThread(String ip, int port, GamePanel panel) {
        this.ip = ip;
        this.port = port;
       // this.part = part;
        this.gamepanel = panel;
    }

    public void setCommand(int command) {
        this.command = command;
        this.test = true;
    }
    public void setPart(Boolean part) {
        this.part = part;
    }

    public void sendCommand() {
        try {
            if(playerhasturn == true || test){
                Log.d("werner","playerhasturn true");
             if (this.command !=0 ) {
                 Log.d("werner", "command not null true");

                if (this.command == 1) {
                    if(gamepanel.getOpponenthandcounter() < 7)
                    gamepanel.setOpponenthandcounter(gamepanel.getOpponenthandcounter()+1);
                 }

                     Log.d("werner", "equals endturn true");

                     //    out.write(this.command);
                     //      if (this.command.equals("endturn")) {
                     out.writeInt(this.command);
                     out.flush();
                     //   }

                     this.command = 0;
                     test = false;

                // }
             }
            }


                Log.d("werner", "---------" + enemyhasturn);

             //   if (enemyhasturn == true || !test){

            while(enemyhasturn){
                    Log.d("werner","noch bevor dem readUTF");

                try {
                    incomigData = in.read();
                    Log.d("werner", "*********" + incomigData);

                    if (incomigData == 1) {
                        gamepanel.setPlayerhasturn(true);
                        gamepanel.setEnemyhasturn((false));
                        this. enemyhasturn = false;
                        this.playerhasturn = true;

                        for(int i = 0; i < 7; i++) {
                            if (gamepanel.getHandCardsName()[i].isFilled() == false) {
                                gamepanel.getHandCardsName()[i] = new HandCard(gamepanel.getDeckCards().get(gamepanel.getDeckremovecounter()), true);
                                gamepanel.getDeckCards().remove(gamepanel.getDeckremovecounter());

                                gamepanel.setDeckremovecounter(gamepanel.getDeckremovecounter() - 1);
                             //   gamepanel.setOpponenthandcounter(gamepanel.getOpponenthandcounter()+1);
                                break;
                            }
                        }
                        incomigData = 0;
                        Log.d("werner", "set new turn");
                    }
                    Log.d("werner","noch bevor dem 6 überprüfen");
                    if(incomigData == 6) {

                        Log.d("werner", "6 erkannt");
                            gamepanel.setOpponenthandcounter(gamepanel.getOpponenthandcounter()-1);
                            incomigData = 0;


                    }
                   // if(incomigData > 1){

                    //}
                }catch (IOException e){
                    e.printStackTrace();
                }


            }


        } catch (IOException e) {
            e.printStackTrace();
            Log.d("werner", "catch triggert peta");
        }
    }

    @Override
    public void run() {
        super.run();
        connect();
        while (true) {
            try {
                Thread.sleep(500);
                sendCommand();
           //     if(enemyhasturn == true) {
             //       incomigData = in.readUTF();
            //    }

            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d("werner", "triggered catchclause");
           // } catch (IOException e) {
          //      e.printStackTrace();
            }
        }
    }

    private void connect() {
        try {
            if(part) {
                Log.d("Peta", "Trying to connect to " + ip + " on port " + port);
                socket = new Socket(ip, port);
                Log.d("Peta", "Connection established");
                connected = true;
                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());
                this.setConnected(true);
            }else if (part == false){
                serverSocket = new ServerSocket(port);
                socket = serverSocket.accept();
                Log.d("Peta", "Connection established");
                connected = true;
                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());
                this.setConnected(true);

            }
        } catch (IOException e) {
            Log.d("Peta", "Failed to connect");
        }
    }

    public boolean isPlayerhasturn() {
        return playerhasturn;
    }

    public void setPlayerhasturn(boolean playerhasturn) {
        this.playerhasturn = playerhasturn;
    }

    public boolean isEnemyhasturn() {
        return enemyhasturn;
    }

    public void setEnemyhasturn(boolean enemyhasturn) {
        this.enemyhasturn = enemyhasturn;
    }

    public int getIncomigData() {
        return incomigData;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
