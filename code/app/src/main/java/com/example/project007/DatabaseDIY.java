package com.example.project007;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public abstract class DatabaseDIY {
    private FirebaseFirestore db;

    public void setDatabase(FirebaseFirestore instanceDb){
        this.db = instanceDb;
    }


    public boolean delete_document(String value, String collection){
        if (value.length()>0) {
            Task<Void> writeResult = db.collection(collection).document(value).delete();
        }
        return true;
    }

    public boolean add_one(String collection){

        return true;
    }

    public static Integer generateId(){
        return 1;
    }
}
