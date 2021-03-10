package com.example.project007;

import android.annotation.SuppressLint;
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
                List<UserEntity> userEntityList = new ArrayList<>();
                reference.child("data").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
                        while (iterator.hasNext()) {
                            DataSnapshot next = iterator.next();
                            UserEntity userEntity = new UserEntity();
                            userEntity.setUid(next.getKey());
                            userEntity.setUsername(next.child("username").getValue().toString());
                            userEntity.setPhone(next.child("phone").getValue().toString());
                            userEntity.setEmail(next.child("email").getValue().toString());
                            userEntityList.add(userEntity);
                        }
                        if (userEntityList.size() > 0)
                            updateUI(userEntityList.get(userEntityList.size() - 1));
                        else
                            updateUI(null);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(UserEntity user) {
        // Writes name to  real-time database
        if (user != null) {
            reference.child("data").child((Integer.parseInt(user.getUid()) + 1) + "").child("phone").setValue(regPhone.getText().toString());
            reference.child("data").child((Integer.parseInt(user.getUid()) + 1) + "").child("username").setValue(regName.getText().toString());
            reference.child("data").child((Integer.parseInt(user.getUid()) + 1) + "").child("email").setValue(regEmail.getText().toString());

            regUidtext.setText("sign up success! uid is "+(Integer.parseInt(user.getUid()) + 1));
        } else {
            reference.child("data").child(1 + "").child("phone").setValue(regPhone.getText().toString());
            reference.child("data").child(1 + "").child("username").setValue(regName.getText().toString());
            reference.child("data").child(1 + "").child("email").setValue(regEmail.getText().toString());
            regUidtext.setText("sign up success! uid is "+ 1);
        }
        Toast.makeText(RegActivity.this, " sign up success!",
                Toast.LENGTH_SHORT).show();
    }

}