package com.spinalcraft.manager.client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.spinalcraft.manager.client.Application;
import com.spinalcraft.manager.client.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
    public Application getItem(int position) {
        return applications.get(position);
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
        date.setText(new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a").format(application.timestamp * 1000L));

        TextView country = (TextView)convertView.findViewById(R.id.country);
        country.setText(application.country);

        TextView comment = (TextView)convertView.findViewById(R.id.comment);
        comment.setText(application.comment);

        TextView status = (TextView)convertView.findViewById(R.id.status);
        switch(application.status){
            case 0:
                status.setTextColor(convertView.getResources().getColor(R.color.darkYellow));
                status.setText("Pending");
                break;
            case 1:
                status.setTextColor(convertView.getResources().getColor(R.color.darkGreen));
                status.setText("Accepted");
                break;
            case 2:
                status.setTextColor(convertView.getResources().getColor(R.color.darkRed));
                status.setText("Declined");
                break;
        }

        return convertView;
    }
}
