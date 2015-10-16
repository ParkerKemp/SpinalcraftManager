package com.spinalcraft.manager.client.util;

import com.spinalcraft.berberos.client.BerberosClient;
import com.spinalcraft.easycrypt.EasyCrypt;
import com.spinalcraft.easycrypt.messenger.MessageReceiver;
import com.spinalcraft.easycrypt.messenger.MessageSender;

import java.net.Socket;

/**
 * Created by Parker on 10/11/2015.
 */
public class AndroidClient extends BerberosClient {
    public AndroidClient(EasyCrypt crypt) {
        super(crypt);
//        cacheTickets = false;
    }

    @Override
    protected void cacheSessionKey(String s, String s1) {
        FileIO.write(".session_" + s, s1);
    }

    @Override
    protected String retrieveSessionKey(String s) {
        String filename = ".session_" + s;
        if(!FileIO.exists(filename))
            return null;

        return FileIO.read(filename);
    }

    @Override
    protected void cacheTicket(String s, String s1) {
        FileIO.write(".ticket_" + s, s1);
    }

    @Override
    protected String retrieveTicket(String s) {
        String filename = ".ticket_" + s;
        if(!FileIO.exists(filename))
            return null;

        return FileIO.read(filename);
    }

    @Override
    public MessageSender getSender(Socket socket, EasyCrypt easyCrypt) {
        return new Sender(socket, easyCrypt);
    }

    @Override
    public MessageReceiver getReceiver(Socket socket, EasyCrypt easyCrypt) {
        return new Receiver(socket, easyCrypt);
    }
}
