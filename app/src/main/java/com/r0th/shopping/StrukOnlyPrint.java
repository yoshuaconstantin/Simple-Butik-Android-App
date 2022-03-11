package com.r0th.shopping;

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

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StrukOnlyPrint extends AppCompatActivity {
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
        setContentView(R.layout.strukreturnnew);
        tanggalprint = findViewById(R.id.tanggalstruk);
        totalqty = findViewById(R.id.txttotalunitstruk);
        totalhrg = findViewById(R.id.txttotalharga);
        totalharga2 = findViewById(R.id.totalharga2);
        totaldisc= findViewById(R.id.totaldisc);
        displaydisc = findViewById(R.id.displaydisc);
        disc=findViewById(R.id.discountext);
        displyhargaawal = findViewById(R.id.discplayhargaawal);
        time = String.valueOf(System.currentTimeMillis());
        oldprice=findViewById(R.id.hargaawal);
        linestruk=findViewById(R.id.linestruk2);
        via=findViewById(R.id.VIA);
        DecimalFormat decim = new DecimalFormat("#,###.##");
        Calendar calendar = Calendar.getInstance();
        print = findViewById(R.id.image1);
        int width= this.getResources().getDisplayMetrics().widthPixels;
        int height= this.getResources().getDisplayMetrics().heightPixels;
        totalWidth = width;
        totalHeight = height;
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
        Uri uri = FileProvider.getUriForFile(this, StrukOnlyPrint.this.getApplicationContext().getPackageName() + ".provider", file1);
        intent.setDataAndType(uri, mimeType);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "choseFile"));
    }


}