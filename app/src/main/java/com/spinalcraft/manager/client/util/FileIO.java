package com.spinalcraft.manager.client.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Parker on 10/11/2015.
 */
public class FileIO {
    public static void write(String filename, String str, Application application){
        try {
            byte[] bytes = str.getBytes("UTF-8");
            FileOutputStream outputStream = application.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(bytes, 0, bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void delete(String filename, Application application){
        application.deleteFile(filename);
    }

    public static boolean exists(String filename, Application application){
        try {
            application.openFileInput(filename);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    public static String read(String filename, Application application){
        byte[] buffer = new byte[1024];
        try {
            FileInputStream inputStream = application.openFileInput(filename);

            inputStream.read(buffer, 0, 1024);
            return nullTerminate(new String(buffer));
        } catch (IOException e) {
            System.err.println("File does not exist: " + filename);
        }
        return null;
    }

    private static String nullTerminate(String str){
        return str.substring(0, str.indexOf('\0'));
    }
}
