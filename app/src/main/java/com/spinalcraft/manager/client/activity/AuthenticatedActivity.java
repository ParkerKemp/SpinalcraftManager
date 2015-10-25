package com.spinalcraft.manager.client.activity;

import android.app.Activity;
import android.content.Intent;

import com.spinalcraft.berberos.common.BerberosError;
import com.spinalcraft.berberos.common.BerberosError.ErrorCode;
import com.spinalcraft.manager.client.task.LoginTask;
import com.spinalcraft.manager.client.util.Command;
import com.spinalcraft.manager.client.util.FileIO;

public class AuthenticatedActivity extends BaseActivity {
    public static final int LoginRequest = 0;

    @Override
    public void onResume(){
        super.onResume();

        if(shouldAuthenticate()){
            String username = FileIO.read(".username", getApplication());
            String password = FileIO.read(".password", getApplication());
            if(username == null || password == null)
                invokeLoginActivity(username, password);
            else
                attemptAutoLogin(username, password);
        }
    }

    protected void attemptAutoLogin(final String username, final String password){
        new LoginTask(username, password, this, new Command() {
            @Override
            public void execute(Object... data) {
                Activity activity = (Activity)data[0];
                BerberosError.ErrorCode code = (ErrorCode)data[1];
                if(code == ErrorCode.NONE)
                    onAuthenticate();
                else
                    invokeLoginActivity(username, null);
            }
        }).execute();
    }

    protected void invokeLoginActivity(String username, String password){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        startActivityForResult(intent, LoginRequest);
    }

    protected void onAuthenticate(){
        //Override in child
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == LoginRequest){
            if(resultCode == RESULT_OK){
                onAuthenticate();
            }
        }
    }

    public void logout(){
        FileIO.delete(".password", getApplication());
        FileIO.delete(".lastAuth", getApplication());
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private boolean shouldAuthenticate(){
        String lastAuthString = FileIO.read(".lastAuth", getApplication());

        if(lastAuthString == null)
            return true;
        long lastAuth = Long.parseLong(lastAuthString);
        long currentTime = System.currentTimeMillis() / 1000;
        return currentTime - lastAuth > 300;
    }
}
