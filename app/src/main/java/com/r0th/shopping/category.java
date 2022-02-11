package com.r0th.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.r0th.shopping.Model.Products;
import com.r0th.shopping.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import soup.neumorphism.NeumorphCardView;

public class category extends AppCompatActivity {
    private DatabaseReference ProductsRef;
    DrawerLayout drawerLayout;
    NeumorphCardView cat1,cat2,cat3,cat4;
    TextView textView;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String kategori = "Hijab";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_kategori_lengkap_user);
        cat1= findViewById(R.id.kategoripakaian);
        cat2= findViewById(R.id.kategoriaksesoris);
        cat3= findViewById(R.id.kategorihijab);
        cat4= findViewById(R.id.kategorialsol);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = findViewById(R.id.relkategori);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        cat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kategori ="Pakaian";
               onStart();
            }
        });
        cat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kategori ="Aksesoris";
                onStart();
            }
        });
        cat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kategori ="Hijab";
                onStart();
            }
        });
        cat4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kategori ="Alat Shalat";
                onStart();
            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef.orderByChild("category").equalTo(kategori), Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
                    {
                        int i = Integer.parseInt(model.getPrice());
                        DecimalFormat decim = new DecimalFormat("#,###.##");
                        holder.txtProductName.setText(model.getPname());
                        holder.stock.setText("Stock : "+model.getStock());
                        holder.txtProductPrice.setText(decim.format(i)+ " IDR.");
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent =new Intent(category.this,ProductDetailsActivity.class);
                                intent.putExtra("pid",model.getPid());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
