package com.spinalcraft.manager.client;

import com.spinalcraft.berberos.client.ClientAmbassador;
import com.spinalcraft.easycrypt.messenger.MessageReceiver;
import com.spinalcraft.easycrypt.messenger.MessageSender;
import com.spinalcraft.manager.client.activity.MainActivity;
import com.spinalcraft.manager.client.util.AndroidClient;
import com.spinalcraft.manager.client.util.Crypt;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Connector implements Runnable {

    private MainActivity activity;

    public Connector(MainActivity activity){
        this.activity = activity;
    }

    @Override
    public void run(){
        Socket socket = connectToServer();
        if(socket != null && socket.isConnected()){
            AndroidClient client = new AndroidClient(Crypt.getInstance(), activity);
            ClientAmbassador ambassador = client.getAmbassador(socket, "derp4", "asdf1234", "manager");
            if(ambassador == null) {
                System.err.println("Authentication failed!");
                return;
            }
            sendTestMessage(ambassador);
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
        MessageSender sender = ambassador.getSender();
        sender.addHeader("intent", "message");
        sender.addItem("message", "YO WHAT UP");
        ambassador.sendMessage(sender);

        MessageReceiver receiver = ambassador.receiveMessage();
        System.out.println("Got back: " + receiver.getItem("message"));
    }
}
