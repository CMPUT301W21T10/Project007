package com.example.project007.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.project007.CustomList;
import com.example.project007.Experiment;
import com.example.project007.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ListView experimentList;
    private ArrayAdapter<Experiment> experimentAdapter;
    private ArrayList<Experiment> experimentDataList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init adapter
        experimentDataList = new ArrayList<>();
        experimentAdapter = new CustomList(this, experimentDataList);


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        experimentList = root.findViewById(R.id.experiment_list);
        experimentList.setAdapter(experimentAdapter);

        // Listener of add new instance button
        final FloatingActionButton addCityButton = root.findViewById(R.id.add_experiment_button);
        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // new AddExperimentFragment().show(getSupportFragmentManager(), "ADD_EXPERIMENT");
            }
        });

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
              //  textView.setText(s);
            }
        });
        return root;
    }
}