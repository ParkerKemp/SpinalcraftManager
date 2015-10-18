package com.spinalcraft.manager.client.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.spinalcraft.manager.client.DrawerClickListener;
import com.spinalcraft.manager.client.R;
import com.spinalcraft.manager.client.util.FileIO;

public class MainActivity extends AppCompatActivity {
    public static final int LoginRequest = 0;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uri data = getIntent().getData();
        if(data != null){
            String path = data.getQueryParameter("key");
            System.out.println(path);
        }
        String url = getIntent().getStringExtra(Intent.EXTRA_TEXT);

        if(url != null)
            System.out.println(url);

        String[] array = {"Applications", "Log out"};
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawerLayout);
        ListView drawerList = (ListView)findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter(this, R.layout.item_drawer_list, array));
        drawerList.setOnItemClickListener(new DrawerClickListener(this));

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawerOpen, R.string.drawerClose){
            public void onDrawerClosed(View view)
            {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                syncState();
            }

            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
            }
        };
        drawer.setDrawerListener(toggle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toggle.syncState();
//        (new Thread(new Connector(this))).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == LoginRequest){
            if(resultCode == RESULT_OK){
                System.out.println("Success?");
            }
        }
    }

    public void openAppList(){
        Intent intent = new Intent(this, ApplicationListActivity.class);
        startActivity(intent);
    }

    public void logout(){
        FileIO.delete(".password", this);
        finish();
    }
}
