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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

    private SubscriptionViewModel subscriptionViewModel;
    private ListView experimentList;
    private ListView experimentList2;
    private ArrayAdapter<Experiment> experimentAdapter;
    private ArrayAdapter<Experiment> experimentAdapter2;

    private ArrayList<Experiment> experimentDataList;
    private ArrayList<Experiment> experimentDataList2;
    final String TAG = "Sample";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        experimentDataList = new ArrayList<>();
        experimentDataList2 = new ArrayList<>();

        experimentAdapter = new ExperimentAdapter(this.getContext(), experimentDataList);
        experimentAdapter2 = new ExperimentAdapter(this.getContext(), experimentDataList2);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        subscriptionViewModel =
                new ViewModelProvider(this).get(SubscriptionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_subscription, container, false);

        experimentList = root.findViewById(R.id.subscript_list);
        experimentList.setAdapter(experimentAdapter);

        experimentList2 = root.findViewById(R.id.own_list);
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
                            }                        } else {
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
                Experiment instanceExperiment = experimentDataList.get(position);
                intent.putExtra("com.example.project007.INSTANCE", instanceExperiment);
                intent.putExtra("com.example.project007.POSITION", position);
                startActivity(intent);
            }
        });
        return root;
    }
}