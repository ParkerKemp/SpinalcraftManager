package com.spinalcraft.manager.client.task;

import android.app.Activity;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spinalcraft.berberos.client.ClientAmbassador;
import com.spinalcraft.easycrypt.messenger.MessageReceiver;
import com.spinalcraft.easycrypt.messenger.MessageSender;
import com.spinalcraft.manager.client.Application;
import com.spinalcraft.manager.client.util.AndroidClient;
import com.spinalcraft.manager.client.util.Command;
import com.spinalcraft.manager.client.util.Crypt;
import com.spinalcraft.manager.client.util.FileIO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ApplicationListTask extends AsyncTask<Void, Void, ArrayList<Application>> {

    private Activity activity;
    private String filter;
    private Command command;

    public ApplicationListTask(Activity activity, String filter, Command command){
        this.activity = activity;
        this.filter = filter;
        this.command = command;
    }

    @Override
    protected ArrayList<Application> doInBackground(Void... params) {
        AndroidClient client = new AndroidClient(Crypt.getInstance(), activity);
        Socket socket = connectToServer();
        if(socket == null)
            return null;
        String username = FileIO.read(".username", activity.getApplication());
        String password = FileIO.read(".password", activity.getApplication());
        ClientAmbassador ambassador = client.getAmbassador(username, password, "manager");
        if(ambassador == null)
            return null;

        MessageSender sender = ambassador.getSender();
        sender.addHeader("intent", "applicationList");
        sender.addItem("filter", filter);
        ambassador.sendMessage(sender);
        MessageReceiver receiver = ambassador.receiveMessage();
        if(receiver == null)
            return null;
        if(receiver.getHeader("status").equals("good")){
            String json = receiver.getItem("applications");
            Type type = new TypeToken<ArrayList<Application>>(){}.getType();
            return new Gson().fromJson(json, type);
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Application> applications){
        command.execute(applications);
    }

    private Socket connectToServer(){
        Socket socket = new Socket();
        try {
            socket.setSoTimeout(5000);
            socket.connect(new InetSocketAddress("mc.spinalcraft.com", 9495), 5000);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if(socket.isConnected())
            return socket;
        else
            return null;
    }
}
