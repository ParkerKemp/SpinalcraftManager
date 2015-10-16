package com.spinalcraft.manager.client;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.spinalcraft.manager.client.activity.MainActivity;

/**
 * Created by Parker on 10/15/2015.
 */
public class DrawerClickListener implements OnItemClickListener {

    private MainActivity activity;

    public DrawerClickListener(MainActivity activity){
        this.activity = activity;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(position){
            case 0:
                activity.logout();
                break;
        }
    }
}
