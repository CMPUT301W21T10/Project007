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

public class CustomList extends ArrayAdapter<Experiment> {

    private final ArrayList<Experiment> experiments;
    private final Context context;

    public CustomList(Context context, ArrayList<Experiment> experiments) {
        super(context,0,experiments);
        this.experiments = experiments;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        // face our layout
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.overview_content,parent,false);
        }
        // get current element
        Experiment experiment = experiments.get(position);

        TextView experimentName = view.findViewById(R.id.name_view);
        TextView experimentDescription = view.findViewById(R.id.description_view);
        TextView experimentDate = view.findViewById(R.id.date_view);

        //error Prone!!! this file may contain previous version experiment xml path, check it before use it!!!!

        TextView experimentTrails = view.findViewById(R.id.trails_view);
        TextView experimentRate = view.findViewById(R.id.rate_view);

        //error Prone!!! this file may contain previous version experiment xml path, check it before use it!!!!

        experimentName.setText(experiment.getName());
        experimentDescription.setText(experiment.getDescription());
        experimentDate.setText(experiment.getDate());

        return view;
    }
}
