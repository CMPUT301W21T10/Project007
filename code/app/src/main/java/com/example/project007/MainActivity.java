package com.example.project007;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_id)
    TextView mainId;
    @BindView(R.id.main_bt1)
    TextView mainBt1;
    @BindView(R.id.main_bt2)
    TextView mainBt2;
    private PopupWindow popupWindow;
    private DatabaseReference reference;
    private EditText popPhone;
    private EditText popEmail;
    private TextView popId;
    private EditText popUsername;
    private int[] location;
    private TextView popEdit;
    private PopupWindow popupWindow1;
    private TextView popSearch;
    private EditText popUsernameEt;
    private PopupWindow popupWindow2;
    private RecyclerView mRecyclerView;
    private RvAdapter adapter;
    private TextView popEdit1;
    private PopupWindow popupWindow3;
    private List<UserEntity> temp;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mainId.setText("uid:"+getIntent().getStringExtra("uid"));
        reference = FirebaseDatabase.getInstance().getReference();
        initPop();
        initPop3();
        initPop2();
    }

    @SuppressLint("SetTextI18n")
    private void initPop4(UserEntity userEntity) {
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.pop_detail, null);

        popupWindow3 = new PopupWindow(inflate, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        inflate.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow3.setBackgroundDrawable(new BitmapDrawable());
        popupWindow3.setOutsideTouchable(true);
        popupWindow3.setFocusable(true);
        mainBt2.getLocationOnScreen(location);

        TextView popId2 = inflate.findViewById(R.id.pop_id);
        TextView popEdit2 = inflate.findViewById(R.id.pop_dismiss);
        TextView popEmail2 = inflate.findViewById(R.id.pop_email);
        TextView popPhone2 = inflate.findViewById(R.id.pop_phone);
        TextView popUsername2= inflate.findViewById(R.id.pop_username);
        popId2.setText("Uid:"+userEntity.getUid());
        popEmail2.setText("Email:"+userEntity.getEmail());
        popPhone2.setText("Phone:"+userEntity.getPhone());
        popUsername2.setText("UserName:"+userEntity.getUsername());
        popEdit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow3.dismiss();
            }
        });
        popupWindow3.showAtLocation(MainActivity.this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    private void initPop3() {
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.pop_list, null);

        popupWindow2 = new PopupWindow(inflate, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        inflate.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow2.setBackgroundDrawable(new BitmapDrawable());
        popupWindow2.setOutsideTouchable(true);
        popupWindow2.setFocusable(true);
        mainBt2.getLocationOnScreen(location);
        mRecyclerView = inflate.findViewById(R.id.mRecyclerView);
        popEdit1 = inflate.findViewById(R.id.pop_edit);
        popEdit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow2.dismiss();
            }
        });
        StaggeredGridLayoutManager stagger = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setLayoutManager(stagger);
    }

    private void initPop2() {
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.pop_search, null);

        popupWindow1 = new PopupWindow(inflate, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        inflate.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow1.setBackgroundDrawable(new BitmapDrawable());
        popupWindow1.setOutsideTouchable(true);
        popupWindow1.setFocusable(true);
        mainBt2.getLocationOnScreen(location);
        popSearch = inflate.findViewById(R.id.pop_search);
        popUsernameEt = inflate.findViewById(R.id.pop_username);



        popSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(popUsernameEt.getText().toString())){
                    Toast.makeText(MainActivity.this, "username is empty.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                temp = new ArrayList<>();

                reference.child("data").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
                        while (iterator.hasNext()){
                            DataSnapshot next = iterator.next();

                            if (next.child("username").getValue().toString().contains(popUsernameEt.getText().toString())){
                                UserEntity userEntity = new UserEntity();
                                userEntity.setEmail(next.child("email").getValue().toString());
                                userEntity.setPhone(next.child("phone").getValue().toString());
                                userEntity.setUid(next.getKey());
                                userEntity.setUsername(next.child("username").getValue().toString());
                                temp.add(userEntity);
                            }
                        }

                        if(temp.size() == 0){
                            Toast.makeText(MainActivity.this, "Search failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            //show list
                            adapter = new RvAdapter(temp,MainActivity.this);
                            mRecyclerView.setAdapter(adapter);
                            popupWindow2.showAtLocation(MainActivity.this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                            adapter.setOnItemClickListener(new RvAdapter.OnItemClickListener() {
                                @Override
                                public void onClick(int position) {
                                    initPop4(temp.get(position));
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initPop() {
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.pop_profile, null);


        popId = inflate.findViewById(R.id.pop_id);
        popEdit = inflate.findViewById(R.id.pop_edit);
        popEmail = inflate.findViewById(R.id.pop_email);
        popPhone = inflate.findViewById(R.id.pop_phone);
        popUsername = inflate.findViewById(R.id.pop_username);
        popId.setText("Uid : "+getIntent().getStringExtra("uid"));
        popEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (TextUtils.isEmpty(popEmail.getText().toString())){
                Toast.makeText(MainActivity.this, "email is empty.",
                        Toast.LENGTH_SHORT).show();
                return;
                }
            if (TextUtils.isEmpty(popPhone.getText().toString())){
                    Toast.makeText(MainActivity.this, "phone is empty.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            if (TextUtils.isEmpty(popUsername.getText().toString())){
                    Toast.makeText(MainActivity.this, "username is empty.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                reference.child("data").child(Objects.requireNonNull(getIntent().getStringExtra("uid"))).child("email").setValue(popEmail.getText().toString());
                reference.child("data").child(Objects.requireNonNull(getIntent().getStringExtra("uid"))).child("phone").setValue(popPhone.getText().toString());
                reference.child("data").child(Objects.requireNonNull(getIntent().getStringExtra("uid"))).child("username").setValue(popUsername.getText().toString());
                Toast.makeText(MainActivity.this, "edit success.",
                        Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        });

        reference.child("data").child(Objects.requireNonNull(getIntent().getStringExtra("uid"))).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                popEmail.setText(""+snapshot.child("email").getValue());
                popPhone.setText(""+snapshot.child("phone").getValue());
                popUsername.setText(""+snapshot.child("username").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        popupWindow = new PopupWindow(inflate, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        inflate.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        location = new int[2];
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        mainBt1.getLocationOnScreen(location);
    }

    @OnClick({R.id.main_bt1, R.id.main_bt2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_bt1:
                popupWindow.showAtLocation(MainActivity.this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                break;
            case R.id.main_bt2:
                popupWindow1.showAtLocation(MainActivity.this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                break;
        }
    }
}