package com.r0th.shopping;

import android.content.Intent;

import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.r0th.shopping.Model.Cart;
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
    private EditText nameEditText,phoneEditText,addressEditText,cityEditText;
    private Button confirmOrderBtn;
    private String totalAmount = "";
    private String totalQuantity = "";
    private TextView totbay;
    private TextView totunit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);
        totalAmount = getIntent().getStringExtra("Total Price");
        totalQuantity = getIntent().getStringExtra("Total Quantity");
        Toast.makeText(this, "Total Price = IDR. "+totalAmount,Toast.LENGTH_SHORT).show();
        confirmOrderBtn = (Button) findViewById(R.id.confirm_final_order_btn);
        totbay=findViewById(R.id.totalbayar);
        totunit=findViewById(R.id.totalunit);
        int i = Integer.parseInt(totalAmount);
        DecimalFormat decim = new DecimalFormat("#,###.##");
        totbay.setText("Rp. "+decim.format(i));
        totunit.setText(totalQuantity);
        //totbay.setText("Total Pembayaran = Rp."+totalAmount);
        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check();
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
