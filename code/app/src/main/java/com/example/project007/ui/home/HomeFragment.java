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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.project007.CustomList;
import com.example.project007.DatabaseController;
import com.example.project007.Experiment;
import com.example.project007.ModifyExperimentFragment;
import com.example.project007.R;
import com.example.project007.ShowDetailActivity;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nonnull;

public class HomeFragment extends Fragment {

    private ListView experimentList;
    private ArrayAdapter<Experiment> experimentAdapter;
    private ArrayList<Experiment> experimentDataList;
    final String TAG = "Sample";
    private HomeViewModel homeViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        experimentDataList = new ArrayList<>();
        experimentDataList.add(new Experiment("name","description","date","type",1)); // Adding the cities and provinces from FireStore
        experimentAdapter = new CustomList(this.getContext(), experimentDataList);


        getChildFragmentManager()
                .setFragmentResultListener("homeRequest", this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                        // Do something with the result
                        Experiment experiment = (Experiment) bundle.getSerializable("com.example.project007.modifiedExperiment");
                        boolean addResult = DatabaseController.add_one("Experiments", experiment);
                        if (addResult){
                            Toast.makeText(getActivity(), "Add Succeed", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getActivity(), "Add Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //FirebaseApp.initializeApp(this.getContext());

        /*
        final TextView textView = root.findViewById(R.id.trytry);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        // init adapter

        experimentList = root.findViewById(R.id.experiment_list);
        experimentList.setAdapter(experimentAdapter);

        final FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        DatabaseController.setDb(db);
        final CollectionReference collectionReference = db.collection("Experiments");

        // Listener of add new instance button
        final FloatingActionButton addCityButton = root.findViewById(R.id.add_experiment_button);
        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ModifyExperimentFragment().show(getChildFragmentManager(), "ADD_EXPERIMENT");

            }
        });

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                // Clear the old list
                if (error!=null){
                    Log.d(TAG,"Error:"+error.getMessage());
                }
                else {
                    experimentDataList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Log.d(TAG, String.valueOf(doc.getData().get("Name")));
                        String name = (String) doc.getData().get("Name");
                        String description = (String) doc.getData().get("Description");
                        String date = (String) doc.getData().get("Date");
                        String type = (String) doc.getData().get("Type");
                        String idString = doc.getId();
                        Integer id = Integer.parseInt(idString);

                        experimentDataList.add(new Experiment(name, description, date, type, id)); // Adding the cities and provinces from FireStore
                    }
                    experimentAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
                }
            }
        });




        // listener to access detail of an element
        // package an experiment and position info in intent
        experimentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ShowDetailActivity.class);
                Experiment instanceExperiment = experimentDataList.get(position);
                intent.putExtra("com.example.project007.INSTANCE", instanceExperiment);
                intent.putExtra("com.example.project007.POSITION", position);
                startActivity(intent);
            }
        });

        return root;
    }
}