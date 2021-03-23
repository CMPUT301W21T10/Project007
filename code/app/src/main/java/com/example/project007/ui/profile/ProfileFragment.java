package com.example.project007.ui.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.project007.AfterLoginActivity;
import com.example.project007.DatabaseController;
import com.example.project007.ModifyExperimentFragment;
import com.example.project007.R;
import com.example.project007.RvAdapter;
import com.example.project007.UserEntity;
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

public class ProfileFragment extends Fragment {
    Integer userId;

    TextView mainId;

    TextView mainBt1;
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
    private ProfileViewModel profileViewModel;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(requireActivity());
        mainId = root.findViewById(R.id.main_id);
        mainId.setText("uid:"+ DatabaseController.getUserId());
        mainBt1 = root.findViewById(R.id.main_bt1);
        mainBt2 = root.findViewById(R.id.main_bt2);

        reference = FirebaseDatabase.getInstance().getReference();
        initPop();
        initPop3();
        initPop2();
        mainBt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.showAtLocation(requireActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
            }
        });
        mainBt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow1.showAtLocation(requireActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
            }
        });
        return root;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("SetTextI18n")
    private void initPop4(UserEntity userEntity) {
        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        popupWindow3.showAtLocation(requireActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    private void initPop3() {
        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    Toast.makeText(requireActivity(), "username is empty.",
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
                            Toast.makeText(requireActivity(), "Search failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            //show list
                            adapter = new RvAdapter(temp, requireActivity());
                            mRecyclerView.setAdapter(adapter);
                            popupWindow2.showAtLocation(requireActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
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
        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.pop_profile, null);


        popId = inflate.findViewById(R.id.pop_id);
        popEdit = inflate.findViewById(R.id.pop_edit);
        popEmail = inflate.findViewById(R.id.pop_email);
        popPhone = inflate.findViewById(R.id.pop_phone);
        popUsername = inflate.findViewById(R.id.pop_username);
        popId.setText("Uid : "+DatabaseController.getUserId());
        popEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(popEmail.getText().toString())){
                    Toast.makeText(requireActivity(), "email is empty.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(popPhone.getText().toString())){
                    Toast.makeText(requireActivity(), "phone is empty.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(popUsername.getText().toString())){
                    Toast.makeText(requireActivity(), "username is empty.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                reference.child("data").child(Objects.requireNonNull(DatabaseController.getUserId())).child("email").setValue(popEmail.getText().toString());
                reference.child("data").child(Objects.requireNonNull(DatabaseController.getUserId())).child("phone").setValue(popPhone.getText().toString());
                reference.child("data").child(Objects.requireNonNull(DatabaseController.getUserId())).child("username").setValue(popUsername.getText().toString());

                Toast.makeText(requireActivity(), "edit success.",
                        Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        });


        reference.child("data").child(Objects.requireNonNull((DatabaseController.getUserId()))).addValueEventListener(new ValueEventListener() {
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


}