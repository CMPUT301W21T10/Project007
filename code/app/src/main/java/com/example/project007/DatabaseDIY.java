package com.example.project007;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class DatabaseDIY {
    private FirebaseFirestore db;

    public DatabaseDIY(FirebaseFirestore db) {
        this.db = db;
    }

    public boolean delete_document(String value, String collection){
        if (value.length()>0) {
            Task<Void> writeResult = db.collection(collection).document(value).delete();
        }
        return true;
    }

    public boolean add_one(String collection,@Nullable Experiment experiment){
        // Retrieving the city name and the province name from the EditText fields
        CollectionReference collectionReference =  db.collection(collection);
        HashMap<String, String> data = new HashMap<>();

        String idString = experiment.getId().toString();
        data.put("Name", experiment.getName());
        data.put("Description", experiment.getDescription());
        data.put("Date", experiment.getDate());
        data.put("Type", experiment.getType());

        // The set method sets a unique id for the document
        collectionReference
                .document(idString)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d(TAG, "Data has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG, "Data could not be added!" + e.toString());
                    }
                });

        return true;
    }

    public static Integer generateId(){
        return 1;
    }
}
