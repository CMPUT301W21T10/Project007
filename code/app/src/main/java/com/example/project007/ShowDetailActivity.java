package com.example.project007;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class ShowDetailActivity extends AppCompatActivity {

    private static Experiment experiment;
    private static Integer position;

    // unPackage info and set basic UI
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);

        Intent intent = getIntent();
        experiment = (Experiment) intent.getSerializableExtra("com.example.project007.INSTANCE");
        position = intent.getIntExtra("com.example.project007.POSITION", -1);

        // set value to textView
        setText();

        // listener to show fragment
        final Button editButton = findViewById(R.id.edit_experiment_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ModifyExperimentFragment(experiment).show(getSupportFragmentManager(), "ADD_EXPERIMENT");
            }
        });
    }

    // set name, description, date
    protected void setText(){
        TextView name = findViewById(R.id.name_detail);
        TextView description = findViewById(R.id.description_detail);
        TextView date = findViewById(R.id.date_detail);
        name.setText(experiment.getName());
        description.setText(experiment.getDescription());
        date.setText(experiment.getDate());
    }

    // delete current select element, return result to main
    public void deleteExperiment(View view) {
        Intent intent = new Intent(ShowDetailActivity.this,MainActivity.class);
        intent.putExtra("delete",true);
        intent.putExtra("position", position);
        setResult(100, intent);
        finish();
    }

    // confirm change, return result to main
    public void confirmChange(View view){
        Intent intent = new Intent(ShowDetailActivity.this,MainActivity.class);
        setResult(100, intent);
        intent.putExtra("delete",false);
        intent.putExtra("position", position);
        intent.putExtra("experiment",experiment);

        setResult(100, intent);
        finish();
    }

    /*
    // when edit button is pressed, and finish input info, check name not none and modify current info.
    @Override
    public void onOkPressed(Experiment newExperiment) {
        String eName = newExperiment.getName();
        if (eName.matches("")) {
            Toast.makeText(this, "You did not enter a name", Toast.LENGTH_SHORT).show();
        }
        else{
            experiment.setDate(newExperiment.getDate());
            experiment.setDescription(newExperiment.getDescription());
            experiment.setName(newExperiment.getName());
            setText();
        }
    }
    */



}