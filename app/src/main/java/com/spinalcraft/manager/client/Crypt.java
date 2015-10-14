package com.spinalcraft.manager.client;

import android.util.Base64;

import com.spinalcraft.easycrypt.EasyCrypt;

/**
 * Created by Parker on 10/2/2015.
 */
public class Crypt extends EasyCrypt {

    private static Crypt crypt;

    private Crypt(){
        //singleton
    }

    public static Crypt getInstance(){
        if(crypt == null)
            crypt = new Crypt();
        return crypt;
    }

    @Override
    public byte[] decode(String s) {
        return Base64.decode(s.getBytes(), Base64.NO_WRAP);
    }

    @Override
    public String encode(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }
}
