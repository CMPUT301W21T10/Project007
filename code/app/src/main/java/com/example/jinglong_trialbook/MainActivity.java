// Some part referenced from ListyCity Project
// More reference see README.txt

package com.example.jinglong_trialbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity implements AddExperimentFragment.OnFragmentInteractionListener {

    // Declare the variables so that you will be able to reference it later.
    private ListView experimentList;
    private ArrayAdapter<Experiment> experimentAdapter;
    private ArrayList<Experiment> experimentDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init adapter
        experimentList = findViewById(R.id.experiment_list);
        experimentDataList = new ArrayList<>();
        experimentAdapter = new CustomList(this, experimentDataList);
        experimentList.setAdapter(experimentAdapter);

        // Listener of add new instance button
        final FloatingActionButton addCityButton = findViewById(R.id.add_experiment_button);
        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddExperimentFragment().show(getSupportFragmentManager(), "ADD_EXPERIMENT");
            }
        });

        // listener to access detail of an element
        // package an experiment and position info in intent
        experimentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ShowDetail.class);
                Experiment instanceExperiment = experimentDataList.get(position);
                intent.putExtra("com.example.jinglong_trialbook.INSTANCE", instanceExperiment);
                intent.putExtra("com.example.jinglong_trialbook.POSITION", position);
                startActivityForResult(intent, 100);
            }
        });

    }

    // this method help refresh main activity when something is modified in ShowDetail activity.
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        experimentAdapter.notifyDataSetChanged();

    }

    // called when somebody done add_experiment_fragment and pressed OK
    // Name should not be empty or get notification and fail to add
    @Override
    public void onOkPressed(Experiment newExperiment){
        String eName = newExperiment.getExperimentName();
        if (eName.matches("")) {
            Toast.makeText(this, "You did not enter a name", Toast.LENGTH_SHORT).show();
        }
        else{
            experimentAdapter.add(newExperiment);
            experimentAdapter.notifyDataSetChanged();
        }
    }

    // Returned result, if somebody press confirm change button or delete button from detail xml
    // modify or delete adapter element
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (data != null) {
                boolean needDelete = data.getBooleanExtra("delete", FALSE);
                int position = data.getIntExtra("position", -1);
                if (needDelete && position >= 0) {
                    experimentAdapter.remove(experimentDataList.get(position));
                } else {
                    Experiment currentExperiment = (Experiment) data.getSerializableExtra("experiment");
                    experimentDataList.set(position, currentExperiment);
                }
                experimentAdapter.notifyDataSetChanged();
            }
        }
    }


}