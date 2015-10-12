package com.spinalcraft.manager.client;

import com.spinalcraft.berberos.client.ClientAmbassador;
import com.spinalcraft.berberos.common.Ambassador;
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

    public Connector(MainActivity activity){
        
    }

    @Override
    public void run(){
        Socket socket = connectToServer();
        if(socket != null && socket.isConnected()){
            AndroidClient client = new AndroidClient(new Crypt());
            ClientAmbassador ambassador = client.getAmbassador(socket, "username", "password", "manager");
            if(ambassador == null) {
                System.err.println("Authentication failed!");
                return;
            }
//                MessageSender sender = ambassador.getSender();
//                sender.addHeader("intent", "message");
//                sender.addItem("message", "derp");
//                ambassador.sendMessage(sender);
//                MessageReceiver receiver = ambassador.receiveMessage();
//                String response = receiver.getItem()
            sendTestMessage(ambassador);
//                if(auth.hasAccess()) {
//                    System.out.println("Have access! Secret Key: " + (new Crypt()).stringFromSecretKey(auth.getSecretKey()));
//                }
//                else{
//                    System.out.println("Requesting access...");
//                    auth.acquireAccess(socket, "GIMME");
//                }
//                if(auth.hasAccess())
//                    sendTestMessage();
        }
        else{
            System.out.println("Not Connected!");
        }
    }

    private Socket connectToServer(){
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress("mc.spinalcraft.com", 9495), 5000);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return socket;
    }

    private void sendTestMessage(ClientAmbassador ambassador){
//        Socket socket = connectToServer();
//        Crypt crypt = new Crypt();
        MessageSender sender = ambassador.getSender();
        sender.addHeader("intent", "message");
        sender.addItem("message", "YO WHAT UP");
        ambassador.sendMessage(sender);

        MessageReceiver receiver = ambassador.receiveMessage();
        System.out.println("Got back: " + receiver.getItem("message"));
    }
}
