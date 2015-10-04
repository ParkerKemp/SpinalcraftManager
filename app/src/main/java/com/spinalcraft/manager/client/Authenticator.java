package com.spinalcraft.manager.client;

import android.content.Context;

import com.google.gson.JsonObject;
import com.spinalcraft.easycrypt.Messenger;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
    private String privateKey;
    private String publicKey;

    public Authenticator(MainActivity activity){
        this.activity = activity;
        crypt = new Crypt();
        verifyKeysExist();
        generateKeys();

        String test = "Hello Worrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrld!";

        try {
            SecretKey key = crypt.generateSecretKey();
            byte[] cipher = crypt.encryptMessage(key, test);
            System.out.println("Original key: " + crypt.encode(key.getEncoded()));
            byte[] keyCipher = crypt.encryptKey(crypt.loadPublicKey(getPublicKey()), key);
            System.out.println("Key Cipher: " + crypt.encode(keyCipher));

            SecretKey newKey = crypt.decryptKey(crypt.loadPrivateKey(getPrivateKey()), keyCipher);

            System.out.println("New key: " + crypt.encode(newKey.getEncoded()));

//            System.out.println("Access request JSON: " + getAccessRequest("1234").toString());

//            String keystring = crypt.stringFromSecretKey(key);
//            System.out.println("Secret key: " + keystring);
            System.out.println("Cipher: " + crypt.encode(cipher));
            String result = crypt.decryptMessage(newKey, cipher);
            System.out.println("Result: " + result);

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    public JsonObject getAccessRequest2(String accessKey){
        JsonObject obj = new JsonObject();
        obj.addProperty("intent", "access");
        obj.addProperty("accessKey", accessKey);
        obj.addProperty("publicKey", getPublicKey());
        return obj;
    }

    public String getAccessRequest(String accessKey){

        String request = "3" + "\n";
        request += "intent:access\n";
        request += "accessKey:" + accessKey + "\n";
        request += "publicKey:" + StringEscapeUtils.escapeJava(getPublicKey()) + "\n";
        return request;
    }

    public void sendAccessRequest(PrintStream printer, String accessKey){
        Messenger messenger = new Messenger();
        messenger.add("intent", "access");
        messenger.add("accessKey", accessKey);
        messenger.add("publicKey", getPublicKey());
        messenger.sendMessage(printer);
    }

    public String getPublicKey(){
        if(publicKey == null){
            publicKey = getKey(".publickey");
        }
        return publicKey;
    }

    public String getPrivateKey(){
        if(privateKey == null){
            privateKey = getKey(".privatekey");
        }
        return privateKey;
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
            System.out.println("Generating keys for the first time");
        }
    }

    private void generateKeys(){
        try {
            KeyPair keyPair = crypt.generateKeys();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            byte[] bytes = crypt.stringFromPrivateKey(privateKey).getBytes("UTF-8");
            FileOutputStream outputStream = activity.openFileOutput(".privatekey", Context.MODE_PRIVATE);
            outputStream.write(bytes, 0, bytes.length);

            bytes = crypt.stringFromPublicKey(publicKey).getBytes("UTF-8");
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
