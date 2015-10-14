package com.spinalcraft.manager.client;

import android.os.AsyncTask;

/**
 * Created by Parker on 10/13/2015.
 */
public class LoginTask extends AsyncTask<String, Integer, Boolean> {
    @Override
    protected Boolean doInBackground(String... params) {
        String username = params[0];
        String password = params[1];
        AndroidClient client = new AndroidClient(Crypt.getInstance());

        if(client.testCredentials(username, password))
            return true;
        else
            return false;
    }

    @Override
    protected void onPostExecute(Boolean result){
        if(result)
            System.out.println("Authentication succeeded!");
        else
            System.out.println("Authentication failed!");
    }
}
