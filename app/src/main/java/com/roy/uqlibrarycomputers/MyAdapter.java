package com.roy.uqlibrarycomputers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Movie;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/** The custom adapter for the newlayout listview
 * Created by Roy Portas on 30/03/15.
 */
public class MyAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private List<LibraryComputer> computers;

    public MyAdapter(Activity activity, List<LibraryComputer> computers){
        this.activity = activity;
        this.computers = computers;
    }

    @Override
    public Object getItem(int location){
        Log.w("TEST", "Clicked " + computers.get(location));
        return computers.get(location);
    }

    @Override
    public long getItemId(int id){
        return id;
    }

    @Override
    public int getCount(){
        return computers.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parents){

        if (inflater == null){
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null){
            convertView = inflater.inflate(R.layout.newlayout, null);
        }

        LibraryComputer c = computers.get(position);

        TextView building = (TextView) convertView.findViewById(R.id.firstLine);
        TextView available = (TextView) convertView.findViewById(R.id.secondLine);

        building.setText(c.getBuilding());
        available.setText(c.getAvailable());

        return convertView;
    }
}
