package com.example.jinglong_trialbook;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.*;

// this class Show detail of an instance experiment from main
public class ShowDetail extends AppCompatActivity implements AddExperimentFragment.OnFragmentInteractionListener {
    private static Experiment experiment;
    private static Integer position;

    // unPackage info and set basic UI
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);

        Intent intent = getIntent();
        experiment = (Experiment) intent.getSerializableExtra("com.example.jinglong_trialbook.INSTANCE");
        position = intent.getIntExtra("com.example.jinglong_trialbook.POSITION", -1);

        // set value to textView
        setText();
        setTrails();

        // listener to show fragment
        final Button editButton = findViewById(R.id.edit_experiment_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddExperimentFragment(experiment).show(getSupportFragmentManager(), "ADD_EXPERIMENT");
            }
        });
    }

    // set name, description, date
    protected void setText(){
        TextView name = findViewById(R.id.name_detail);
        TextView description = findViewById(R.id.description_detail);
        TextView date = findViewById(R.id.date_detail);
        name.setText(experiment.getExperimentName());
        description.setText(experiment.getExperimentDescription());
        date.setText(experiment.getExperimentDate());
    }

    // set trails and rate
    @SuppressLint("DefaultLocale")
    protected void setTrails(){
        TextView trails = findViewById(R.id.trails_detail);
        TextView rate = findViewById(R.id.rate_detail);
        StringBuilder showOut = new StringBuilder();

        ArrayList<Boolean> current = experiment.getExperimentTrails();
        for (int i = 0; i < current.size(); i++){
            String midString;
            if(current.get(i) == Boolean.TRUE){
                midString = String.format("Trail%d: Success\n",i+1);
            }
            else{
                midString = String.format("Trail%d: Failure\n",i+1);
            }
            showOut.append(midString);
        }
        trails.setText(showOut);
        rate.setText(experiment.computeRate());
    }

    // add success to trail list
    public void addSuccess(View view) {
        ArrayList<Boolean> trails = experiment.getExperimentTrails();
        trails.add(TRUE);
        experiment.setExperimentTrails(trails);
        setTrails();
    }

    // add failure to trail list
    public void addFailure(View view) {
        ArrayList<Boolean> trails = experiment.getExperimentTrails();
        trails.add(FALSE);
        experiment.setExperimentTrails(trails);
        setTrails();
    }

    // delete current select element, return result to main
    public void deleteExperiment(View view) {
        Intent intent = new Intent(ShowDetail.this,MainActivity.class);
        intent.putExtra("delete",true);
        intent.putExtra("position", position);
        setResult(100, intent);
        finish();
    }

    // confirm change, return result to main
    public void confirmChange(View view){
        Intent intent = new Intent(ShowDetail.this,MainActivity.class);
        setResult(100, intent);
        intent.putExtra("delete",false);
        intent.putExtra("position", position);
        intent.putExtra("experiment",experiment);

        setResult(100, intent);
        finish();
    }

    // when edit button is pressed, and finish input info, check name not none and modify current info.
    @Override
    public void onOkPressed(Experiment newExperiment) {
        String eName = newExperiment.getExperimentName();
        if (eName.matches("")) {
            Toast.makeText(this, "You did not enter a name", Toast.LENGTH_SHORT).show();
        }
        else{
            experiment.setExperimentDate(newExperiment.getExperimentDate());
            experiment.setExperimentDescription(newExperiment.getExperimentDescription());
            experiment.setExperimentName(newExperiment.getExperimentName());
            setText();
        }
    }


}