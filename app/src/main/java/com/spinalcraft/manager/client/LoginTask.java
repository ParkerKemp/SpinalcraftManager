package com.spinalcraft.manager.client;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

/**
 * Created by Parker on 10/13/2015.
 */
public class LoginTask extends AsyncTask<Void, Void, Boolean> {
    private String username;
    private String password;
    private Activity activity;
    private Command command;

    public LoginTask(String username, String password, Activity activity, Command command){
        this.username = username;
        this.password = password;
        this.activity = activity;
        this.command = command;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        AndroidClient client = new AndroidClient(Crypt.getInstance());

        if(client.testCredentials(username, password)) {
            FileIO.write(".username", username);
            FileIO.write(".password", password);
            return true;
        }
        else
            return false;
    }

    @Override
    protected void onPostExecute(Boolean result){
        if(result){
            System.out.println("Authentication succeeded!");
        }
        else
            System.out.println("Authentication failed!");
        command.execute(activity, result);
    }
}
