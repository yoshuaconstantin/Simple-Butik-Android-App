package com.r0th.shopping;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.r0th.shopping.Model.Products;
import com.r0th.shopping.ViewHolder.DataBarang_adminViewHolder;
import com.r0th.shopping.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import soup.neumorphism.NeumorphButton;
import soup.neumorphism.NeumorphCardView;

public class DataBarang_Admin extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView searchList;
    private String searchInput;
    private List<Products> items;
    private DatabaseReference reference,RefCart,referenceAdmin;
    private DatabaseReference reference2;
    private FirebaseDatabase database;
    int loop=1;
    FirebaseRecyclerOptions<Products> options;
    FirebaseRecyclerAdapter<Products, DataBarang_adminViewHolder> adapter2;
    EditText kolomsearch;
    Button search;
    NeumorphCardView cat1,cat2,cat3,cat4;
    String pencarian="";
    String pencarianlower="";
    String allsearch="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_kategori_lengkap);
        recyclerView = findViewById(R.id.relkategoriadm);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(gridLayoutManager);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Products");
        RefCart = database.getReference("Cart List");
        kolomsearch = findViewById(R.id.search_product_name);
        cat1 = findViewById(R.id.kategoripakaianadm);
        cat2 = findViewById(R.id.kategoriaksesorisadm);
        cat3 = findViewById(R.id.kategorihijabadm);
        cat4 = findViewById(R.id.kategorialsoladm);

        cat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pencarian = "Pakaian";
                kategorisearch();
            }
        });
        cat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pencarian = "Aksesoris";
                kategorisearch();
            }
        });
        cat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pencarian = "Hijab";
                kategorisearch();
            }
        });
        cat4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pencarian = "Alat Shalat";
                kategorisearch();
            }
        });
        search= findViewById(R.id.search_btn);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInput = kolomsearch.getText().toString();
                allsearch=searchInput;
                pencarian();
            }
        });
        setHasOptionsMenu(true);
        onStart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference, Products.class)
                .build();

        adapter2 = new FirebaseRecyclerAdapter<Products, DataBarang_adminViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DataBarang_adminViewHolder dataBarang_adminViewHolder, int i, @NonNull Products products) {
                DecimalFormat decim = new DecimalFormat("#,###.##");
                int y = Integer.parseInt(products.getPrice());
                dataBarang_adminViewHolder.rvnmbarang.setText(products.getPname());
                dataBarang_adminViewHolder.rvstockbrng.setText("Stock : "+products.getStock());
                dataBarang_adminViewHolder.rvhargabrng.setText("Rp."+decim.format(y));
                Picasso.get().load(products.getImage()).into(dataBarang_adminViewHolder.imageView);

            }




            @NonNull
            @Override
            public DataBarang_adminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemview = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_items_layout, parent, false);
                return new DataBarang_adminViewHolder(itemview);
            }
        };
        adapter2.startListening();
        recyclerView.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
        recyclerView.getViewTreeObserver();

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter2.startListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(DataBarang_Admin.this,AdminCategoryActivity.class);
        startActivity(intent);
        this.finish();
    }
    private void setHasOptionsMenu(boolean b) {
    }
    public void showtask(){
        options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("pname").equalTo(allsearch), Products.class)
                .build();

        adapter2 = new FirebaseRecyclerAdapter<Products, DataBarang_adminViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DataBarang_adminViewHolder dataBarang_adminViewHolder, int i, @NonNull Products products) {
                DecimalFormat decim = new DecimalFormat("#,###.##");
                int y = Integer.parseInt(products.getPrice());
//                int g = Integer.parseInt(products.getDiscount());
//                double h = g * 0.01;
//                double x = y*h;
//                int z = (int) (y - Double.valueOf(x));
                dataBarang_adminViewHolder.rvnmbarang.setText(products.getPname());
                dataBarang_adminViewHolder.rvstockbrng.setText("Stock : "+products.getStock());
                dataBarang_adminViewHolder.rvhargabrng.setText("Rp."+decim.format(y));
                Picasso.get().load(products.getImage()).into(dataBarang_adminViewHolder.imageView);
            }




            @NonNull
            @Override
            public DataBarang_adminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemview = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_items_layout, parent, false);
                return new DataBarang_adminViewHolder(itemview);
            }
        };
        adapter2.startListening();
        recyclerView.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
        recyclerView.getViewTreeObserver();

    }
    private void kategorisearch(){
        options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("category").equalTo(pencarian), Products.class)
                .build();

        adapter2 = new FirebaseRecyclerAdapter<Products, DataBarang_adminViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DataBarang_adminViewHolder dataBarang_adminViewHolder, int i, @NonNull Products products) {
                DecimalFormat decim = new DecimalFormat("#,###.##");
                int y = Integer.parseInt(products.getPrice());
//                int g = Integer.parseInt(products.getDiscount());
//                double h = g * 0.01;
//                double x = y*h;
//                int z = (int) (y - Double.valueOf(x));
                dataBarang_adminViewHolder.rvnmbarang.setText(products.getPname());
                dataBarang_adminViewHolder.rvstockbrng.setText("Stock : "+products.getStock());
                dataBarang_adminViewHolder.rvhargabrng.setText("Rp."+decim.format(y));
                Picasso.get().load(products.getImage()).into(dataBarang_adminViewHolder.imageView);
            }




            @NonNull
            @Override
            public DataBarang_adminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemview = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_items_layout, parent, false);
                return new DataBarang_adminViewHolder(itemview);
            }
        };
        adapter2.startListening();
        recyclerView.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
        recyclerView.getViewTreeObserver();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("Update")){
            showUpdateDialog(adapter2.getRef(item.getOrder()).getKey(), adapter2.getItem(item.getOrder()));


        } else if(item.getTitle().equals("Delete")){
            deleteTask(adapter2.getRef(item.getOrder()).getKey());
        }

        return super.onContextItemSelected(item);
    }
    private void deleteTask(String key) {
        reference.child(key).removeValue();
        RefCart.child("User view").child("Products").child(key).removeValue();
        RefCart.child("Admin view").child("Products").child(key).removeValue();
    }

    private void showUpdateDialog(final String key, Products item) {



        AlertDialog.Builder builder = new AlertDialog.Builder(DataBarang_Admin.this);
        builder.setTitle("Update");
        builder.setMessage("Silahkan Update Data");
        View updateLayout = LayoutInflater.from(DataBarang_Admin.this).inflate(R.layout.customeditbarang, null);


        final EditText namabarang = updateLayout.findViewById(R.id.editnamaproduk);
        final EditText editstock = updateLayout.findViewById(R.id.editprodukstok);
        final EditText editharga = updateLayout.findViewById(R.id.editprodukharga);
        //final EditText editdiskon = updateLayout.findViewById(R.id.editdiscount);


        namabarang.setText(item.getPname());
        editstock.setText(item.getStock());
        editharga.setText(item.getPrice());
        //editdiskon.setText(item.getDiscount());


        String pid = item.getPid();
        String desc = item.getDescription();
        String image = item.getImage();
        String kategori = item.getCategory();
        String date = item.getDate();
        String time  = item.getTime();
        String disc = item.getDiscount();
        String barc = item.getBarcode();
        String ogprice = item.getOgprice();

        builder.setView(updateLayout);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nm = namabarang.getText().toString();
                String stck = editstock.getText().toString();
                String hrg = editharga.getText().toString();
                String dsc = "0";


                Products daitem3 = new Products(nm,desc,hrg,image,kategori,pid,date,time,stck,barc,dsc,ogprice);
                reference.child(key).setValue(daitem3);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void pencarian(){
        options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("pname").startAt(allsearch), Products.class)
                .build();

        adapter2 = new FirebaseRecyclerAdapter<Products, DataBarang_adminViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DataBarang_adminViewHolder dataBarang_adminViewHolder, int i, @NonNull Products products) {
                DecimalFormat decim = new DecimalFormat("#,###.##");
                int y = Integer.parseInt(products.getPrice());
//                int g = Integer.parseInt(products.getDiscount());
//                double h = g * 0.01;
//                double x = y*h;
//                int z = (int) (y - Double.valueOf(x));
                dataBarang_adminViewHolder.rvnmbarang.setText(products.getPname());
                dataBarang_adminViewHolder.rvstockbrng.setText("Stock : "+products.getStock());
                dataBarang_adminViewHolder.rvhargabrng.setText("Rp."+decim.format(y));
                Picasso.get().load(products.getImage()).into(dataBarang_adminViewHolder.imageView);
            }




            @NonNull
            @Override
            public DataBarang_adminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemview = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_items_layout, parent, false);
                return new DataBarang_adminViewHolder(itemview);
            }
        };
        adapter2.startListening();
        recyclerView.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
        recyclerView.getViewTreeObserver();

    }

}
