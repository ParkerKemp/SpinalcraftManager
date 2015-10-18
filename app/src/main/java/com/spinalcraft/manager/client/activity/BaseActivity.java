package com.spinalcraft.manager.client.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Parker on 10/18/2015.
 */
public class BaseActivity extends AppCompatActivity {
    public void shortToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
