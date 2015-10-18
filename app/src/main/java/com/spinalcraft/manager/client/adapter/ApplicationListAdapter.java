package com.spinalcraft.manager.client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.spinalcraft.manager.client.Application;
import com.spinalcraft.manager.client.R;

import java.util.ArrayList;

/**
 * Created by Parker on 10/18/2015.
 */
public class ApplicationListAdapter extends ArrayAdapter<Application> {
    private ArrayList<Application> applications;

    public ApplicationListAdapter(Context context, int resource, ArrayList<Application> applications) {
        super(context, resource);
        this.applications = applications;
    }

    public void setList(ArrayList<Application> applications){
        this.applications = applications;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount(){
        return applications.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_application_list, parent, false);
        }

        Application application = applications.get(position);
        TextView username = (TextView)convertView.findViewById(R.id.username);
        username.setText(application.username);

        TextView date = (TextView)convertView.findViewById(R.id.date);
        date.setText(application.timestamp.toString());

        TextView country = (TextView)convertView.findViewById(R.id.country);
        country.setText(application.country);

        return convertView;
    }
}
