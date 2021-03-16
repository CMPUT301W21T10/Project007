package com.example.project007;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TrailList_OtherFrags extends ArrayAdapter<Trails> {
    private ArrayList<Trails> trails;
    private Context context;

    public TrailList_OtherFrags(Context context, ArrayList<Trails> trails){
        super(context,0, trails);
        this.trails = trails;
        this.context = context;
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.format_content_othertrails, parent,false);
        }
        Trails trail = trails.get(position);
        TextView trailTitle = view.findViewById(R.id.trail_text);
        TextView date = view.findViewById(R.id.date_text);
        TextView time = view.findViewById(R.id.time_text);
        TextView variesData = view.findViewById(R.id.result_text);

        trailTitle.setText(trail.getTrail_title());
        date.setText(trail.getDate());
        time.setText(trail.getTime());
        variesData.setText(trail.getVariesData());

        return view;
    }
}