package com.spinalcraft.manager.client;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.spinalcraft.manager.client.activity.ApplicationActivity;
import com.spinalcraft.manager.client.activity.ApplicationListActivity;

/**
 * Created by Parker on 10/18/2015.
 */
public class ApplicationListClickListener implements OnItemClickListener {

    private ApplicationListActivity activity;

    public ApplicationListClickListener(ApplicationListActivity activity){
        this.activity = activity;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(activity, ApplicationActivity.class);
        intent.putExtra("application", new Application());
        activity.startActivity(intent);
    }
}
