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

public class QuestionDatabaseController {

    @SuppressLint("StaticFieldLeak")
    private static FirebaseFirestore db;
    private static String UserId;
    private static Integer maxQuestionId;

    public static Integer getMaxQuestionId() {
        return maxQuestionId;
    }

    public static void setMaxQuestionId(Integer maxQuestionId) {
        QuestionDatabaseController.maxQuestionId = maxQuestionId;
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
        QuestionDatabaseController.db = db;
    }

    public static boolean add_Question(String collection, Question question){
        // Retrieving the city name and the province name from the EditText fields
        CollectionReference collectionReference =  db.collection(collection);
        HashMap<String, String> data = new HashMap<>();

        String idString = question.getId().toString();
        data.put("Question", question.getQuestion());
        data.put("Answer_Id", question.getAnswer_id().toString());

        final boolean[] condition = new boolean[1];
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

    public static boolean delete_Question(String collection, Question question){
        CollectionReference collectionReference =  db.collection(collection);
        String idString = question.getId().toString();
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


    public static Integer generateQuestionId(){
        return maxQuestionId + 1;
    }
}