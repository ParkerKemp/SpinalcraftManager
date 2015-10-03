package com.spinalcraft.manager.client;

import android.util.Base64;

import com.spinalcraft.easycrypt.EasyCrypt;

/**
 * Created by Parker on 10/2/2015.
 */
public class Crypt extends EasyCrypt {
    @Override
    protected byte[] decode(String s) {
        return Base64.decode(s, Base64.DEFAULT);
    }

    @Override
    protected String encode(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
