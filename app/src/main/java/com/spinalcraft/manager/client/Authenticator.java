package com.spinalcraft.manager.client;

import android.content.Context;

import com.spinalcraft.easycrypt.messenger.MessageReceiver;
import com.spinalcraft.easycrypt.messenger.MessageSender;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

/**
 * Created by Parker on 10/2/2015.
 */
public class Authenticator {
    private Crypt crypt;
    private MainActivity activity;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private SecretKey secretKey;

    private boolean hasAccess = false;

    public Authenticator(MainActivity activity){
        this.activity = activity;
        crypt = new Crypt();
        verifyKeysExist();
    }

    public boolean acquireAccess(BufferedReader reader, PrintStream printer, String accessKey) throws GeneralSecurityException {
        MessageSender sender = new MessageSender(printer);
        sender.addHeader("intent", "access");
        sender.addItem("accessKey", accessKey);
        sender.addHeader("publicKey", crypt.stringFromPublicKey(getPublicKey()));
        sender.sendMessage();

        MessageReceiver receiver = new MessageReceiver(reader);
        receiver.receiveMessage();

        if(receiver.getHeader("status").equals("GOOD")){
            String secret = receiver.getItem("secret");
            secretKey = crypt.decryptKey(getPrivateKey(), crypt.decode(secret));
            saveKey(crypt.stringFromSecretKey(secretKey), ".secretkey");
            hasAccess = true;
            System.out.println("Acquired access! Secret key: " + crypt.stringFromSecretKey(secretKey));

            return true;
        }
        else{
            System.out.println("Access was denied!");
        }
        return false;
    }

    public boolean hasAccess(){
        return hasAccess;
    }

    public PublicKey getPublicKey(){
        if(publicKey == null){
            try {
                publicKey = crypt.loadPublicKey(getKey(".publickey"));
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
        }
        return publicKey;
    }

    public PrivateKey getPrivateKey(){
        if(privateKey == null){
            try {
                privateKey = crypt.loadPrivateKey(getKey(".privatekey"));
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
        }
        return privateKey;
    }

    public SecretKey getSecretKey(){
        if(secretKey == null && hasAccess){
            secretKey = crypt.loadSecretKey(getKey(".secretkey"));
        }
        return secretKey;
    }

    private String getKey(String filename){
        byte[] buffer = new byte[1024];
        try {
            FileInputStream inputStream = activity.openFileInput(filename);
            inputStream.read(buffer, 0, 1024);
            return nullTerminate(new String(buffer));
        } catch (FileNotFoundException e) {
            generateKeys();
            return getKey(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String nullTerminate(String str){
        return str.substring(0, str.indexOf('\0'));
    }

    private void verifyKeysExist(){
        try {
            activity.openFileInput(".publickey");
            activity.openFileInput(".privatekey");
        } catch (FileNotFoundException e) {
            generateKeys();
            System.out.println("Generating keys for the first time.");
        }
        try {
            activity.openFileInput(".secretkey");
            hasAccess = true;
        } catch (FileNotFoundException e) {
            hasAccess = false;
        }
    }

    private void generateKeys(){
        try {
            KeyPair keyPair = crypt.generateKeys();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            saveKey(crypt.stringFromPrivateKey(privateKey), ".privatekey");
            saveKey(crypt.stringFromPublicKey(publicKey), ".publickey");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    private void saveKey(String keyString, String filename){
        try {
            byte[] bytes = keyString.getBytes("UTF-8");
            FileOutputStream outputStream = activity.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(bytes, 0, bytes.length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
