package com.spinalcraft.manager.client.com.spinalcraft.manager.client.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.spinalcraft.berberos.common.BerberosError.ErrorCode;
import com.spinalcraft.easycrypt.messenger.Messenger;
import com.spinalcraft.manager.client.com.spinalcraft.manager.client.util.Command;
import com.spinalcraft.manager.client.com.spinalcraft.manager.client.util.FileIO;
import com.spinalcraft.manager.client.com.spinalcraft.manager.client.com.spinalcraft.manager.client.task.LoginTask;
import com.spinalcraft.manager.client.R;

public class MainActivity extends AppCompatActivity {
    public static final int LoginRequest = 0;
//    private TextView output;

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
                    ErrorCode code = (ErrorCode)data[1];
                    if(code != ErrorCode.NONE) {
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
        startActivityForResult(intent, LoginRequest);
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
