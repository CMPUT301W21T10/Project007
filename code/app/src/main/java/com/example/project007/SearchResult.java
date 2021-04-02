package com.example.project007;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.project007.ui.subscription.SubscriptionViewModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchResult extends AppCompatActivity {

    private SubscriptionViewModel subscriptionViewModel;
    private ListView experimentList;
    private ArrayAdapter<Experiment> experimentAdapter;
    private ArrayList<Experiment> experimentDataList;
    final String TAG = "Sample";
    private String searchKey = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();
        searchKey = intent.getStringExtra("Key");

        experimentDataList = new ArrayList<>();
        experimentAdapter = new ExperimentAdapter(this, experimentDataList);
        experimentList = findViewById(R.id.subscript_list);
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

                        Experiment oneExperiment = null;
                        if (doc.exists()) {
                            // convert document to POJO
                            oneExperiment = doc.toObject(Experiment.class);
                            System.out.println(oneExperiment);
                            experimentDataList.add(oneExperiment);

                        } else {
                            System.out.println("No such document!");
                        }

                    }
                    processData();
                    experimentAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
                }
            }
        });

        // listener to access detail of an element
        // package an experiment and position info in intent
        experimentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchResult.this, TrailsActivity.class);
                Experiment instanceExperiment = experimentDataList.get(position);
                intent.putExtra("com.example.project007.INSTANCE", instanceExperiment);
                intent.putExtra("com.example.project007.POSITION", position);
                startActivity(intent);
            }
        });

    }

    private void processData(){
        ArrayList<Experiment> result = new ArrayList<Experiment>();
        for (int i = 0; i<experimentDataList.size(); i++){
            Experiment experiment = experimentDataList.get(i);


            if (experiment.getName().contains(searchKey)){
                result.add(experiment);
                break;
            }
            if (experiment.getDescription().contains(searchKey)){
                result.add(experiment);
                break;
            }
            if (experiment.getRegion().contains(searchKey)){
                result.add(experiment);
                break;
            }
            if (experiment.getType().contains(searchKey)){
                result.add(experiment);
                break;
            }
            if (experiment.getDate().contains(searchKey)){
                result.add(experiment);
                break;
            }
            if (experiment.isCondition() && searchKey.equals("End")){
                result.add(experiment);
                break;
            }
            if (!experiment.isCondition() && searchKey.equals("Processing")){
                result.add(experiment);
                break;
            }

        }
        experimentDataList = result;

    }
}