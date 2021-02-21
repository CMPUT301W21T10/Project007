package com.example.jinglong_trialbook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static java.lang.Boolean.TRUE;

// this class use mostly for getView since we need DIY UI
public class CustomList extends ArrayAdapter<Experiment> {

    private ArrayList<Experiment> experiments;
    private Context context;

    public CustomList(Context context, ArrayList<Experiment> experiments) {
        super(context,0,experiments);
        this.experiments = experiments;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        // face our layout
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content,parent,false);
        }
        // get current element
        Experiment experiment = experiments.get(position);
        ArrayList<Boolean> current = experiment.getExperimentTrails();

        //set all textView value for mainActivity
        @SuppressLint("DefaultLocale") String trails = String.format("Total trails: %d",current.size());
        String result = experiment.computeRate();

        TextView experimentName = view.findViewById(R.id.name_view);
        TextView experimentDescription = view.findViewById(R.id.description_view);
        TextView experimentDate = view.findViewById(R.id.date_view);
        TextView experimentTrails = view.findViewById(R.id.trails_view);
        TextView experimentRate = view.findViewById(R.id.rate_view);


        experimentName.setText(experiment.getExperimentName());
        experimentDescription.setText(experiment.getExperimentDescription());
        experimentDate.setText(experiment.getExperimentDate());
        experimentTrails.setText(trails);
        experimentRate.setText(result);

        return view;
    }
}
