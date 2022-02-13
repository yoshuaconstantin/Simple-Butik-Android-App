package com.r0th.shopping;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

public class CartActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn,Bayar;
    private TextView txtTotalAmount, txtMsg1,txtchkt;
    TextView totalhargadisplay;
    private int overTotalPrice=0;
    private int overTotalQuantity=0;
    private View finalInvoiceViewToPrint;
    String testtt ="";
    String TAG="";
    RelativeLayout relativeLayout;
    Display mDisplay;
    String imagesUri;
    String path;
    Bitmap bitmap;
    String file_name = "Screenshot";
    File myPath;
    int totalHeight;
    int totalWidth;
    public static final int READ_PHONE = 110;
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
        txtchkt = findViewById(R.id.textcheckout);
        WindowManager wm = (WindowManager) getSystemService(this.WINDOW_SERVICE);
        mDisplay = wm.getDefaultDisplay();
        totalhargadisplay=findViewById(R.id.cartproducttotalprice);
        relativeLayout = findViewById(R.id.relparent);


        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //takeScreenShot();
                //txtTotalAmount.setText("Total Price = IDR."+String.valueOf(overTotalPrice));
                Intent intent = new Intent(CartActivity.this,ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price", String.valueOf(overTotalPrice));
                intent.putExtra("Total Quantity", String.valueOf(overTotalQuantity));
                startActivity(intent);
                finish();

            }
        });

    }
    public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth){

        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();

        if(bgDrawable != null){
            bgDrawable.draw(canvas);
        }else{
            canvas.drawColor(Color.WHITE);
        }

        view.draw(canvas);
        return returnedBitmap;
    }
    private void takeScreenShot(){

        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ScreenShot/");

        if(!folder.exists()){
            boolean success = folder.mkdir();
        }

        path = folder.getAbsolutePath();
        path = path + "/" + file_name + System.currentTimeMillis() + ".pdf";

        View u = findViewById(R.id.relparent);

        RelativeLayout z = findViewById(R.id.relparent);
        totalHeight = 2300;
        totalWidth = 1080;
        String extr = Environment.getExternalStorageDirectory() + "/Pictures/";
        File file = new File(extr);
        if(!file.exists())
            file.mkdir();
        String fileName = file_name + ".jpg";
        myPath = new File(extr, fileName);
        imagesUri = myPath.getPath();
        bitmap = getBitmapFromView(u, totalHeight, totalWidth);

        try{
            FileOutputStream fos = new FileOutputStream(myPath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    createPdf();


    }
    private void createPdf() {

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);

        Bitmap bitmap = Bitmap.createScaledBitmap(this.bitmap, this.bitmap.getWidth(), this.bitmap.getHeight(), true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);
        File filePath = new File(path);
        try{
            document.writeTo(new FileOutputStream(filePath));
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this, "Something Wrong: "+e.toString(), Toast.LENGTH_SHORT).show();
        }

        document.close();

        if (myPath.exists())
            myPath.delete();

        openPdf(path);

    }

    private void openPdf(String path) {
        File file = new File(path);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file), "image/*");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open FIle");
        try{
            startActivity(intent);
        }catch (ActivityNotFoundException e){
            Toast.makeText(this, "No Apps to read PDF FIle", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User view")
                                .child("Products"),Cart.class).build();
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
                    NextProcessBtn.setVisibility(View.VISIBLE);
                    txtchkt.setVisibility(View.VISIBLE);
                }else {
                    NextProcessBtn.setVisibility(View.GONE);
                    txtchkt.setVisibility(View.GONE);
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
