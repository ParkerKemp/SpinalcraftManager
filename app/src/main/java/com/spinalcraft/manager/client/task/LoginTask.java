package com.spinalcraft.manager.client.task;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;

import com.spinalcraft.manager.client.activity.BaseActivity;
import com.spinalcraft.manager.client.util.AndroidClient;
import com.spinalcraft.manager.client.util.Command;
import com.spinalcraft.manager.client.util.Crypt;
import com.spinalcraft.manager.client.util.FileIO;
import com.spinalcraft.berberos.common.BerberosError.ErrorCode;

/**
 * Created by Parker on 10/13/2015.
 */
public class LoginTask extends AsyncTask<Void, Void, ErrorCode> {
    private String username;
    private String password;
    private BaseActivity activity;
    private Command command;

    public LoginTask(String username, String password, BaseActivity activity, Command command){
        this.username = username;
        this.password = password;
        this.activity = activity;
        this.command = command;
    }

    @Override
    protected ErrorCode doInBackground(Void... params) {
        System.out.println("Authenticating");
        AndroidClient client = new AndroidClient(Crypt.getInstance(), activity);

        return client.testCredentials(username, password);
    }

    @Override
    protected void onPostExecute(ErrorCode code){
        handleCode(code);

        if(code == ErrorCode.NONE){
            FileIO.write(".username", username, activity.getApplication());
            FileIO.write(".password", password, activity.getApplication());
        }
        else if(code == ErrorCode.AUTHENTICATION){
            FileIO.delete(".password", activity.getApplication());
        }

        command.execute(activity, code);
    }

    private void invalidResponse(){
        activity.shortToast("Received invalid response from authentication server.");
    }

    private void connectionFailed(){
        activity.shortToast("Could not connect to authentication server.");
    }

    private void accessDenied(){
        activity.shortToast("Invalid username or password.");
    }

    private void handleCode(ErrorCode code){
        switch(code){
            case AUTHENTICATION:
                accessDenied();
                break;
            case CONNECTION:
                connectionFailed();
                break;
            case SECURITY:
                invalidResponse();
                break;
            default:
                break;
        }
    }
}
