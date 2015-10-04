package com.spinalcraft.manager.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Connector implements Runnable {
    Authenticator auth;

    public Connector(MainActivity activity){
        auth = new Authenticator(activity);
    }

    @Override
    public void run(){
        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress("mc.spinalcraft.com", 9494), 5000);
            if(socket.isConnected()){
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintStream printer = new PrintStream(socket.getOutputStream());
                auth.sendAccessRequest(printer, "derp");
                System.out.println("Public key: " + auth.getPublicKey());

                System.out.println("Connected!");
                String response = reader.readLine();
                System.out.println(response);
            }
            else{
                System.out.println("Not Connected!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
