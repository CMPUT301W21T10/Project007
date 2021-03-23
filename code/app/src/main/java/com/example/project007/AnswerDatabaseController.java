package com.example.project007;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class AnswerDatabaseController {

    @SuppressLint("StaticFieldLeak")
    private static FirebaseFirestore db; // answer db
    private static String UserId = "1";
    private static Integer maxAnswerId = 0;

    public static Integer getMaxAnswerId() {
        return maxAnswerId;
    }

    public static void setMaxAnswerId(Integer maxAnswerId) {
        AnswerDatabaseController.maxAnswerId = maxAnswerId;
    }

    public static String getUserId() {
        return UserId;
    }

    public static void setUserId(String userId) {
        UserId = userId;
    }

    public static FirebaseFirestore getDb() {
        return db;
    }

    public static void setDb(FirebaseFirestore db) {
        AnswerDatabaseController.db = db;
    }

    public static boolean add_Answer(String collection, Answer answer){
        CollectionReference collectionReference =  db.collection(collection);
        HashMap<String, String> data = new HashMap<>();

        String idString = answer.getId().toString();
        data.put("Answer", answer.getAnswer());

        final boolean[] condition = new boolean[1];
        // The set method sets a unique id for the document
        collectionReference
                .document(idString)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d(TAG, "Data has been added successfully!");
                        condition[0] = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG, "Data could not be added!" + e.toString());
                        condition[0] = false;
                    }
                });
        return condition[0];
    }

    public static boolean delete_Answer(String collection, Answer answer){
        CollectionReference collectionReference =  db.collection(collection);
        String idString = answer.getId().toString();
        final boolean[] condition = new boolean[1];
        collectionReference
                .document(idString)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Data has been deleted successfully!");
                        condition[0] = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data could not be deleted!" + e.toString());
                        condition[0] = false;
                    }
                });
        return condition[0];
    }


    public static Integer generateAnswerId(){
        return maxAnswerId + 1;
    }
}
