package com.spinalcraft.manager.client.activity;

import android.os.Bundle;

import com.spinalcraft.manager.client.R;
import com.spinalcraft.manager.client.util.TextFitTextView;

/**
 * Created by Parker on 10/18/2015.
 */
public class ApplicationActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
//
//        TextFitTextView uuid = (TextFitTextView)findViewById(R.id.uuid);
//        uuid.setFitTextToBox(true);
    }
}
