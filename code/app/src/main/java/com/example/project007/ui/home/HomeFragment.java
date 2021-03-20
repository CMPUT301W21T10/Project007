package com.example.project007.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.project007.Question;
import com.example.project007.QuestionDatabaseController;
import com.example.project007.R;
import com.example.project007.TrailsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ListView experimentList;
    private ArrayAdapter<Experiment> experimentAdapter;
    private ArrayList<Experiment> experimentDataList;
    final String TAG = "Sample";
    private HomeViewModel homeViewModel;
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
                            Toast.makeText(getActivity(), "Add Succeed", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getActivity(), "Add Failed", Toast.LENGTH_SHORT).show();
                        }
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
                            case "edit":
                                new ModifyExperimentFragment(experimentDataList.get(savedPosition)).show(getChildFragmentManager(), "EDIT_EXPERIMENT");
                                Toast.makeText(getActivity(), "edit Succeed", Toast.LENGTH_SHORT).show();
                                break;

                            case "delete":
                                DatabaseController.deleteExperiment(String.valueOf(instance.getId()));
                                //experimentDataList.remove(savedPosition);  //把数据源里面相应数据删除
                                //experimentAdapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), "delete Succeed", Toast.LENGTH_SHORT).show();
                                break;

                            case "end":
                                if (instance.isCondition()){
                                    Integer minimum = instance.getMinimumTrails();

                                    if (instance.getTrailsId().size() >= minimum){
                                        instance.setCondition(false);
                                        Toast.makeText(getActivity(), "end Succeed", Toast.LENGTH_SHORT).show();
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
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        experimentList = root.findViewById(R.id.experiment_list);
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
                        ArrayList<String> trailsId = (ArrayList<String>) doc.getData().get("trailsId");
                        ArrayList<String> subscriptionId = (ArrayList<String>) doc.getData().get("subscriptionId");
                        boolean requireLocation = Boolean.parseBoolean((String) doc.getData().get("requireLocation"));
                        boolean condition = Boolean.parseBoolean((String) doc.getData().get("condition"));
                        Integer minimumTrails = ((Long) doc.getData().get("minimumTrails")).intValue();
                        String region = (String) doc.getData().get("region");
                        String idString = doc.getId();
                        Integer id = Integer.parseInt(idString);
                        experimentDataList.add(new Experiment(name, description, date, type, id,
                                trailsId,subscriptionId, requireLocation,condition,minimumTrails,region));
                    }
                    DatabaseController.setMaxExperimentId(experimentDataList.size());
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
                savedPosition = position;
                new ActionFragment().show(getChildFragmentManager(), "requireAction");
                return true;
            }
        });

        return root;
    }
}