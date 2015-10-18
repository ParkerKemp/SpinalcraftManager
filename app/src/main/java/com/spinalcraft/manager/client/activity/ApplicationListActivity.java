package com.spinalcraft.manager.client.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.widget.ListView;

import com.spinalcraft.manager.client.Application;
import com.spinalcraft.manager.client.R;
import com.spinalcraft.manager.client.adapter.ApplicationListAdapter;
import com.spinalcraft.manager.client.task.ApplicationListTask;
import com.spinalcraft.manager.client.util.Command;

import java.util.ArrayList;

/**
 * Created by Parker on 10/17/2015.
 */
public class ApplicationListActivity extends NavActivity implements OnRefreshListener{
    private SwipeRefreshLayout swipeLayout;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_list);

        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipeLayout);
        swipeLayout.setOnRefreshListener(this);

        ListView listView = (ListView)findViewById(R.id.applicationListView);
        listView.setAdapter(new ApplicationListAdapter(this, R.layout.item_application_list, new ArrayList<Application>()));

        getApplications();
//        String[] array = {"Applications", "Log out"};
//        listView.setAdapter(new ArrayAdapter(this, R.layout.item_drawer_list, array));
    }

    @Override
    public void onRefresh() {
        getApplications();
    }

    private void updateAdapter(ArrayList<Application> applications){
        ListView listView = (ListView)findViewById(R.id.applicationListView);
        ((ApplicationListAdapter)listView.getAdapter()).setList(applications);
    }

    private void getApplications(){
        new ApplicationListTask(this, new Command() {
            @Override
            public void execute(Object... data) {
                swipeLayout.setRefreshing(false);
                ArrayList<Application> applications = (ArrayList)data[0];
                if(applications == null) {
                    shortToast("Failed to retrieve applications!");
                    return;
                }

                updateAdapter(applications);
            }
        }).execute();
    }
}
