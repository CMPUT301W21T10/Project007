package com.example.project007.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import com.example.project007.ExperimentAdapter;
import com.example.project007.DatabaseController;
import com.example.project007.Experiment;
import com.example.project007.ModifyExperimentFragment;
import com.example.project007.R;
import com.example.project007.SearchResult;
import com.example.project007.TrailsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This is HomeFragment
 * This class performances main interaction and show experiments
 * connect with TrailsActivity, ActionFragment, and ModifyExperimentFragment
 */

public class HomeFragment extends Fragment {

    private ArrayAdapter<Experiment> experimentAdapter;
    private ArrayList<Experiment> experimentDataList;
    private final String TAG = "Sample";
    private Integer savedPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        experimentDataList = new ArrayList<>();
        experimentAdapter = new ExperimentAdapter(this.getContext(), experimentDataList);

        getChildFragmentManager()
                .setFragmentResultListener("homeRequest", this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                        // Do something with the result
                        Experiment experiment = (Experiment) bundle.getSerializable("com.example.project007.modifiedExperiment");
                        boolean addResult = DatabaseController.modify_experiment("Experiments", experiment);
                        if (addResult){
                            Toast.makeText(getActivity(), "Add/Edit Failed", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getActivity(), "Add/Edit Succeed", Toast.LENGTH_SHORT).show();
                        }//revert logic
                    }
                });

        getChildFragmentManager()
                .setFragmentResultListener("actionRequest", this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                        // Do something with the result
                        Fragment prev = getChildFragmentManager().findFragmentByTag("requireAction");
                        if (prev != null) {
                            DialogFragment df = (DialogFragment) prev;
                            df.dismiss();
                        }

                        String action = bundle.getString("action");
                        Experiment instance = experimentDataList.get(savedPosition);

                        switch (action){
                            case "publish":
                                if (DatabaseController.isPublish()){
                                    instance.setPublishCondition(false);
                                    Toast.makeText(getActivity(), "UnPublish Succeed", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    instance.setPublishCondition(true);
                                    Toast.makeText(getActivity(), "Publish Succeed", Toast.LENGTH_SHORT).show();
                                }
                                DatabaseController.modify_experiment("Experiments",instance);
                                break;

                            case "edit":
                                if (instance.isCondition()){
                                    new ModifyExperimentFragment(experimentDataList.get(savedPosition)).show(getChildFragmentManager(), "EDIT_EXPERIMENT");
                                }
                                else{
                                    Toast.makeText(getActivity(), "End Experiment cannot be modified", Toast.LENGTH_SHORT).show();
                                }

                                break;

                            case "delete":
                                DatabaseController.deleteExperiment(instance);
                                Toast.makeText(getActivity(), "delete Succeed", Toast.LENGTH_SHORT).show();

                                break;

                            case "end":
                                if (instance.isCondition()){
                                    Integer minimum = instance.getMinimumTrails();

                                    if (instance.getTrailsId().size() >= minimum){
                                        instance.setCondition(false);
                                        Toast.makeText(getActivity(), "end Succeed", Toast.LENGTH_SHORT).show();
                                        DatabaseController.modify_experiment("Experiments",instance);
                                    }
                                    else{
                                        Toast.makeText(getActivity(), "do not satisfy minimum trails", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(getActivity(), "Already end", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }



                    }
                });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ListView experimentList = root.findViewById(R.id.experiment_list);
        experimentList.setAdapter(experimentAdapter);

        final FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        DatabaseController.setDb(db);
        final CollectionReference collectionReference = db.collection("Experiments");

        // Listener of add new instance button
        final FloatingActionButton addExperimentButton = root.findViewById(R.id.add_experiment_button);
        addExperimentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DatabaseController.getUserId()!=null){
                    new ModifyExperimentFragment().show(getChildFragmentManager(), "ADD_EXPERIMENT");
                }
                else{
                    Toast.makeText(getActivity(), "You need sign in!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final FloatingActionButton searchButton = root.findViewById(R.id.search_experiment_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout searchBar = root.findViewById(R.id.search_bar);
                searchBar.setVisibility(View.VISIBLE);
            }
        });

        final Button confirmButton = root.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout searchBar = root.findViewById(R.id.search_bar);
                searchBar.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(getActivity(), SearchResult.class);
                EditText searchKey = root.findViewById(R.id.search_key);
                intent.putExtra("Key", searchKey.getText().toString());
                searchKey.setText("");
                startActivity(intent);
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
                    int experimentId = 0;

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                        Experiment oneExperiment = null;
                        if (doc.exists()) {
                            // convert document to POJO
                            oneExperiment = doc.toObject(Experiment.class);
                            System.out.println(oneExperiment);

                            if (oneExperiment.isPublishCondition()){
                                experimentDataList.add(oneExperiment);
                            }
                            if (oneExperiment.getId() > experimentId){
                                experimentId = oneExperiment.getId();
                            }
                        } else {
                            System.out.println("No such document!");
                        }
                    }
                    DatabaseController.setMaxExperimentId(experimentId);
                    experimentAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
                }



            }
        });

        // listener to access detail of an element
        // package an experiment and position info in intent
        experimentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), TrailsActivity.class);
                Experiment instanceExperiment = experimentDataList.get(position);
                intent.putExtra("com.example.project007.INSTANCE", instanceExperiment);
                intent.putExtra("com.example.project007.POSITION", position);
                startActivity(intent);
            }
        });

        experimentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Experiment instance = experimentDataList.get(position);

                if (DatabaseController.getUserId().equals(instance.getUserId())) {
                    savedPosition = position;
                    DatabaseController.setPublish(instance.isPublishCondition());
                    new ActionFragment().show(getChildFragmentManager(), "requireAction");
                }
                else {
                    Toast.makeText(getActivity(), "You are not the owner", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });

        return root;
    }

}