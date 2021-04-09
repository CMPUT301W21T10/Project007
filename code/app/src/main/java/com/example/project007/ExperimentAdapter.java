package com.example.project007;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * This is ExperimentAdapter
 * Adapter for experiment List
 */
public class ExperimentAdapter extends ArrayAdapter<Experiment> {

    private final ArrayList<Experiment> experiments;
    private final Context context;
    private final Animation animation;

    private View globalView;

    public ExperimentAdapter(Context context, ArrayList<Experiment> experiments) {
        super(context,0,experiments);
        this.experiments = experiments;
        this.context = context;
        animation= AnimationUtils.loadAnimation(context, R.anim.push_out);

    }

    @Override
    public int getCount() {
        return experiments.size();
    }

    @Override
    public Experiment getItem(int arg0) {
        return experiments.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return experiments.get(arg0).getId();
    }

    @SuppressLint("SetTextI18n")
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        // face our layout
        try {
            Activity activity = (Activity)context;
            if(activity  instanceof SearchResult){
                view = LayoutInflater.from(context).inflate(R.layout.result_content,parent,false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.overview_content,parent,false);
        }

        // get current element
        Experiment experiment = experiments.get(position);

        TextView experimentName = view.findViewById(R.id.name_view);
        TextView experimentDescription = view.findViewById(R.id.description_view);

        ImageView image = view.findViewById(R.id.experiment_image);

        experimentName.setText(experiment.getName());
        experimentDescription.setText(experiment.getDescription());
        switch (experiment.getType()){
            case "Count-based": image.setImageResource(R.drawable.c); break;
            case "Binomial": image.setImageResource(R.drawable.b); break;
            case "Non-negative": image.setImageResource(R.drawable.n); break;
            case "Measurement": image.setImageResource(R.drawable.m); break;
        }

        TextView experimentUser = view.findViewById(R.id.owner_view);
        TextView experimentCondition = view.findViewById(R.id.condition_view);

        if(experimentCondition != null && experiment.isCondition()){
            experimentCondition.setText("Processing");
        }
        else if (experimentCondition != null){
            experimentCondition.setText("End");
        }

        if (experimentUser != null){
            String userId = experiment.getUserId();
            experimentUser.setText("anonymity");
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("data").child(userId).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // How to return this value?
                    if(dataSnapshot != null) {
                        System.out.println(dataSnapshot.getValue(String.class));
                        experimentUser.setText(dataSnapshot.getValue(String.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });

        }

        /*
        view.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getX();
                        if (globalDelButton != null)
                        break;
                    case MotionEvent.ACTION_UP:
                        upX = event.getX();
                        break;
                }

                if (delButton != null) {
                    if (Math.abs(- downX + upX) > 300) {
                        delButton.setVisibility(View.VISIBLE);
                        globalDelButton = delButton;
                        globalView = v;
                        return true;
                    }
                    return false;
                }
                return false;
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (globalDelButton != null) {
                    globalDelButton.setVisibility(View.GONE);
                    deleteItem(globalView, position);
                }

            }
        });

*/
        return view;

    }
/*
    public void deleteItem(View view,final int position)
    {
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Experiment instance = experiments.get(position);
                DatabaseController.deleteExperiment(instance);
                experiments.remove(position);
                notifyDataSetChanged();
            }
        });

    }*/

}
