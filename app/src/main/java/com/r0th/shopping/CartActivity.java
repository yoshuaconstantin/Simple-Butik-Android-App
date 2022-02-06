package com.r0th.shopping;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.Query;
import com.google.firebase.database.core.Context;
import com.r0th.shopping.Model.Cart;
import com.r0th.shopping.Prevalent.CartViewHolder;
import com.r0th.shopping.Prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class CartActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn,Bayar;
    private TextView txtTotalAmount, txtMsg1;
    TextView totalhargadisplay;
    private int overTotalPrice=0;
    private int overTotalQuantity=0;
    String testtt ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        NextProcessBtn = (Button)findViewById(R.id.next_btn);
        txtTotalAmount = (TextView)findViewById(R.id.total_price);
        txtMsg1 = (TextView)findViewById(R.id.msg1);
        totalhargadisplay=findViewById(R.id.cartproducttotalprice);
        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CartActivity.this, " " +testtt, Toast.LENGTH_SHORT).show();
                //txtTotalAmount.setText("Total Price = IDR."+String.valueOf(overTotalPrice));
                Intent intent = new Intent(CartActivity.this,ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price", String.valueOf(overTotalPrice));
                intent.putExtra("Total Quantity", String.valueOf(overTotalQuantity));
                startActivity(intent);
                finish();

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        Bayar =findViewById(R.id.next_btn);
        CheckOrderState();
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User view")
                                .child(Prevalent.currentOnlineUser.getPhone()).child("Products"),Cart.class).build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {

                int i = Integer.parseInt(model.getPrice());
                DecimalFormat decim = new DecimalFormat("#,###.##");
                holder.txtProductQuantity.setText(model.getQuantity());
                holder.txtProductPrice.setText(decim.format(i)+" IDR.");
                holder.txtProductName.setText(model.getPname());

                int oneTyprProductTPrice = ((Integer.valueOf(model.getPrice())))* Integer.valueOf(model.getQuantity());
                int overTotQ = ((Integer.valueOf(model.getQuantity())))* 1;
                holder.displaytotalhrg.setText(decim.format(oneTyprProductTPrice)+" IDR.");
               overTotalQuantity = overTotalQuantity + overTotQ;
                overTotalPrice = overTotalPrice + oneTyprProductTPrice;
                //totalhargadisplay.setText(oneTyprProductTPrice+"IDR.");

                DatabaseReference check = FirebaseDatabase.getInstance().getReference("Cart List").
                        child("User view").
                        child(Prevalent.currentOnlineUser.getPhone()).
                        child("Products").
                        child(model.getPid()).
                        child("quantity");
                check.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String e = snapshot.getValue(String.class);

                        testtt = e;
                        //Toast.makeText(CartActivity.this, "Test "+e, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Edit",
                                        "Hapus"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Opsional");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i==0){
                                    Intent intent = new Intent(CartActivity.this,ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                if (i==1){
                                    cartListRef.child("User view")
                                            .child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(CartActivity.this,"Item removed Successfully.",Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(CartActivity.this,HomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                    cartListRef.child("Admin view")
                                            .child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }

        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        cartListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //Toast.makeText(CartActivity.this,"1",Toast.LENGTH_SHORT).show();
                    Bayar.setVisibility(View.VISIBLE);
                }else {
                    Bayar.setVisibility(View.GONE);
                }//Toast.makeText(CartActivity.this,"2",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                    int sum = 0;
//                    sum += postSnapshot.child("quantity").getValue(Integer.class);
//                    Toast.makeText(CartActivity.this,"test "+sum,Toast.LENGTH_SHORT).show();
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }
    private void CheckOrderState()
    {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();
                    if (shippingState.equals("Shipped")){
                        txtTotalAmount.setText("TDear "+userName+"\n order is shipped successfully.");
                        recyclerView.setVisibility(View.GONE);
                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("Congratulations, Your Final order has been shipped successfully. Soon you will received your order at your door step.");
                        NextProcessBtn.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this,"You can purchase more products, Once you received your first order",Toast.LENGTH_SHORT).show();
                    }
                    else if (shippingState.equals("Not Shipped")){
                        txtTotalAmount.setText("Shipping State = Not Shipped");
                        recyclerView.setVisibility(View.GONE);
                        txtMsg1.setVisibility(View.VISIBLE);

                        NextProcessBtn.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this,"You can purchase more products, Once you received your first order",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
