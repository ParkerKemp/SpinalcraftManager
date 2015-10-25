package com.spinalcraft.manager.client;

import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;

import com.spinalcraft.manager.client.activity.ApplicationListActivity;

/**
 * Created by Parker on 10/18/2015.
 */
public class TabSelectListener implements TabLayout.OnTabSelectedListener{

    private ApplicationListActivity activity;

    public TabSelectListener(ApplicationListActivity activity){
        this.activity = activity;
    }

    @Override
    public void onTabSelected(Tab tab) {
        String filter = tab.getText().toString().toLowerCase();
        activity.setFilter(filter);
        activity.updateListView();
        activity.selectedTab = tab.getPosition();
    }

    @Override
    public void onTabUnselected(Tab tab) {

    }

    @Override
    public void onTabReselected(Tab tab) {

    }
}
