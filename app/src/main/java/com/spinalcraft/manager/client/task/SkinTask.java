package com.spinalcraft.manager.client.task;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spinalcraft.manager.client.util.Command;
import com.spinalcraft.manager.client.util.Crypt;
import com.spinalcraft.manager.client.util.FileIO;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Parker on 10/20/2015.
 */
public class SkinTask extends AsyncTask<Void, Void, Bitmap> {

    private Activity activity;
    private Command command;
    private String uuid;

    public SkinTask(Activity activity, Command command, String uuid){
        this.activity = activity;
        this.command = command;
        this.uuid = uuid;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        String url = getSkinUrl();
        if(url == null)
            return null;
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
            Bitmap face = Bitmap.createBitmap(bitmap, 8, 8, 8, 8);
            Bitmap faceMask = Bitmap.createBitmap(bitmap, 40, 8, 8, 8);
            return overlay(face, faceMask);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result){
        command.execute(result);
    }

    private String getSkinUrl(){
        String skinUrl = FileIO.read(".skin_" + uuid, activity.getApplication());
        if(skinUrl != null)
            return skinUrl;
        String url = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid;
        try {
            HttpURLConnection connection = createConnection(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String json = reader.readLine();
            System.out.println(json);
            JsonParser parser = new JsonParser();
            JsonObject obj = parser.parse(json).getAsJsonObject();
            JsonArray properties = obj.get("properties").getAsJsonArray();
            obj = properties.get(0).getAsJsonObject();
            String value = obj.get("value").getAsString();
            String decoded = new String(Crypt.getInstance().decode(value));
            obj = parser.parse(decoded).getAsJsonObject();
            obj = obj.get("textures").getAsJsonObject();
            obj = obj.get("SKIN").getAsJsonObject();
            skinUrl = obj.get("url").getAsString();
            FileIO.write(".skin_" + uuid, skinUrl, activity.getApplication());
            return skinUrl;
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HttpURLConnection createConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        return connection;
    }

    private Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, new Matrix(), null);
        return bmOverlay;
    }
}
