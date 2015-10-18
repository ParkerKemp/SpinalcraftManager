package com.spinalcraft.manager.client;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.spinalcraft.manager.client.activity.MainActivity;
import com.spinalcraft.manager.client.activity.NavActivity;

public class DrawerClickListener implements OnItemClickListener {

    private NavActivity activity;
    private DrawerLayout drawerLayout;

    public DrawerClickListener(NavActivity activity, DrawerLayout drawerLayout){
        this.activity = activity;
        this.drawerLayout = drawerLayout;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        drawerLayout.closeDrawer(Gravity.LEFT);
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
