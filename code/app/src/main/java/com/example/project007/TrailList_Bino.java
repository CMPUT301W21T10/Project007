package com.example.project007;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * This class is customList of Binomial experiment.
 * <p>
 * Show convert view<br>
 * </p>
 */
public class TrailList_Bino extends ArrayAdapter<Trails> {
    private final ArrayList<Trails> trails;
    private final Context context;

    public TrailList_Bino(Context context, ArrayList<Trails> trails){
        super(context,0, trails);
        this.trails = trails;
        this.context = context;
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.format_content, parent,false);
        }
        Trails trail = trails.get(position);
        Location displayLoc = trail.getLocation();


        TextView trailTitle = view.findViewById(R.id.trail_text);
        TextView date = view.findViewById(R.id.date_text);
        TextView time = view.findViewById(R.id.time_text);
        TextView success = view.findViewById(R.id.success_text);
        TextView fail = view.findViewById(R.id.fail_text);
        TextView location = view.findViewById(R.id.Location_text);
        ImageView ignore = view.findViewById(R.id.ignoreImg);

        if (displayLoc!=null){
            String LocString = displayLoc.getLatitude() + ":" + displayLoc.getLongitude();
            location.setText(LocString);
        }
        trailTitle.setText(trail.getTrail_title());
        date.setText(trail.getDate());
        time.setText(trail.getTime());
        success.setText(trail.getSuccess());
        fail.setText(trail.getFailure());

        //if ignore shows the mark on that item
        boolean ignoreConditon = trail.isIgnoreCondition();
        if (ignoreConditon){
            ignore.setImageResource(R.drawable.crossimages);
        }else{
            ignore.setImageResource(R.drawable.checkimages);
        }


        return view;
    }
}
