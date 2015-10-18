package com.spinalcraft.manager.client;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.spinalcraft.manager.client.activity.MainActivity;
import com.spinalcraft.manager.client.activity.NavActivity;

/**
 * Created by Parker on 10/15/2015.
 */
public class DrawerClickListener implements OnItemClickListener {

    private NavActivity activity;

    public DrawerClickListener(NavActivity activity){
        this.activity = activity;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(position){
            case 0:
                activity.openAppList();
                break;
            case 1:
                activity.logout();
                break;
        }
    }
}
