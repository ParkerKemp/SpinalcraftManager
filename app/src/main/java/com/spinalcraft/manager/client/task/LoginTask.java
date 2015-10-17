package com.spinalcraft.manager.client.task;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;

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
    private Activity activity;
    private Command command;

    public LoginTask(String username, String password, Activity activity, Command command){
        this.username = username;
        this.password = password;
        this.activity = activity;
        this.command = command;
    }

    @Override
    protected ErrorCode doInBackground(Void... params) {
        AndroidClient client = new AndroidClient(Crypt.getInstance(), activity);

        ErrorCode code = client.testCredentials(username, password);
        if(code == ErrorCode.NONE){
            FileIO.write(".username", username, activity);
            FileIO.write(".password", password, activity);
        }
        return code;
    }

    @Override
    protected void onPostExecute(ErrorCode code){
        if(code == ErrorCode.NONE){
            System.out.println("Authentication succeeded!");
        }
        else
            System.out.println("Authentication failed!");
        handleCode(code);
        command.execute(activity, code);
    }

    private void invalidResponse(){
        new AlertDialog.Builder(activity)
                .setTitle("Invalid response!")
                .setMessage("Received an invalid response from authentication server!")
                .setPositiveButton("OK", null)
                .show();
    }

    private void connectionFailed(){
        new AlertDialog.Builder(activity)
                .setTitle("Connection failed!")
                .setMessage("Could not connect to authentication server!")
                .setPositiveButton("OK", null)
                .show();
    }

    private void accessDenied(){
        new AlertDialog.Builder(activity)
                .setTitle("Authentication failed!")
                .setMessage("Username or password is invalid. Please try again.")
                .setPositiveButton("OK", null)
                .show();
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
