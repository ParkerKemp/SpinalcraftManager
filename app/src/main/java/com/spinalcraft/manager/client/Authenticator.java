package com.spinalcraft.manager.client;

import android.app.Activity;
import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by Parker on 10/2/2015.
 */
public class Authenticator {
    private Crypt crypt;
    private MainActivity activity;

    public Authenticator(MainActivity activity){
        crypt = new Crypt();
        verifyKeysExist();
    }

    public String getPublicKey(){
        return getKey(".publickey");
    }

    public String getPrivateKey(){
        return getKey(".privatekey");
    }

    private String getKey(String filename){
        byte[] buffer = new byte[100];
        try {
            FileInputStream inputStream = activity.openFileInput(filename);
            inputStream.read(buffer, 0, 100);
            return new String(buffer);
        } catch (FileNotFoundException e) {
            generateKeys();
            return getKey(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void verifyKeysExist(){
        try {
            activity.openFileInput(".publickey");
            activity.openFileInput(".privatekey");
        } catch (FileNotFoundException e) {
            generateKeys();
            System.out.println("Generating keys for the first time");
        }
    }

    private void generateKeys(){
        try {
            KeyPair keyPair = crypt.generateKeys();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            byte[] bytes = crypt.stringFromPrivateKey(privateKey).getBytes();
            FileOutputStream outputStream = activity.openFileOutput(".privatekey", Context.MODE_PRIVATE);
            outputStream.write(bytes, 0, bytes.length);

            bytes = crypt.stringFromPublicKey(publicKey).getBytes();
            outputStream = activity.openFileOutput(".publickey", Context.MODE_PRIVATE);
            outputStream.write(bytes, 0, bytes.length);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
