package com.spinalcraft.manager.client.util;

import android.app.Activity;

import com.spinalcraft.berberos.client.BerberosClient;
import com.spinalcraft.easycrypt.EasyCrypt;
import com.spinalcraft.easycrypt.messenger.MessageReceiver;
import com.spinalcraft.easycrypt.messenger.MessageSender;

import java.net.Socket;

public class AndroidClient extends BerberosClient {
    private Activity activity;

    public AndroidClient(EasyCrypt crypt, Activity activity) {
        super("auth.spinalcraft.com", 9494, crypt);
        this.activity = activity;
//        cacheTickets = false;
    }

    @Override
    protected String retrieveAccessPackage(String serviceIdentity){
        return FileIO.read(".accessPackage_" + serviceIdentity, activity.getApplication());
    }

    @Override
    protected void cacheAccessPackage(String serviceIdentity, String accessPackageString){
        FileIO.write(".accessPackage_" + serviceIdentity, accessPackageString, activity.getApplication());
    }

//    @Override
//    protected void cacheSessionKey(String s, String s1) {
//        FileIO.write(".session_" + s, s1, activity.getApplication());
//    }
//
//    @Override
//    protected String retrieveSessionKey(String s) {
//        String filename = ".session_" + s;
//        if(!FileIO.exists(filename, activity.getApplication()))
//            return null;
//
//        return FileIO.read(filename, activity.getApplication());
//    }
//
//    @Override
//    protected void cacheTicket(String s, String s1) {
//        FileIO.write(".ticket_" + s, s1, activity.getApplication());
//    }
//
//    @Override
//    protected String retrieveTicket(String s) {
//        String filename = ".ticket_" + s;
//        if(!FileIO.exists(filename, activity.getApplication()))
//            return null;
//
//        return FileIO.read(filename, activity.getApplication());
//    }

    @Override
    public MessageSender getSender(Socket socket, EasyCrypt easyCrypt) {
        return new Sender(socket, easyCrypt);
    }

    @Override
    public MessageReceiver getReceiver(Socket socket, EasyCrypt easyCrypt) {
        return new Receiver(socket, easyCrypt);
    }
}
