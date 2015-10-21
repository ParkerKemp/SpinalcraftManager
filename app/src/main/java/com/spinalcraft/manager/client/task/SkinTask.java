package com.spinalcraft.manager.client.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.spinalcraft.manager.client.util.Command;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Parker on 10/20/2015.
 */
public class SkinTask extends AsyncTask<String, Void, Bitmap> {

    private Command command;

    public SkinTask(Command command){
        this.command = command;
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        String username = params[0];
        String url = "http://skins.minecraft.net/MinecraftSkins/" + username + ".png";
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
            return Bitmap.createBitmap(bitmap, 8, 8, 8, 8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result){
        command.execute(result);
    }
}
