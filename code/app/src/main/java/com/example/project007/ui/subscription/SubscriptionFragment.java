package com.example.project007.ui.subscription;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.project007.DatabaseController;
import com.example.project007.Experiment;
import com.example.project007.ExperimentAdapter;
import com.example.project007.R;
import com.example.project007.ui.home.HomeViewModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SubscriptionFragment extends Fragment {

    private SubscriptionViewModel subscriptionViewModel;
    private ListView experimentList;
    private ArrayAdapter<Experiment> experimentAdapter;
    private ArrayList<Experiment> experimentDataList;
    final String TAG = "Sample";

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
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        subscriptionViewModel =
                new ViewModelProvider(this).get(SubscriptionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_subscription, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        subscriptionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        experimentList = root.findViewById(R.id.experiment_list);
        experimentList.setAdapter(experimentAdapter);

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
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Log.d(TAG, String.valueOf(doc.getData().get("Name")));
                        String name = (String) doc.getData().get("Name");
                        String description = (String) doc.getData().get("Description");
                        String date = (String) doc.getData().get("Date");
                        String type = (String) doc.getData().get("Type");
                        Integer[] trailsId = (Integer[]) doc.getData().get("trailsId");
                        String[] subscriptionId = (String[]) doc.getData().get("subscriptionId");
                        boolean requireLocation = Boolean.parseBoolean((String) doc.getData().get("requireLocation"));
                        boolean condition = Boolean.parseBoolean((String) doc.getData().get("condition"));
                        Integer minimumTrails = ((Long) doc.getData().get("minimumTrails")).intValue();
                        String region = (String) doc.getData().get("region");
                        String idString = doc.getId();
                        Integer id = Integer.parseInt(idString);
                       // if (subscriptionId.contains(DatabaseController.getUserId())){

                      //  }
                        experimentDataList.add(new Experiment(name, description, date, type, id,
                                trailsId,subscriptionId, requireLocation,condition,minimumTrails,region));
                    }
                    experimentAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
                }
            }
        });

        return root;
    }
}