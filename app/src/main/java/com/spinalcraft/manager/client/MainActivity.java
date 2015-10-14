package com.spinalcraft.manager.client;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.spinalcraft.easycrypt.messenger.Messenger;

public class MainActivity extends AppCompatActivity {
    TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FileIO.setActivity(this);
        Messenger.shouldShowDebug = true;

        Uri data = getIntent().getData();
        if(data != null){
            String path = data.getQueryParameter("key");
            System.out.println(path);
        }
        String url = getIntent().getStringExtra(Intent.EXTRA_TEXT);

        if(url != null)
            System.out.println(url);


        cachedLogin();
//        loginPrompt();

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

    private void cachedLogin(){
        String username = FileIO.read(".username");
        String password = FileIO.read(".password");
        if(username == null || password == null)
            loginPrompt();
        else
            new LoginTask(username, password, this, new Command(){
                @Override
                public void execute(Object... data) {
//                    Activity activity = (Activity)data[0];
                    boolean success = (Boolean)data[1];
                    if(!success) {
                        loginPrompt();
                        System.out.println("Failure");
                    }
                    else
                        System.out.println("Success");
                }
            }).execute();
    }

    private void loginPrompt(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
