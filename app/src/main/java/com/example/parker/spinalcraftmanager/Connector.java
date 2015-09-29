package com.example.parker.spinalcraftmanager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Connector implements Runnable {
    @Override
    public void run(){
        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress("192.168.0.14", 9494), 5000);
            if(socket.isConnected()){
                System.out.println("Connected!");
            }
            else{
                System.out.println("Not Connected!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
