package com.spinalcraft.manager.client;


import android.view.View;
import android.view.View.OnClickListener;

import com.spinalcraft.manager.client.activity.ApplicationActivity;
import com.spinalcraft.manager.client.task.ApplicationAnswerTask;
import com.spinalcraft.manager.client.util.Command;

/**
 * Created by Parker on 10/20/2015.
 */
public class ApplicationAnswerClickListener implements OnClickListener {

    private ApplicationActivity activity;
    private String uuid;
    private boolean accept;

    public ApplicationAnswerClickListener(ApplicationActivity activity, String uuid, boolean accept){
        this.activity = activity;
        this.uuid = uuid;
        this.accept = accept;
    }

    @Override
    public void onClick(View v) {
        new ApplicationAnswerTask(activity, new Command() {
            @Override
            public void execute(Object... data) {
                Application application = (Application)data[0];
                if(application == null) {
                    activity.shortToast("Server error.");
                    return;
                }
                activity.loadView(application);
            }
        }, uuid, accept).execute();
    }
}
