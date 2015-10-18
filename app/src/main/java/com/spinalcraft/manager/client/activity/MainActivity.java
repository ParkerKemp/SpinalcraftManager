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

public class MainActivity extends NavActivity {
    public static final int LoginRequest = 0;

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
}
