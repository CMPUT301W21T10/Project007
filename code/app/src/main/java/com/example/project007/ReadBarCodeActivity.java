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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This activity read Barcode
 * <p>
 * Use camera to get info from a bar code.<br>
 * </p>
 */
public class ReadBarCodeActivity extends AppCompatActivity {
    private String isbarcode="false";
    private String trail_id;
    final String TAG = "Trails_Sample";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        setContentView(R.layout.qrcode_scan_content);
        TextView textView = this.findViewById(R.id.textview_zxing);
        isbarcode=intent.getStringExtra("IsBarcode");
        String userid = intent.getStringExtra("ID");
        trail_id=intent.getStringExtra("trail_id");
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
                Toast toast = Toast.makeText(getApplicationContext(),"Cancel.",Toast.LENGTH_SHORT);
                toast.show();
                finish();
            } else if (isbarcode.equals("true")){
                String databaseUserId = DatabaseController.getUserId();
                String result = intentResult.getContents();//return value
                //delete the one already in the database
                Query value = collectionReference.whereEqualTo(databaseUserId,result);
                value.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Log.d(TAG, document.getId());
                                if(!document.getId().equals(trail_id)){
                                    final DocumentReference DocumentReference = db.collection("Trails").document(document.getId());
                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put(databaseUserId, FieldValue.delete());
                                    DocumentReference.update(updates);}
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());

                        }   }
                });
                //add new one
                Map<String, String> adding = new HashMap<>();
                adding.put(databaseUserId, result);
                db.collection("Trails").document(trail_id)
                        .set(adding, SetOptions.merge());
                Toast toast = Toast.makeText(getApplicationContext(), "Successfully update barcode register.", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        }
    }
}

