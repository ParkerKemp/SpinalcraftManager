package com.spinalcraft.manager.client.task;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spinalcraft.berberos.client.ClientAmbassador;
import com.spinalcraft.easycrypt.messenger.MessageReceiver;
import com.spinalcraft.easycrypt.messenger.MessageSender;
import com.spinalcraft.manager.client.Application;
import com.spinalcraft.manager.client.activity.ApplicationActivity;
import com.spinalcraft.manager.client.util.AndroidClient;
import com.spinalcraft.manager.client.util.Command;
import com.spinalcraft.manager.client.util.Crypt;
import com.spinalcraft.manager.client.util.FileIO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ApplicationAnswerTask extends AsyncTask<Void, Void, Application> {
    private String uuid;
    private boolean accept;
    private ApplicationActivity activity;
    private Command command;

    public ApplicationAnswerTask(ApplicationActivity activity, Command command, String uuid, boolean accept){
        this.uuid = uuid;
        this.accept = accept;
        this.activity = activity;
        this.command = command;
    }

    @Override
    protected Application doInBackground(Void... params) {
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
        sender.addHeader("intent", "applicationAnswer");
        sender.addItem("uuid", uuid);
        sender.addItem("accept", String.valueOf(accept));
        ambassador.sendMessage(sender);
        MessageReceiver receiver = ambassador.receiveMessage();
        if(receiver == null)
            return null;

        if(!receiver.getHeader("status").equals("good"))
            return null;

        String json = receiver.getItem("application");
        Type type = new TypeToken<Application>(){}.getType();
        return new Gson().fromJson(json, type);
    }

    @Override
    protected void onPostExecute(Application application){
        command.execute(application);
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
