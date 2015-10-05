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
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintStream printer = new PrintStream(socket.getOutputStream());
                if(auth.hasAccess()) {
                    System.out.println("Have access! Secret Key: " + (new Crypt()).stringFromSecretKey(auth.getSecretKey()));
                }
                else{
                    System.out.println("Requesting access...");
                    auth.acquireAccess(reader, printer, "GIMME");
                }
                sendTestMessage();
            }
            else{
                System.out.println("Not Connected!");
            }
        } catch (IOException e) {
            e.printStackTrace();
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream printer = new PrintStream(socket.getOutputStream());
            Crypt crypt = new Crypt();
            MessageSender sender = new MessageSender(printer);
            sender.addHeader("intent", "message");
            sender.addHeader("publicKey", crypt.stringFromPublicKey(auth.getPublicKey()));
            sender.addItem("message", "YO WHAT UP");
            sender.sendEncrypted(auth.getSecretKey(), crypt);

            MessageReceiver receiver = new MessageReceiver(reader);
            receiver.receiveMessage();
            if(receiver.needsSecretKey()){
                receiver.decrypt(auth.getSecretKey(), crypt);
            }
            System.out.println("Got back: " + receiver.getItem("message"));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
