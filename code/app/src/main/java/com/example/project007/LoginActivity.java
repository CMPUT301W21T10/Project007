package com.example.project007;

import android.content.Intent;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {



    @BindView(R.id.reg_email)
    EditText regEmail;
    @BindView(R.id.reg_signup)
    TextView regSignup;
    @BindView(R.id.reg_btn)
    TextView regBtn;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        reference = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void reload() {

    }


    @OnClick({R.id.reg_btn, R.id.reg_signup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.reg_signup:
                startActivity(new Intent(LoginActivity.this, RegActivity.class));
                break;
            case R.id.reg_btn:
                if (TextUtils.isEmpty(regEmail.getText().toString())) {
                    Toast.makeText(LoginActivity.this, " Uid is empty!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                List<UserEntity> userEntityList = new ArrayList<>();
                reference.child("data").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
                        boolean logged = false;
                        while (iterator.hasNext()){
                            DataSnapshot next = iterator.next();
                            if (next.getKey().equals(regEmail.getText().toString())){
                                startActivity(new Intent(LoginActivity.this, MainActivity.class).putExtra("uid",next.getKey()));
                                logged = true;
                                finish();
                            }
                        }
                        if (!logged){
                            Toast.makeText(LoginActivity.this, " Uid err!",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
        }
    }
}