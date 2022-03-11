package com.r0th.shopping.returbarang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.type.DateTime;
import com.r0th.shopping.HomeActivity;
import com.r0th.shopping.Model.Products;
import com.r0th.shopping.ProductDetailsActivity;
import com.r0th.shopping.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ProductDetailReturWithState extends AppCompatActivity {
    private DatabaseReference ProductsRef;
    private Button addToCartButton;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice,productDescription,productName,producdiscount;
    private String productID="", state = "Normal";
    int stock = 0;
    String kategori = "";
    String State = "nul";
    String katconvert="";
    String nmbrng,qtybrng,hrgbrng,katbrng,statenew ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productID = getIntent().getStringExtra("pid");
        State = getIntent().getStringExtra("state");
        ///
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
        ////
        addToCartButton =(Button) findViewById(R.id.pd_add_to_cart_button);
        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        productImage = (ImageView) findViewById(R.id.product_image_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);
        producdiscount = findViewById(R.id.product_discount_details);
        getProductDetails(productID);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        ProductsRef.child(productID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kategori = (String) snapshot.child("category").getValue();
                if (kategori == "Hijab"){
                    katconvert = "hijab";
                }else if (kategori == "Alat Shalat"){
                    katconvert = "alatshalat";
                }else if (kategori == "Pakaian"){
                    katconvert = "pakaian";
                }else if (kategori == "Aksesoris"){
                    katconvert = "aksesoris";
                }
                switch (kategori){
                    case "Hijab":
                        katconvert = "hijab";
                        break;
                    case "Alat Shalat":
                        katconvert = "alatshalat";
                        break;
                    case "Pakaian":
                        katconvert = "pakaian";
                        break;
                    case "Aksesoris":
                        katconvert = "aksesoris";
                        break;
                    default:
                        katconvert = "Null";
                        break;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (State){
                    case "returbarang":
                        statebrng();
                        break;
                    case "returuang":
                        stateuang();
                        break;
                    case "returbarangend":
                        prosesakhirreturbarang();
                        break;
                    default:
                        Toast.makeText(ProductDetailReturWithState.this, "Tidak ada State..!", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void prosesakhirreturbarang(){
        String saveCurrentTime,saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentday = new SimpleDateFormat("dd");
        String hari = currentday.format(calForDate.getTime());
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd. yyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());
        SimpleDateFormat month1 = new SimpleDateFormat("MMM yyy");

        String bulan2 = month1.format(calForDate.getTime());
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Returbarang");
        final DatabaseReference minstock = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);
        int y = Integer.parseInt(productPrice.getText().toString());
        int g = Integer.parseInt(producdiscount.getText().toString());
        double h = g * 0.01;
        double x = y*h;
        int z = (int) (y - Double.valueOf(x));
        long mDateTime = System.currentTimeMillis();
        int hargaawal = Integer.parseInt(hrgbrng);
        int hargamargin = hargaawal - z;

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("price",String.valueOf(z));
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
//        cartMap.put("",);
//        cartMap.put("",);
//        cartMap.put("",);
//        cartMap.put("",);
        cartMap.put("barangmasuk",qtybrng);
        cartMap.put("barangkeluar",numberButton.getNumber());
        cartMap.put("discount","");
        cartMap.put("category",kategori);
    }

    private void getProductDetails(String productID) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Products products=dataSnapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    producdiscount.setText(products.getDiscount());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void statebrng(){
        String saveCurrentTime,saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentday = new SimpleDateFormat("dd");
        String hari = currentday.format(calForDate.getTime());
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd. yyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());
        SimpleDateFormat month1 = new SimpleDateFormat("MMM yyy");

        String bulan2 = month1.format(calForDate.getTime());
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Returbarang");
        final DatabaseReference minstock = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);
        int y = Integer.parseInt(productPrice.getText().toString());
        int g = Integer.parseInt(producdiscount.getText().toString());
        double h = g * 0.01;
        double x = y*h;
        int z = (int) (y - Double.valueOf(x));
        long mDateTime = System.currentTimeMillis();

        Intent intent =new Intent(ProductDetailReturWithState.this, ViewReturBarangAll.class);
        intent.putExtra("namabrng",productName.getText().toString());
        intent.putExtra("hargabrng",String.valueOf(z));
        intent.putExtra("quantitybrng",numberButton.getNumber());
        intent.putExtra("kategorimasuk",kategori);
        intent.putExtra("statenew","returbarangend");
        startActivity(intent);





    }
    public void stateuang(){
        updatestock();
        String saveCurrentTime,saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentday = new SimpleDateFormat("dd");
        String hari = currentday.format(calForDate.getTime());
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd. yyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());
        SimpleDateFormat month1 = new SimpleDateFormat("MMM yyy");

        String bulan2 = month1.format(calForDate.getTime());
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Returbarang");
        final DatabaseReference minstock = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);
        int y = Integer.parseInt(productPrice.getText().toString());
        int g = Integer.parseInt(producdiscount.getText().toString());
        double h = g * 0.01;
        double x = y*h;
        int z = (int) (y - Double.valueOf(x));
        long mDateTime = System.currentTimeMillis();
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("price",String.valueOf(z));
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");
        cartMap.put("category",kategori);

        cartListRef.child(bulan2).child(hari).child(String.valueOf(mDateTime)).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(ProductDetailReturWithState.this, StrukReturUang.class);
                intent.putExtra("id",String.valueOf(mDateTime));
                intent.putExtra("qty", numberButton.getNumber());
                intent.putExtra("price",String.valueOf(z));
                startActivity(intent);
            }



        });

    }
    private void updatestock(){
        //converter//

        //endofconvert//
        Date date = new Date();
        Calendar calForDate = Calendar.getInstance();
        ////////
        Calendar calForDate2 = Calendar.getInstance();
        calForDate2.setTime(date);
        calForDate2.add(Calendar.DATE,-1);
        ///////
        Date minus1 = calForDate2.getTime();


        SimpleDateFormat currentday = new SimpleDateFormat("dd");
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd. yyy");
        String tanggal = currentDate.format(calForDate.getTime());
        String dayminus = currentday.format(calForDate2.getTime());
        Toast.makeText(this, "day "+dayminus, Toast.LENGTH_SHORT).show();
        SimpleDateFormat month1 = new SimpleDateFormat("MMM yyy");
        String bulan2 = month1.format(calForDate.getTime());
        String hari = currentday.format(calForDate.getTime());
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("Pembukuan");
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Products");
        ref1.child(productID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String stock = (String) snapshot.child("stock").getValue();
                int x = Integer.parseInt(stock);
                int y = Integer.parseInt(numberButton.getNumber());
                int z = x + y;
                String hasil = String.valueOf(z);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Products").child(productID);
                ref.child("stock").setValue(hasil);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref2.child(bulan2).child(hari).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    //start total trx
                    String totaltrx = (String) snapshot.child("totaltransaksi").getValue();
                    int a = Integer.parseInt(totaltrx);
                    int n = Integer.parseInt(productPrice.getText().toString());
                    int g = Integer.parseInt(producdiscount.getText().toString());
                    double h = g * 0.01;
                    double m = n*h;
                    int b = (int) (n - Double.valueOf(m));
                    int totalhasil = a - b;
                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Pembukuan");
                    ref2.child(bulan2).child(hari).child("totaltransaksi").setValue(String.valueOf(totalhasil));
                    //end total trx

                    //start total unit
                    String totalunit = (String) snapshot.child("totalunit").getValue();
                    int unita = Integer.parseInt(totalunit);
                    int unitb = Integer.parseInt(numberButton.getNumber());
                    int unitc = unita - unitb;
                    DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("Pembukuan");
                    ref3.child(bulan2).child(hari).child("totalunit").setValue(String.valueOf(unitc));
                    //end total unit

                    //start update stock bedasarkan barang
                    String katestock = (String) snapshot.child(katconvert).getValue();
                    int x = Integer.parseInt(katestock);
                    int y = Integer.parseInt(numberButton.getNumber());
                    int z = x - y;
                    String hasil = String.valueOf(z);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Pembukuan");
                    ref.child(bulan2).child(hari).child(katconvert).setValue(hasil);
                    //end update stock
                }else{
                    ref2.child(bulan2).child(dayminus).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //start total trx
                        String totaltrx = (String) snapshot.child("totaltransaksi").getValue();
                        int a = Integer.parseInt(totaltrx);
                        int n = Integer.parseInt(productPrice.getText().toString());
                        int g = Integer.parseInt(producdiscount.getText().toString());
                        double h = g * 0.01;
                        double m = n*h;
                        int b = (int) (n - Double.valueOf(m));
                        int totalhasil = a - b;
                        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Pembukuan");
                        ref2.child(bulan2).child(dayminus).child("totaltransaksi").setValue(String.valueOf(totalhasil));
                        //end total trx

                        //start total unit
                        String totalunit = (String) snapshot.child("totalunit").getValue();
                        int unita = Integer.parseInt(totalunit);
                        int unitb = Integer.parseInt(numberButton.getNumber());
                        int unitc = unita - unitb;
                        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("Pembukuan");
                        ref3.child(bulan2).child(dayminus).child("totalunit").setValue(String.valueOf(unitc));
                        //end total unit

                        //start update stock bedasarkan barang
                        String katestock = (String) snapshot.child(katconvert).getValue();
                        int x = Integer.parseInt(katestock);
                        int y = Integer.parseInt(numberButton.getNumber());
                        int z = x - y;
                        String hasil = String.valueOf(z);
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Pembukuan");
                        ref.child(bulan2).child(dayminus).child(katconvert).setValue(hasil);
                        //end update stock
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}
