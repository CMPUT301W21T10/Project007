package com.example.project007;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * This class shows the program start interface.
 * <p>
 * The program obtains permission from the user to obtain system information.<br>
 * The program checks whether the user is a new user for registration or an old user for logs in.<br>
 * </p>
 */
public class LoginActivity extends AppCompatActivity {



    @BindView(R.id.reg_email)
    public TextView regEmail;
    @BindView(R.id.reg_signup)
    public TextView regSignup;
    @BindView(R.id.reg_btn)
    public TextView regBtn;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        reference = FirebaseDatabase.getInstance().getReference();
        reload();
    }
    /**
     * Tinitialization
     */
    private void reload() {
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
    }
    /**
     *  * The program obtains permission from the user to obtain system information.<br>
     * @return Return AndroidId.
     */
    public String getAndroidId(Context context) {
        return Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //
                reference.child("data").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
                        boolean logged = false;
                        while (iterator.hasNext()) {
                            DataSnapshot next = iterator.next();
                            if (next.getKey().equals(getAndroidId(LoginActivity.this))) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class).putExtra("uid", next.getKey()));
                                logged = true;
                                Toast.makeText(LoginActivity.this, " Uid login successfully!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                        if (!logged) {
                            regEmail.setText(getAndroidId(LoginActivity.this));
                            Toast.makeText(LoginActivity.this, " please registry!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, RegActivity.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                Toast.makeText(this, "Permissions err", Toast.LENGTH_SHORT).show();
            }
        }

    }

/*
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
    }*/
}
