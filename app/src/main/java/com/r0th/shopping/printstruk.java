package com.r0th.shopping;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowMetrics;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.r0th.shopping.Model.Cart;
import com.r0th.shopping.Model.Products;
import com.r0th.shopping.Prevalent.CartViewHolder;
import com.r0th.shopping.Prevalent.StrukViewHolder;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class printstruk extends AppCompatActivity {
    private int overTotalPrice=0;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private int overTotalQuantity=0;
    TextView totalqty,totalhrg,totalharga2,tanggalprint,totaldisc,disc,displaydisc,oldprice,displyhargaawal,linestruk,via;
    Display mDisplay;
    String imagesUri;
    String path;
    Bitmap bitmap;
    String file_name = "Screenshot";
    File myPath;
    int totalHeight=0;
    ImageView print;
    int totalWidth=0;
    String totamount,totalquantity,saveCurrentDate, saveCurrentTime,discprice,discount,hargaawal,pemvia;
    String time = "";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.strukpembayaran);
        tanggalprint = findViewById(R.id.tanggalstruk);
        totalqty = findViewById(R.id.txttotalunitstruk);
        totalhrg = findViewById(R.id.txttotalharga);
        totalharga2 = findViewById(R.id.totalharga2);
        totaldisc= findViewById(R.id.totaldisc);
        displaydisc = findViewById(R.id.displaydisc);
        disc=findViewById(R.id.discountext);
        displyhargaawal = findViewById(R.id.discplayhargaawal);
        time = String.valueOf(System.currentTimeMillis());
        totamount = getIntent().getStringExtra("Total Price");
        totalquantity = getIntent().getStringExtra("Total Quantity");
        discprice = getIntent().getStringExtra("Discount Price");
        discount = getIntent().getStringExtra("Discount");
        hargaawal= getIntent().getStringExtra("Total Awal");
        pemvia= getIntent().getStringExtra("Via");
        oldprice=findViewById(R.id.hargaawal);
        linestruk=findViewById(R.id.linestruk2);
        via=findViewById(R.id.VIA);
        int c = Integer.parseInt(totamount);
        int d = Integer.parseInt(hargaawal);
        DecimalFormat decim = new DecimalFormat("#,###.##");
        Calendar calendar = Calendar.getInstance();
        print = findViewById(R.id.image1);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenShot();
                onBackPressed();
            }
        });
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());
        tanggalprint.setText(saveCurrentDate +"  - "+saveCurrentTime);
        totalqty.setText(totalquantity);
        totaldisc.setText("Rp. "+decim.format(Integer.valueOf(discprice)));
        disc.setText(discount+"%");
        totalharga2.setText("Rp. "+decim.format(c));
        totalhrg.setText("Rp. "+decim.format(c));
        oldprice.setText("Rp. "+decim.format(d));
        via.setText("Pembayaran Melalui : "+pemvia);
        recyclerView = findViewById(R.id.recstruk);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        int width= this.getResources().getDisplayMetrics().widthPixels;
        int height= this.getResources().getDisplayMetrics().heightPixels;
        totalWidth = width;
        totalHeight = height;
        if (Integer.parseInt(discount) == 0){
            disc.setVisibility(View.INVISIBLE);
            displaydisc.setVisibility(View.INVISIBLE);
            displyhargaawal.setVisibility(View.INVISIBLE);
            oldprice.setVisibility(View.INVISIBLE);
            totaldisc.setVisibility(View.INVISIBLE);
            linestruk.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User view")
                                .child("Products"), Cart.class).build();
        FirebaseRecyclerAdapter<Cart, StrukViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, StrukViewHolder>(options) {
            @NonNull
            @Override
            public StrukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout_struk, parent, false);
                StrukViewHolder holder = new StrukViewHolder(view);
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull StrukViewHolder strukViewHolder, int i, @NonNull Cart cart) {
                int a = Integer.parseInt(cart.getPrice());
                DecimalFormat decim = new DecimalFormat("#,###.##");
                strukViewHolder.namabrng.setText(cart.getPname());
                strukViewHolder.qtybrng.setText(cart.getQuantity());
                strukViewHolder.hargasatuan.setText("Rp. "+decim.format(a));

                //totalhargadisplay.setText(oneTyprProductTPrice+"IDR.");
            }


        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
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

        View u = findViewById(R.id.relparentstruk);

        RelativeLayout z = findViewById(R.id.relparentstruk);
        totalHeight = totalHeight;
        totalWidth = totalWidth;
        String extr = Environment.getExternalStorageDirectory() + "/Pictures/";
        File file = new File(extr);
        if(!file.exists())
            file.mkdir();
        String fileName = file_name +time+ ".jpg";
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


        //membuka file yang sudah jadi dari gambar di atas
        //Step, buat GenericFileProdiver, masukin profider ke manifest, buat XML provider_paths.xml
        File file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Pictures/",fileName);
        String extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file1).toString());
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK);
        Uri uri = FileProvider.getUriForFile(this, printstruk.this.getApplicationContext().getPackageName() + ".provider", file1);
        intent.setDataAndType(uri, mimeType);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "choseFile"));
    }


}