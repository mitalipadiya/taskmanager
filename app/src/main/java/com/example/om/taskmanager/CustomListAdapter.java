package com.example.om.taskmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by OM on 20/04/2017.
 */
public class CustomListAdapter extends BaseAdapter {
    Context context;
    String[] taskname;
    String[] taskdate;

    LayoutInflater inflater;
    public CustomListAdapter(Context context,String[] taskname,String[] taskdate){
        this.context=context;
        this.taskname=taskname;
        this.taskdate=taskdate;
    }
    @Override
    public int getCount() {
        return taskname.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txtTask,txtDate;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=inflater.inflate(R.layout.list_item,parent,false);
        txtTask = (TextView) itemView.findViewById(R.id.txt_task);
        txtDate= (TextView) itemView.findViewById(R.id.txt_date);
        txtTask.setText(taskname[position]);
        txtDate.setText(taskdate[position]);
        return itemView;
    }
}
