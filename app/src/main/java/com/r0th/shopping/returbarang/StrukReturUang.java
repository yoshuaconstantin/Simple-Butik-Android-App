package com.r0th.shopping.returbarang;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.r0th.shopping.Model.Cart;
import com.r0th.shopping.Prevalent.StrukViewHolder;
import com.r0th.shopping.R;
import com.r0th.shopping.printstruk;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StrukReturUang extends AppCompatActivity {
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
    int totalHeight=0;
    ImageView print;
    int totalWidth=0;
    String id="";
    String totamount,totalquantity,saveCurrentDate, saveCurrentTime;
    String time = "";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retur_strukuang);
        tanggalprint = findViewById(R.id.returuang_tanggalstruk);
        totalqty = findViewById(R.id.returuang_txttotalunitstruk);
        totalhrg = findViewById(R.id.returuang_txttotalharga);
        totalharga2 = findViewById(R.id.returuang_totalharga2);
        time = String.valueOf(System.currentTimeMillis());
        id = getIntent().getStringExtra("id");
        totamount = getIntent().getStringExtra("price");
        totalquantity = getIntent().getStringExtra("qty");
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
        recyclerView = findViewById(R.id.retur_recstruk);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        int width= this.getResources().getDisplayMetrics().widthPixels;
        int height= this.getResources().getDisplayMetrics().heightPixels;
        totalWidth = width;
        totalHeight = height;
    }

    @Override
    protected void onStart() {
        super.onStart();
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

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference("Returbarang");
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child(bulan2)
                                .child(hari).child(id), Cart.class).build();
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
        Uri uri = FileProvider.getUriForFile(this, StrukReturUang.this.getApplicationContext().getPackageName() + ".provider", file1);
        intent.setDataAndType(uri, mimeType);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "choseFile"));
    }

}
