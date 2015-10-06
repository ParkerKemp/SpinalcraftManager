package com.spinalcraft.manager.client;

import com.spinalcraft.easycrypt.EasyCrypt;
import com.spinalcraft.easycrypt.messenger.MessageReceiver;

import java.net.Socket;

import javax.crypto.SecretKey;

/**
 * Created by Parker on 10/5/2015.
 */
public class Receiver extends MessageReceiver {
    private Authenticator auth;

    public Receiver(Socket socket, EasyCrypt crypt, Authenticator auth) {
        super(socket, crypt);
        this.auth = auth;
    }

    @Override
    protected SecretKey getSecretKeyForIdentifier(String s) {
        return auth.getSecretKey();
    }

    @Override
    protected long getLastTransmitTimeForIdentifier(String s) {
        //Since this is a client, it will not be receiving handshake requests
        return -1;
    }
}
