package com.example.project007;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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

import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * This class shows the registered interface.
 * <p>
 * The program requests personal information from the user for registration.<br>
 * The program automatically obtains the Android ID in the device for user registration.<br>
 * </p>
 */
public class RegActivity extends AppCompatActivity {
    @BindView(R.id.reg_name)
    public EditText regName;
    @BindView(R.id.reg_email)
    public EditText regEmail;
    @BindView(R.id.reg_phone)
    public EditText regPhone;
    @BindView(R.id.reg_btn)
    public TextView regBtn;
    @BindView(R.id.reg_uidtext)
    public TextView regUidtext;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        ButterKnife.bind(this);

        reference = FirebaseDatabase.getInstance().getReference();

    }

    /**
     *  Determine the mailbox format entered by the user<br>
     * @return the result of checking wether is a valid email or not.
     */
    public Boolean isEmail(String str) {
        boolean isEmail = false;
        String expr = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        if (str.matches(expr)) {
            isEmail = true;
        }
        return isEmail;
    }

    @OnClick({R.id.reg_btn})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.reg_btn) {
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
                    int tempInt = 0;
                    while (iterator.hasNext()) {
                        DataSnapshot next = iterator.next();
                        if (next.getKey().equals(getAndroidId(RegActivity.this))) {
                            tempInt++;
                        }
                    }
                    if (tempInt > 0) {
                        Toast.makeText(RegActivity.this, " UID is  exist!",
                                Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        reference.child("data").child(getAndroidId(RegActivity.this) + "").child("phone").setValue(regPhone.getText().toString());
                        reference.child("data").child(getAndroidId(RegActivity.this) + "").child("username").setValue(regName.getText().toString());
                        reference.child("data").child(getAndroidId(RegActivity.this) + "").child("email").setValue(regEmail.getText().toString());
                        Toast.makeText(RegActivity.this, " UID is registered successfully!",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegActivity.this, MainActivity.class).putExtra("uid", reference.child("data").child(getAndroidId(RegActivity.this) + "").getKey()));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
    /**
     *  * The program obtains permission from the user to obtain system information.<br>
     * @return Return AndroidId.
     */
    public  String getAndroidId(Context context) {
        return Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
    }
}