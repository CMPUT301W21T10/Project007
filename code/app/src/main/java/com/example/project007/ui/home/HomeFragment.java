package com.example.project007.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.project007.ModifyExperimentFragment;
import com.example.project007.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements ModifyExperimentFragment.OnFragmentInteractionListener{

    private ListView experimentList;
    private ArrayAdapter<Experiment> experimentAdapter;
    private ArrayList<Experiment> experimentDataList;
    private Context context;
    private FirebaseFirestore db;
    final String TAG = "Sample";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // init adapter
        experimentDataList = new ArrayList<>();
        experimentAdapter = new CustomList(this.getContext(), experimentDataList);
        experimentList = root.findViewById(R.id.experiment_list);
        experimentList.setAdapter(experimentAdapter);
        this.context=getActivity();
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Experiments");

        // Listener of add new instance button
        final FloatingActionButton addCityButton = root.findViewById(R.id.add_experiment_button);
        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ModifyExperimentFragment().show(getFragmentManager(), "ADD_EXPERIMENT");
            }
        });


        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                // Clear the old list
                experimentDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    Log.d(TAG, String.valueOf(doc.getData().get("Name")));
                    String name = (String) doc.getData().get("Name");
                    String description = (String) doc.getData().get("Description");
                    String date = (String) doc.getData().get("Date");
                    String type = (String) doc.getData().get("Type");
                    String idString = doc.getId();
                    Integer id= Integer.parseInt(idString);

                    experimentDataList.add(new Experiment(name,description,date,type,id)); // Adding the cities and provinces from FireStore
                }
                experimentAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched

            }
        });



        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //  textView.setText(s);
            }
        });
        return root;
    }

    public void initDatabase(){

    }

    @Override
    public void onOkPressed(Experiment newExperiment) {

    }
}