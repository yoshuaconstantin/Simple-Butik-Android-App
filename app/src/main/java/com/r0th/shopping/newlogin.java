package com.r0th.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class newlogin extends AppCompatActivity {
    DatabaseReference referenceadm,referenceusr;
    EditText edtadm,edtusr;
    ImageView imgadm,imgusr;
    String passwordadm,passwordusr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_login);

        edtadm = findViewById(R.id.edtpassadmin);
        edtusr = findViewById(R.id.edtpassuser);
        imgadm = findViewById(R.id.imgadm);
        imgusr= findViewById(R.id.imgusr);
        referenceadm = FirebaseDatabase.getInstance().getReference("Admins");
        referenceusr = FirebaseDatabase.getInstance().getReference("Users");
        passadm();
        passusr();
        //
        edtadm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edtadm.getText().toString().equals(passwordadm)){
                    Intent intent = new Intent(newlogin.this,AdminCategoryActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtusr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edtusr.getText().toString().equals(passwordusr)){
                    Intent intent = new Intent(newlogin.this,HomeActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //
        imgadm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgadm.setVisibility(View.INVISIBLE);
                imgusr.setVisibility(View.VISIBLE);
                edtusr.setText("");

            }
        });
        imgusr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgadm.setVisibility(View.VISIBLE);
                imgusr.setVisibility(View.INVISIBLE);
                edtadm.setText("");
            }
        });

    }

    private void passadm() {
        referenceadm.child("0899").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                passwordadm = snapshot.child("password").getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }
    private void passusr(){
        referenceusr.child("0877").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                passwordusr = snapshot.child("password").getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }


}
