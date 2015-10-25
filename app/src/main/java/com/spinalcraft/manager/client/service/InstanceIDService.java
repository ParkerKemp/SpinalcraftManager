package com.spinalcraft.manager.client.service;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Parker on 10/23/2015.
 */
public class InstanceIDService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh(){
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
