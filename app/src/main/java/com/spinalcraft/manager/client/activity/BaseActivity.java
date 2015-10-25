package com.spinalcraft.manager.client.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.spinalcraft.easycrypt.messenger.Messenger;

/**
 * Created by Parker on 10/18/2015.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        Messenger.shouldShowDebug = true;
        setTitle("");
    }
    public void shortToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
