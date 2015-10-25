package com.spinalcraft.manager.client.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spinalcraft.manager.client.Application;
import com.spinalcraft.manager.client.ApplicationAnswerClickListener;
import com.spinalcraft.manager.client.R;
import com.spinalcraft.manager.client.task.ApplicationAnswerTask;
import com.spinalcraft.manager.client.task.SkinTask;
import com.spinalcraft.manager.client.util.Command;
import com.spinalcraft.manager.client.util.TextFitTextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Parker on 10/18/2015.
 */
public class ApplicationActivity extends AuthenticatedActivity {

    private ImageView skin;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Application application = (Application)intent.getSerializableExtra("application");

        loadView(application);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadView(Application application){
        TextView username = (TextView)findViewById(R.id.username);
        username.setText(application.username);

        TextView uuid = (TextView)findViewById(R.id.uuid);
        uuid.setText(application.uuid);

        TextView country = (TextView)findViewById(R.id.country);
        country.setText("Location: " + application.country);

        TextView age = (TextView)findViewById(R.id.age);

        age.setText("Approx. age: " + (Calendar.getInstance().get(Calendar.YEAR) - application.year));

        String appStatus = "";
        switch(application.status){
            case 0:
                appStatus = "pending";
                break;
            case 1:
            case 2:
                appStatus = "completed";
                break;
        }

        TextView status = (TextView)findViewById(R.id.status);
        status.setText("Application status: " + appStatus);

        TextView submissionDate = (TextView)findViewById(R.id.submissionDate);
        submissionDate.setText("Submitted on " + new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a").format(application.timestamp * 1000L));

        TextView statusDetails = (TextView)findViewById(R.id.statusDetails);

        initButtons(application);

        if(application.status == 0) {
            statusDetails.setVisibility(View.GONE);
        }
        else if(application.status == 1) {
            statusDetails.setText("Accepted by " + application.staffActor + " on " + new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a").format(application.actionTimestamp * 1000L));
            statusDetails.setVisibility(View.VISIBLE);
        }
        else{
            statusDetails.setText("Declined by " + application.staffActor + " on " + new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a").format(application.actionTimestamp * 1000L));
            statusDetails.setVisibility(View.VISIBLE);
        }

        TextView comment = (TextView)findViewById(R.id.comment);
        comment.setText(application.comment);

        TextView email = (TextView)findViewById(R.id.email);
        email.setText(application.email);

        skin = (ImageView)findViewById(R.id.avatar);
        getSkin(application.uuid);
    }

    private void initButtons(Application application){
        LinearLayout buttonBar = (LinearLayout)findViewById(R.id.buttonBar);

        if(application.status != 0){
            buttonBar.setVisibility(View.GONE);
            return;
        }

        buttonBar.setVisibility(View.VISIBLE);

        Button accept = (Button)findViewById(R.id.accept);
        Button decline = (Button)findViewById(R.id.decline);

        accept.setOnClickListener(new ApplicationAnswerClickListener(this, application.uuid, true));

        decline.setOnClickListener(new ApplicationAnswerClickListener(this, application.uuid, false));
    }

    private void getSkin(String uuid){
        new SkinTask(this, new Command(){

            @Override
            public void execute(Object... data) {
                Bitmap bitmap = (Bitmap)data[0];
                if(bitmap == null)
                    return;
                BitmapDrawable drawable = new BitmapDrawable(bitmap);
                drawable.setAntiAlias(false);
                drawable.setFilterBitmap(false);
                skin.setImageDrawable(drawable);
            }
        }, uuid).execute();
    }
}
