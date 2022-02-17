package com.r0th.shopping;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.r0th.shopping.Model.Cart;
import com.r0th.shopping.Model.pembukuan_data_item;
import com.r0th.shopping.Prevalent.CartViewHolder;
import com.r0th.shopping.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private DatabaseReference ProductsRef,ProductsRef2,myref,referencenew;
    private EditText nameEditText,phoneEditText,addressEditText,cityEditText;
    private Button confirmOrderBtn;
    private String totalAmount = "";
    private String totalQuantity = "";
    String pakaian = "";
    String hijab = "";
    String alsolat = "";
    String aksesoris = "";
    //String untuk cek data cart
    String pakaiancart = "";
    String hijabcart = "";
    String alsolatcart="";
    String aksesoriscart="";
    String totaltransaksicart="";
    String totalunitcart="";


    private TextView totbay,printstruk;
    private TextView totunit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);
        totalAmount = getIntent().getStringExtra("Total Price");
        totalQuantity = getIntent().getStringExtra("Total Quantity");
        //Toast.makeText(this, "Total Price = IDR. "+totalAmount,Toast.LENGTH_SHORT).show();
        confirmOrderBtn = (Button) findViewById(R.id.confirm_final_order_btn);
        totbay=findViewById(R.id.totalbayar);
        totunit=findViewById(R.id.totalunit);
        int i = Integer.parseInt(totalAmount);
        printstruk = findViewById(R.id.printstruk);
        printstruk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfirmFinalOrderActivity.this, printstruk.class);
                intent.putExtra("Total Price", String.valueOf(totalAmount));
                intent.putExtra("Total Quantity", String.valueOf(totalQuantity));
                startActivity(intent);

            }
        });

        DecimalFormat decim = new DecimalFormat("#,###.##");
        totbay.setText("Rp. "+decim.format(i));
        totunit.setText(totalQuantity);
        //totbay.setText("Total Pembayaran = Rp."+totalAmount);
        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checkdatabutu();
                checkdatabutu();


            }
        });
    }

    private void testpengurangan(){

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("Cart List");
        DatabaseReference productsRef2= FirebaseDatabase.getInstance().getReference("Products");
        productsRef.child("User view").child("Products").orderByChild("pname").addListenerForSingleValueEvent(new ValueEventListener() {
            String nm="";
            String qty ="";
            String nm2="";
            String qty2="";
            String stockupdate="";
            String PID="";
            int hasil=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    qty = dataSnapshot.child("quantity").getValue().toString();
                    nm = dataSnapshot.child("pname").getValue().toString();
                    productsRef2.orderByChild("pname").equalTo(nm).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                    qty2 = dataSnapshot1.child("stock").getValue().toString();
                                    hasil = Integer.parseInt(qty2) - Integer.parseInt(qty);
                                    PID=dataSnapshot1.child("pid").getValue().toString();
                                    stockupdate = String.valueOf(hasil);
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Products").child(PID);
                                    ref.child("stock").setValue(stockupdate);
                                }

                            }else{
                                Toast.makeText(ConfirmFinalOrderActivity.this, "not exist", Toast.LENGTH_SHORT).show();
                            }



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


    private void checkdatabutu(){
        testpengurangan();
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Pembukuan");
        ProductsRef2 = FirebaseDatabase.getInstance().getReference().child("Cart List");
        ProductsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    Calendar calForDate = Calendar.getInstance();
                    SimpleDateFormat currentday = new SimpleDateFormat("dd");
                    SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd. yyy");
                    String tanggal = currentDate.format(calForDate.getTime());
                    SimpleDateFormat month1 = new SimpleDateFormat("MMM yyy");
                    String bulan2 = month1.format(calForDate.getTime());
                    String hari = currentday.format(calForDate.getTime());
                    ProductsRef.child(bulan2).child(hari).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {


                                ambildatacart();
                                Handler handler = new Handler();
                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        int htnghijab, htngaksesoris, htngalatshlt, htngpakaian, htngtotal, htngtotalunit = 0;
                                        String hijab1 = snapshot.child("hijab").getValue(String.class);
                                        String aksesoris1 = snapshot.child("aksesoris").getValue(String.class);
                                        String alatshalat1 = snapshot.child("alatshalat").getValue(String.class);
                                        String pakaian1 = snapshot.child("pakaian").getValue(String.class);
                                        String totaltransaksi = snapshot.child("totaltransaksi").getValue(String.class);
                                        String totalunit = snapshot.child("totalunit").getValue(String.class);
                                        //hitung
                                        htnghijab = Integer.parseInt(hijab1) + Integer.parseInt(hijabcart);
                                        htngaksesoris = Integer.parseInt(aksesoris1) + Integer.parseInt(aksesoriscart);
                                        htngalatshlt = Integer.parseInt(alatshalat1) + Integer.parseInt(alsolatcart);
                                        htngpakaian = Integer.parseInt(pakaian1) + Integer.parseInt(pakaiancart);
                                        htngtotal = Integer.parseInt(totaltransaksi) + Integer.parseInt(totalAmount);
                                        htngtotalunit = Integer.parseInt(totalunit) + Integer.parseInt(totalQuantity);

                                        String finalhijab = String.valueOf(htnghijab);
                                        String finalaksesoris = String.valueOf(htngaksesoris);
                                        String finalalatsolat = String.valueOf(htngalatshlt);
                                        String finalpakaian = String.valueOf(htngpakaian);
                                        String finaltotal = String.valueOf(htngtotal);
                                        String finaltotalunit = String.valueOf(htngtotalunit);

                                        //masuk ke database
//                                    final HashMap<String, Object> cartMap = new HashMap<>();
//                                    cartMap.put("aksesoris", String.valueOf(htnghijab));
//                                    cartMap.put("alatshalat", String.valueOf(htngaksesoris));
//                                    cartMap.put("hijab", String.valueOf(htngalatshlt));
//                                    cartMap.put("pakaian", String.valueOf(htngpakaian));
//                                    cartMap.put("tanggal", tanggal);
//                                    cartMap.put("totaltransaksi", String.valueOf(htngtotal));
//                                    cartMap.put("totalunit", String.valueOf(htngtotalunit));
                                        referencenew = FirebaseDatabase.getInstance().getReference().child("Pembukuan");
                                        pembukuan_data_item data_item1 = new pembukuan_data_item(tanggal, finaltotal, finaltotalunit, finalpakaian, finalalatsolat, finalhijab, finalaksesoris);
                                        referencenew.child(bulan2).child(hari).setValue(data_item1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    ProductsRef2.removeValue();
                                                    Toast.makeText(ConfirmFinalOrderActivity.this, "Selesai..!", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                    Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                                    startActivity(intent);

                                                }

                                            }
                                        });


                                    }
                                };
                                handler.postDelayed(runnable, 1000);

                            }else{
                                tambahdata();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else {

                    tambahdata();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void tambahdata(){
        myref = FirebaseDatabase.getInstance().getReference("Pembukuan");
        ProductsRef = FirebaseDatabase.getInstance().getReference("Cart List").child("User view").child("Products");
        ProductsRef.orderByChild("category").equalTo("Pakaian").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sum = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){


                    sum += Integer.parseInt ( String.valueOf ( dataSnapshot.child ( "quantity" ).getValue () ) );


                }
                pakaian = String.valueOf(sum);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ProductsRef.orderByChild("category").equalTo("Alat Shalat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sumcat1 = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){


                    sumcat1 += Integer.parseInt ( String.valueOf ( dataSnapshot.child ( "quantity" ).getValue () ) );


                }
                alsolat = String.valueOf(sumcat1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ProductsRef.orderByChild("category").equalTo("hijab").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sumcat2 = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){


                    sumcat2 += Integer.parseInt ( String.valueOf ( dataSnapshot.child ( "quantity" ).getValue () ) );


                }
                hijab = String.valueOf(sumcat2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ProductsRef.orderByChild("category").equalTo("Aksesoris").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sumcat3 = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){


                    sumcat3 += Integer.parseInt ( String.valueOf ( dataSnapshot.child ( "quantity" ).getValue () ) );


                }
                aksesoris = String.valueOf(sumcat3);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Handler handler= new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                totalAmount = getIntent().getStringExtra("Total Price");
                totalQuantity = getIntent().getStringExtra("Total Quantity");
                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd. yyy");
                SimpleDateFormat currentDate1 = new SimpleDateFormat("dd");
                SimpleDateFormat month = new SimpleDateFormat("MMM yyy");
                String bulan = month.format(calForDate.getTime());
                String hari = currentDate1.format(calForDate.getTime());
                String tanggal = currentDate.format(calForDate.getTime());

                pembukuan_data_item data_item1 = new pembukuan_data_item(tanggal,totalAmount,totalQuantity,pakaian,alsolat,hijab,aksesoris);
                myref.child(bulan).child(hari).setValue(data_item1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(ConfirmFinalOrderActivity.this, "Data sudah di simpan..!", Toast.LENGTH_SHORT).show();
                        ProductsRef2 = FirebaseDatabase.getInstance().getReference().child("Cart List");

                        Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                        ProductsRef2.removeValue();
                        startActivity(intent);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        };
        handler.postDelayed(runnable, 1000); //start after 5 seconds



    }
    private void ambildatacart(){
        ProductsRef = FirebaseDatabase.getInstance().getReference("Cart List").child("User view").child("Products");
        ProductsRef.orderByChild("category").equalTo("Pakaian").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sum = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){


                    sum += Integer.parseInt ( String.valueOf ( dataSnapshot.child ( "quantity" ).getValue () ) );


                }
                pakaiancart = String.valueOf(sum);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ProductsRef.orderByChild("category").equalTo("Alat Shalat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sumcat1 = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){


                    sumcat1 += Integer.parseInt ( String.valueOf ( dataSnapshot.child ( "quantity" ).getValue () ) );


                }
                alsolatcart = String.valueOf(sumcat1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ProductsRef.orderByChild("category").equalTo("Hijab").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sumcat2 = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){


                    sumcat2 += Integer.parseInt ( String.valueOf ( dataSnapshot.child ( "quantity" ).getValue () ) );


                }
                hijabcart = String.valueOf(sumcat2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ProductsRef.orderByChild("category").equalTo("Aksesoris").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sumcat3 = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){


                    sumcat3 += Integer.parseInt ( String.valueOf ( dataSnapshot.child ( "quantity" ).getValue () ) );


                }
                aksesoriscart = String.valueOf(sumcat3);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void Check() {


//        DatabaseReference check = FirebaseDatabase.getInstance().getReference("Cart List").
//                child("User view").
//                child(Prevalent.currentOnlineUser.getPhone()).
//                child("Products");
//        check.addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot uniqueKeySnapshot : snapshot.getChildren()) {
//                    //Loop 1 to go through all the child nodes of users
//                    //loop 2 to go through all the child nodes of books node
//
//                    String quantity = uniqueKeySnapshot.child("quantity").getValue(String.class);
//                    Toast.makeText(ConfirmFinalOrderActivity.this, "Test " + quantity, Toast.LENGTH_SHORT).show();
//                }
//                Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
//                startActivity(intent);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        DatabaseReference check1 = FirebaseDatabase.getInstance().getReference("Products");
        check1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot uniqueKeySnapshot : snapshot.getChildren()) {
                    //Loop 1 to go through all the child nodes of users
                    //loop 2 to go through all the child nodes of books node

                    String quantity = uniqueKeySnapshot.child("stock").getValue(String.class);
                    Toast.makeText(ConfirmFinalOrderActivity.this, "Test " + quantity, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
        private void pembukuan(){

        }
    private void ConfirmOrder() {
        final String saveCurrentTime,saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd. yyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());
        final DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());
        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount",totalAmount);
        ordersMap.put("name",nameEditText.getText().toString());
        ordersMap.put("phone",phoneEditText.getText().toString());
        ordersMap.put("address",addressEditText.getText().toString());
        ordersMap.put("city",cityEditText.getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);
        ordersMap.put("state", "Not Shipped");
        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User view")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(ConfirmFinalOrderActivity.this,"Your final Order has been placed successfully.",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ConfirmFinalOrderActivity.this,HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });


    }
}
