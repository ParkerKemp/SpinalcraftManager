package com.spinalcraft.manager.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.spinalcraft.manager.client.Application;
import com.spinalcraft.manager.client.ApplicationListClickListener;
import com.spinalcraft.manager.client.R;
import com.spinalcraft.manager.client.TabSelectListener;
import com.spinalcraft.manager.client.adapter.ApplicationListAdapter;
import com.spinalcraft.manager.client.task.ApplicationListTask;
import com.spinalcraft.manager.client.util.Command;

import java.util.ArrayList;
import java.util.Collections;

public class ApplicationListActivity extends BaseActivity implements OnRefreshListener, OnItemClickListener {
    private SwipeRefreshLayout swipeLayout;
    private String filter;
    private ApplicationListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_list);

        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipeLayout);
        swipeLayout.setOnRefreshListener(this);

        TypedValue typed_value = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.actionBarSize, typed_value, true);
        swipeLayout.setProgressViewOffset(false, 0, getResources().getDimensionPixelSize(typed_value.resourceId));

        swipeLayout.setRefreshing(true);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("Pending"));
        tabLayout.addTab(tabLayout.newTab().setText("Accepted"));
        tabLayout.addTab(tabLayout.newTab().setText("Declined"));
        tabLayout.setOnTabSelectedListener(new TabSelectListener(this));
        tabLayout.getTabAt(1).select();

        adapter = new ApplicationListAdapter(this, R.layout.item_application_list, new ArrayList<Application>());
        ListView listView = (ListView)findViewById(R.id.applicationListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

//        getApplications();
    }

    @Override
    public void onRefresh() {
        updateListView();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ApplicationActivity.class);
        Application application = adapter.getItem(position);
        System.out.println("Sending username: " + application.username);
        intent.putExtra("application", adapter.getItem(position));
        startActivity(intent);
    }

    public void setFilter(String filter){
        this.filter = filter;
    }

    private void updateAdapter(ArrayList<Application> applications){
        ListView listView = (ListView)findViewById(R.id.applicationListView);
        ((ApplicationListAdapter)listView.getAdapter()).setList(applications);
    }

    public void updateListView(){
        swipeLayout.setRefreshing(true);
        new ApplicationListTask(this, filter, new Command() {

            @Override
            public void execute(Object... data) {
                swipeLayout.setRefreshing(false);
                ArrayList<Application> applications = (ArrayList<Application>)data[0];
                if(applications == null) {
                    shortToast("Failed to retrieve applications!");
                    return;
                }
                Collections.sort(applications, Collections.reverseOrder());
                updateAdapter(applications);
            }
        }).execute();
    }
}
