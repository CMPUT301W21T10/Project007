package com.example.project007.ui.subscription;

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

import com.example.project007.DatabaseController;
import com.example.project007.Experiment;
import com.example.project007.ExperimentAdapter;
import com.example.project007.ModifyExperimentFragment;
import com.example.project007.R;
import com.example.project007.TrailsActivity;
import com.example.project007.ui.home.ActionFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This is SubscriptionFragment
 * This class shows subscriptions list
 * connect with TrailsActivity, ActionFragment, and ModifyExperimentFragment
 */
public class SubscriptionFragment extends Fragment {

    private ArrayAdapter<Experiment> experimentAdapter;
    private ArrayAdapter<Experiment> experimentAdapter2;

    private ArrayList<Experiment> experimentDataList;
    private ArrayList<Experiment> experimentDataList2;
    private final String TAG = "Sample";
    private Integer savedPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        experimentDataList = new ArrayList<>();
        experimentDataList2 = new ArrayList<>();

        experimentAdapter = new ExperimentAdapter(this.getContext(), experimentDataList);
        experimentAdapter2 = new ExperimentAdapter(this.getContext(), experimentDataList2);

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
                        Experiment instance = experimentDataList2.get(savedPosition);

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
                                new ModifyExperimentFragment(experimentDataList2.get(savedPosition)).show(getChildFragmentManager(), "EDIT_EXPERIMENT");
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
        View root = inflater.inflate(R.layout.fragment_subscription, container, false);

        ListView experimentList = root.findViewById(R.id.subscript_list);
        experimentList.setAdapter(experimentAdapter);

        ListView experimentList2 = root.findViewById(R.id.own_list);
        experimentList2.setAdapter(experimentAdapter2);

        final FirebaseFirestore db;
        db = DatabaseController.getDb();
        final CollectionReference collectionReference = db.collection("Experiments");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                // Clear the old list
                if (error!=null){
                    Log.d(TAG,"Error:"+error.getMessage());
                }
                else {
                    experimentDataList.clear();
                    experimentDataList2.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                        Experiment oneExperiment = null;
                        if (doc.exists()) {
                            // convert document to POJO
                            oneExperiment = doc.toObject(Experiment.class);
                            System.out.println(oneExperiment);

                            if (oneExperiment.getSubscriptionId().contains(DatabaseController.getUserId())){
                                experimentDataList.add(oneExperiment);
                            }
                            if (oneExperiment.getUserId().equals((DatabaseController.getUserId()))){
                                experimentDataList2.add(oneExperiment);
                            }
                        } else {
                            System.out.println("No such document!");
                        }

                    }
                    experimentAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
                    experimentAdapter2.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
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

        experimentList2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), TrailsActivity.class);
                Experiment instanceExperiment = experimentDataList2.get(position);
                intent.putExtra("com.example.project007.INSTANCE", instanceExperiment);
                intent.putExtra("com.example.project007.POSITION", position);
                startActivity(intent);
            }
        });

        experimentList2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Experiment instance = experimentDataList2.get(position);

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