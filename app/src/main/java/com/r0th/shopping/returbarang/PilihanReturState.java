package com.r0th.shopping.returbarang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.r0th.shopping.ProductDetailsActivity;
import com.r0th.shopping.R;
import com.r0th.shopping.SearchProductsActivity;

import soup.neumorphism.NeumorphCardView;

public class PilihanReturState extends AppCompatActivity {
    String State="null";
    NeumorphCardView statebrng,stateuang;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retur_state);
        statebrng=findViewById(R.id.statebarang);
        stateuang=findViewById(R.id.stateuang);

        statebrng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                State="returbarang";
                Intent intent =new Intent(PilihanReturState.this, ViewReturBarangAll.class);
                intent.putExtra("state",State);
                startActivity(intent);
            }
        });
        stateuang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                State="returuang";
                Intent intent =new Intent(PilihanReturState.this, ViewReturBarangAll.class);
                intent.putExtra("state",State);
                startActivity(intent);
            }
        });


    }
}
