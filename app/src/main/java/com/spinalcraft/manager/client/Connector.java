package com.spinalcraft.manager.client;

import com.spinalcraft.easycrypt.messenger.MessageReceiver;

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
                if(auth.hasAccess()) {
                    System.out.println("Have access! Secret Key: " + (new Crypt()).stringFromSecretKey(auth.getSecretKey()));
                }
                else{
                    System.out.println("Requesting access...");
                    auth.acquireAccess(reader, printer, "derp");
                }
            }
            else{
                System.out.println("Not Connected!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
