package com.spinalcraft.manager.client;

import com.spinalcraft.easycrypt.messenger.MessageReceiver;
import com.spinalcraft.easycrypt.messenger.MessageSender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.GeneralSecurityException;

public class Connector implements Runnable {
    Authenticator auth;

    public Connector(MainActivity activity){
        auth = new Authenticator(activity);
    }

    @Override
    public void run(){
        Socket socket = connectToServer();
        try {
            if(socket.isConnected()){
                if(auth.hasAccess()) {
                    System.out.println("Have access! Secret Key: " + (new Crypt()).stringFromSecretKey(auth.getSecretKey()));
                }
                else{
                    System.out.println("Requesting access...");
                    auth.acquireAccess(socket, "GIMME");
                }
                if(auth.hasAccess())
                    sendTestMessage();
            }
            else{
                System.out.println("Not Connected!");
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    private Socket connectToServer(){
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress("mc.spinalcraft.com", 9494), 5000);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return socket;
    }

    private void sendTestMessage(){
        try {
            Socket socket = connectToServer();
            Crypt crypt = new Crypt();
            MessageSender sender = new Sender(socket, crypt);
            sender.addHeader("intent", "message");
            sender.setIdentifier(crypt.stringFromPublicKey(auth.getPublicKey()));
            sender.addHeader("publicKey", crypt.stringFromPublicKey(auth.getPublicKey()));
            sender.addItem("message", "YO WHAT UP");
            sender.sendEncrypted(auth.getSecretKey());

            MessageReceiver receiver = new Receiver(socket, crypt, auth);
            receiver.receiveMessage();
            System.out.println("Got back: " + receiver.getItem("message"));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
