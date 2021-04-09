package com.example.project007;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Map;

/**
 * This class the default frame of scanning
 * <p>
 * Base frame of scanning.<br>
 * </p>
 */
public class ScanActivity extends AppCompatActivity {
    private TextView textView;
    private String userid;
    private final String TAG="trail_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        setContentView(R.layout.qrcode_scan_content);
        textView = this.findViewById(R.id.textview_zxing);
        new IntentIntegrator(this)
                .setCaptureActivity(CustomizeScanActivity.class)
                .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)// choose the scan type we want to use
                .setCameraId(0)// choose camera
                .setBeepEnabled(true)// choose if use warning tone
                .initiateScan();// initialize

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {
            final FirebaseFirestore db;
            db = DatabaseController.getDb();
            final CollectionReference collectionReference = db.collection("Trails");
            if (intentResult.getContents() == null) {
                //failure
                Toast toast = Toast.makeText(getApplicationContext(),"Cancel Scanning.",Toast.LENGTH_SHORT);
                toast.show();
                finish();
            } else {
                String result = intentResult.getContents();//return value
                if (result.contains("Type")){
                    String[] list=result.split(" ");
                    String ID=list[4];
                    if (result.contains("success")){
                        collectionReference.document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                        String value=document.getString("Success");
                                        int int_result= Integer.parseInt(value);
                                        int_result+=1;
                                        String new_result=Integer.toString(int_result);
                                        collectionReference.document(ID).update("Success",new_result);
                                        Toast toast = Toast.makeText(getApplicationContext(),"Successfully increase 1 of Success.",Toast.LENGTH_SHORT);
                                        toast.show();
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
                    }else if (result.contains("failure")){
                        collectionReference.document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                        String value=document.getString("Failure");
                                        int int_result= Integer.parseInt(value);
                                        int_result+=1;
                                        String new_result=Integer.toString(int_result);
                                        collectionReference.document(ID).update("Failure",new_result);
                                        Toast toast = Toast.makeText(getApplicationContext(),"Successfully increase 1 of Failure.",Toast.LENGTH_SHORT);
                                        toast.show();
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
                    }else if (result.contains("Count")){
                        collectionReference.document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                        String value=document.getString("VariesData");
                                        int int_result= Integer.parseInt(value);
                                        int_result+=1;
                                        String new_result=Integer.toString(int_result);
                                        collectionReference.document(ID).update("VariesData",new_result);
                                        Toast toast = Toast.makeText(getApplicationContext(),"Successfully increase 1 of Count.",Toast.LENGTH_SHORT);
                                        toast.show();
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
                    }
                    textView.setText("Result：\n" + result);
                }
                else {
                    String databaseUserId = DatabaseController.getUserId();
                    Query trail_id = db.collection("Trails").whereEqualTo(databaseUserId, result);
                    trail_id.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Map<String, Object> value = null;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    if(document.getData().size()>0){
                                        value=document.getData(); } }
                                if (value == null){textView.setText("Result：\n" +result);}else {
                                textView.setText("Result：\n" +value);}}
                            else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                                textView.setText("Result：\n" +result);
                            }
                        }
                    });

                }
            }
        }
    }
}

