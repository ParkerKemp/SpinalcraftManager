package com.spinalcraft.manager.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Connector implements Runnable {
    @Override
    public void run(){
        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress("mc.spinalcraft.com", 9494), 5000);
            if(socket.isConnected()){
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
