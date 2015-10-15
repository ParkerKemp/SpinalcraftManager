package com.spinalcraft.manager.client.com.spinalcraft.manager.client.util;

import com.spinalcraft.easycrypt.EasyCrypt;
import com.spinalcraft.easycrypt.messenger.MessageReceiver;

import java.net.Socket;

import javax.crypto.SecretKey;

/**
 * Created by Parker on 10/5/2015.
 */
public class Receiver extends MessageReceiver {

    public Receiver(Socket socket, EasyCrypt crypt) {
        super(socket, crypt);
    }
}
