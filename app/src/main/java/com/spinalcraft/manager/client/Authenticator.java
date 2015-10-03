package com.spinalcraft.manager.client;

import android.text.style.AlignmentSpan;
import android.util.Base64;

import com.spinalcraft.easycrypt.EasyCrypt;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

/**
 * Created by Parker on 10/2/2015.
 */
public class Authenticator {
    public Authenticator(){

        Crypt crypt = new Crypt();
        try {
            KeyPair keyPair = crypt.generateKeys();

            PrivateKey privateKey = keyPair.getPrivate();
            String privateString = crypt.stringFromPrivateKey(privateKey);

            System.out.println("Private key from EasyCrypt: " + privateString);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }
}
