package com.spinalcraft.manager.client;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

/**
 * Created by Parker on 10/2/2015.
 */
public class Authenticator {
    public Authenticator(){
        try {
            KeyPair keyPair = generateKeys();
            PrivateKey priv = keyPair.getPrivate();
            System.out.println("Private key: " + priv.toString());
            PublicKey pub = keyPair.getPublic();
            System.out.println("Public key: " + pub.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private KeyPair generateKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("DSA");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        generator.initialize(1024, random);
        return generator.generateKeyPair();
    }
}
