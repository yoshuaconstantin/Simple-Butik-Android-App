package com.r0th.shopping;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.r0th.shopping.Prevalent.CartViewHolder;
import com.r0th.shopping.Prevalent.StrukViewHolder;

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
    TextView totalqty,totalhrg,totalharga2,tanggalprint;
    Display mDisplay;
    String imagesUri;
    String path;
    Bitmap bitmap;
    String file_name = "Screenshot";
    File myPath;
    int totalHeight;
    ImageView print;
    int totalWidth;
    String totamount,totalquantity,saveCurrentDate, saveCurrentTime;
    String time = "";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.strukpembayaran);
        tanggalprint = findViewById(R.id.tanggalstruk);
        totalqty = findViewById(R.id.txttotalunitstruk);
        totalhrg = findViewById(R.id.txttotalharga);
        totalharga2 = findViewById(R.id.totalharga2);
        time = String.valueOf(System.currentTimeMillis());
        totamount = getIntent().getStringExtra("Total Price");
        totalquantity = getIntent().getStringExtra("Total Quantity");
        int c = Integer.parseInt(totamount);
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
        totalharga2.setText("Rp. "+decim.format(c));
        totalhrg.setText("Rp. "+decim.format(c));
        recyclerView = findViewById(R.id.recstruk);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
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
        totalHeight = 2300;
        totalWidth = 1080;
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