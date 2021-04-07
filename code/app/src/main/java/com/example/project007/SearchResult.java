package com.example.project007;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project007.ui.subscription.SubscriptionViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SearchResult extends AppCompatActivity {

    private SubscriptionViewModel subscriptionViewModel;
    private ListView experimentList;
    private ArrayAdapter<Experiment> experimentAdapter;
    private ArrayList<Experiment> experimentDataList = new ArrayList<>();
    final String TAG = "Sample";
    private String searchKey = "";
    ArrayList<UserEntity> userList = new ArrayList<UserEntity>();
    public static boolean condition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();
        searchKey = intent.getStringExtra("Key");
        experimentAdapter = new ExperimentAdapter(this, experimentDataList);
        experimentList = findViewById(R.id.subscript_list);
        experimentList.setAdapter(experimentAdapter);

        final FirebaseFirestore db;
        db = DatabaseController.getDb();
        final CollectionReference collectionReference = db.collection("Experiments");

        final CollectionReference collectionReference2 = db.collection("Users");
        collectionReference2.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                // Clear the old list
                if (error!=null){
                    Log.d(TAG,"Error:"+error.getMessage());
                }
                else {
                    userList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                        UserEntity user = null;
                        if (doc.exists()) {
                            // convert document to POJO
                            user = doc.toObject(UserEntity.class);
                            System.out.println(user);

                            if (user.getUsername().contains(searchKey)){
                                userList.add(user);
                            }
                        } else {
                            System.out.println("No such document!");
                        }
                    }
                }

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

                                    if (processData(oneExperiment)){
                                        experimentDataList.add(oneExperiment);
                                    }
                                    if (!experimentDataList.contains(oneExperiment)){
                                        for (int i = 0 ;  i < userList.size(); i ++){
                                            if (userList.get(i).getUid().equals(oneExperiment.getUserId())){
                                                experimentDataList.add(oneExperiment);
                                                break;
                                            }
                                        }
                                    }

                                } else {
                                    System.out.println("No such document!");
                                }

                            }
                        }
                        experimentAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched

                    }
                });



            }
        });
        if(experimentDataList.size() == 0) {
            Toast.makeText(SearchResult.this, "No related result.",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
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

    public boolean processData( Experiment experiment){

        if (!experiment.isPublishCondition()){
            return false;
        }
        if (experiment.getName().contains(searchKey)){
            return true;

        }
        if (experiment.getDescription().contains(searchKey)){
            return true;

        }
        if (experiment.getRegion().contains(searchKey)){
            return true;

        }
        if (experiment.getType().contains(searchKey)){
            return true;

        }
        if (experiment.getDate().contains(searchKey)){
            return true;

        }

        if (experiment.isCondition() && searchKey.equals("End")){
            return true;

        }
        if (!experiment.isCondition() && searchKey.equals("Processing")){
            return true;
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();

                while (iterator.hasNext()){
                    DataSnapshot next = iterator.next();
                    if (next.child("username").getValue().toString().contains(searchKey)){
                        UserEntity userEntity = new UserEntity();
                        userEntity.setEmail(next.child("email").getValue().toString());
                        userEntity.setPhone(next.child("phone").getValue().toString());
                        userEntity.setUid(next.getKey());
                        userEntity.setUsername(next.child("username").getValue().toString());
                        final FirebaseFirestore db;
                        db = FirebaseFirestore.getInstance();
                        DatabaseController.setDb(db);
                        final CollectionReference collectionReference = db.collection("Users");
                        collectionReference.document(userEntity.getUid()).set(userEntity);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return false;

    }
}