package com.example.project007;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegActivity extends AppCompatActivity {
    @BindView(R.id.reg_name)
    EditText regName;
    @BindView(R.id.reg_email)
    EditText regEmail;
    @BindView(R.id.reg_phone)
    EditText regPhone;
    @BindView(R.id.reg_btn)
    TextView regBtn;
    @BindView(R.id.reg_uidtext)
    TextView regUidtext;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        ButterKnife.bind(this);

        reference = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    public Boolean isEmail(String str) {
        Boolean isEmail = false;
        String expr = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        if (str.matches(expr)) {
            isEmail = true;
        }
        return isEmail;
    }

    @OnClick({R.id.reg_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.reg_btn:
                if (TextUtils.isEmpty(regEmail.getText().toString()) || !isEmail(regEmail.getText().toString())) {
                    Toast.makeText(RegActivity.this, " email account error!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(regName.getText().toString())) {
                    Toast.makeText(RegActivity.this, " name is empty!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(regPhone.getText().toString())) {
                    Toast.makeText(RegActivity.this, " phone is empty!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                reference.child("data").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
                        int tempInt =0;
                        while (iterator.hasNext()) {
                            DataSnapshot next = iterator.next();
                            if (next.getKey().equals(getAndroidId(RegActivity.this))){
                                tempInt++;
                            }
                        }
                        if (tempInt>0){
                            Toast.makeText(RegActivity.this, " UID is  exist!",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else{
                            reference.child("data").child(getAndroidId(RegActivity.this)+ "").child("phone").setValue(regPhone.getText().toString());
                            reference.child("data").child(getAndroidId(RegActivity.this) + "").child("username").setValue(regName.getText().toString());
                            reference.child("data").child(getAndroidId(RegActivity.this) + "").child("email").setValue(regEmail.getText().toString());
                            Toast.makeText(RegActivity.this, " UID is registered successfully!",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                break;
        }
    }



    public  String getAndroidId(Context context) {
        String ANDROID_ID = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
        return ANDROID_ID;
    }
}