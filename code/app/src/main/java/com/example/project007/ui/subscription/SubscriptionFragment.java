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
import com.example.project007.TrailsActivity;
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
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        subscriptionViewModel =
                new ViewModelProvider(this).get(SubscriptionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_subscription, container, false);

        experimentList = root.findViewById(R.id.subscript_list);
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
                        ArrayList<String> trailsId = (ArrayList<String>) doc.getData().get("trailsId");
                        ArrayList<String> subscriptionId = (ArrayList<String>) doc.getData().get("subscriptionId");
                        boolean requireLocation = Boolean.parseBoolean((String) doc.getData().get("requireLocation"));
                        boolean condition = Boolean.parseBoolean((String) doc.getData().get("condition"));
                        Integer minimumTrails = ((Long) doc.getData().get("minimumTrails")).intValue();
                        String region = (String) doc.getData().get("region");
                        String idString = doc.getId();
                        Integer id = Integer.parseInt(idString);

                        if (subscriptionId != null){
                            if (subscriptionId.contains(DatabaseController.getUserId())){
                                experimentDataList.add(new Experiment(name, description, date, type, id,
                                        trailsId, subscriptionId, requireLocation, condition, minimumTrails, region));
                            }
                        }

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
                Intent intent = new Intent(getActivity(), TrailsActivity.class);
                Experiment instanceExperiment = experimentDataList.get(position);
                intent.putExtra("com.example.project007.INSTANCE", instanceExperiment);
                intent.putExtra("com.example.project007.POSITION", position);
                startActivity(intent);
            }
        });
        return root;
    }
}