package com.example.project007;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.reflect.Array;

public class ScanActivity extends AppCompatActivity {
    private TextView textView;
    private String barcode;
    private String isbarcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode_scan_content);
        textView = this.findViewById(R.id.textview_zxing);
        new IntentIntegrator(this)
                .setCaptureActivity(CustomizeScanActivity.class)
                .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)// 扫码的类型,可选：一维码，二维码，一/二维码
                .setCameraId(0)// 选择摄像头,可使用前置或者后置
                .setBeepEnabled(true)// 是否开启声音,扫完码之后会"哔"的一声
                .initiateScan();// 初始化扫码
        Intent intent=getIntent();
        isbarcode=intent.getStringExtra("isBarcode");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                //failure
                Toast toast = Toast.makeText(getApplicationContext(),"Scan failure.",Toast.LENGTH_SHORT);
                toast.show();
            } else {
                String result = intentResult.getContents();//return value
                if (result.contains("Type")){
                    String[] list=result.split(" ");
                    String ID=list[4];
                    int int_result= Integer.parseInt(list[6]);
                    int_result+=1;
                    String new_result=Integer.toString(int_result);
                    final FirebaseFirestore db;
                    db = DatabaseController.getDb();
                    final CollectionReference collectionReference = db.collection("Trails");
                    if (result.contains("success")){
                        collectionReference.document(ID).update("Success",new_result);
                        Toast toast = Toast.makeText(getApplicationContext(),"Success increase 1 of success.",Toast.LENGTH_SHORT);
                        toast.show();
                    }else if (result.contains("failure")){
                        collectionReference.document(ID).update("Failure",new_result);
                        Toast toast = Toast.makeText(getApplicationContext(),"Success increase 1 of failure.",Toast.LENGTH_SHORT);
                        toast.show();
                    }else if (result.contains("Count")){
                        collectionReference.document(ID).update("VariesData",new_result);
                        Toast toast = Toast.makeText(getApplicationContext(),"Success increase 1 of count.",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    textView.setText("Result：\n" + result);
                }
                else {
                    textView.setText("Result：\n" + result);
                    }
                }
                }


            }
        }


