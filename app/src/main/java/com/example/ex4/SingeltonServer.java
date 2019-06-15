package com.example.ex4;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import static android.content.ContentValues.TAG;


public class SingeltonServer {

    private String ip;
    private int port;
    private Socket socket;
    private static volatile SingeltonServer server = null;

    private SingeltonServer() {
    }

    public static SingeltonServer getInstance() {
        if (server == null) {
            server = new SingeltonServer();
        }
        return server;
    }


    public void ConnectToServer(String Mip, int Mport) {
        Runnable runable = new Runnable(){
            @Override
            public void run(){

                try {
                    InetAddress serverAddr = InetAddress.getByName("10.0.2.2");
                    socket = new Socket(serverAddr, 5402);
                } catch (IOException e) {
                    Log.e("TCP", "C: Error", e);
                    System.out.println(e.toString());
                }

            }
        };
        Thread thread = new Thread(runable);
        thread.start();

    }

    public void mainSendToSimulator(float aileronN, float elevatorN) {
        final String aileronS = "set /controls/flight/aileron " + Float.toString(aileronN) + "\r\n";
        final String elevatorS = "set /controls/flight/elevator " + Float.toString(elevatorN) + "\r\n";
        SendingCommand(aileronS);
        SendingCommand(elevatorS);
    }

    public void SendingCommand(final String toSend) {
        final PrintWriter sender;
        try {
            sender = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream())), true);
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    if (sender != null) {
                        Log.d(TAG, "Sending: " + toSend);
                        sender.println(toSend);
                        sender.flush();
                    }
                }
            }; Thread thread = new Thread(r);
            thread.start();

        } catch (IOException e) {
            Log.e("TCP", "C: Error", e);
            System.out.println((e.toString()));
        }
    }

    public void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
            Log.e("TCP", "C: Error", e);
            System.out.println((e.toString()));
        }
    }

}
