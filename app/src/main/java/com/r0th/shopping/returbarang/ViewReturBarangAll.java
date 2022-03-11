package com.r0th.shopping.returbarang;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.r0th.shopping.Model.Products;
import com.r0th.shopping.ProductDetailsActivity;
import com.r0th.shopping.R;
import com.r0th.shopping.ViewHolder.ProductViewHolder;
import com.r0th.shopping.category;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import soup.neumorphism.NeumorphCardView;

public class ViewReturBarangAll extends AppCompatActivity {
    private DatabaseReference ProductsRef;
    DrawerLayout drawerLayout;
    NeumorphCardView cat1,cat2,cat3,cat4;
    TextView textView;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String kategori = "Hijab";
    String state ="null";
    String nmbrng,qtybrng,hrgbrng,katbrng,statenew ="";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retur_layout_all);
        state = getIntent().getStringExtra("state");
        statenew = getIntent().getStringExtra("statenew");
        switch (statenew){
            case "returbarangend":
                nmbrng = getIntent().getStringExtra("namabrng");
                qtybrng = getIntent().getStringExtra("hargabrng");
                hrgbrng = getIntent().getStringExtra("quantitybrng");
                katbrng = getIntent().getStringExtra("kategorimasuk");
                break;
            default:

                break;
        }
        cat1= findViewById(R.id.retur_kategoripakaianadm);
        cat2= findViewById(R.id.retur_kategoriaksesorisadm);
        cat3= findViewById(R.id.retur_kategorihijabadm);
        cat4= findViewById(R.id.retur_kategorialsoladm);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView = findViewById(R.id.retur_rel);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(gridLayoutManager);
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

                                Intent intent =new Intent(ViewReturBarangAll.this, ProductDetailReturWithState.class);
                                intent.putExtra("pid",model.getPid());
                                intent.putExtra("namabrng",nmbrng);
                                intent.putExtra("hargabrng",qtybrng);
                                intent.putExtra("quantitybrng",hrgbrng);
                                intent.putExtra("kategorimasuk",katbrng);
                                intent.putExtra("statenew",statenew);
                                intent.putExtra("state",state);
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
