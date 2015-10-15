package com.spinalcraft.manager.client.com.spinalcraft.manager.client.util;

import com.spinalcraft.easycrypt.EasyCrypt;
import com.spinalcraft.easycrypt.messenger.MessageSender;

import java.net.Socket;

/**
 * Created by Parker on 10/5/2015.
 */
public class Sender extends MessageSender {
    public Sender(Socket socket, EasyCrypt crypt) {
        super(socket, crypt);
    }
}
