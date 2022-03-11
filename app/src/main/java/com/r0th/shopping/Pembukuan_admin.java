package com.r0th.shopping;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kal.rackmonthpicker.MonthType;
import com.kal.rackmonthpicker.RackMonthPicker;
import com.kal.rackmonthpicker.listener.DateMonthDialogListener;
import com.kal.rackmonthpicker.listener.OnCancelMonthDialogListener;
import com.r0th.shopping.Model.Products;
import com.r0th.shopping.Model.pembukuan_data_item;
import com.r0th.shopping.ViewHolder.PembukuanViewHolder;
import com.r0th.shopping.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Pembukuan_admin extends AppCompatActivity {
int mDay;
    private DatabaseReference ProductsRef,myref;
DateUtils dateUtils;
Button datepicker,tombolcob;
    int mMonth;
    int mYear;
    String totalbln,totalunit,totalkeuntungan="";
    private DatePickerDialog datePickerDialog;
    TextView textdatepicker,namabulan,totaltrx,totalunitterjual,totalkeuntangan;
    String tanggalhasil ="Feb 2022";
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pembukuan);
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat month1 = new SimpleDateFormat("MMM yyy");
        tanggalhasil = month1.format(calForDate.getTime());
        datepicker = findViewById(R.id.datePickerButton);
        //datepicker.setText(getTodaysDate());
        ProductsRef = FirebaseDatabase.getInstance().getReference("Pembukuan");
        recyclerView = findViewById(R.id.recdatapem);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RackMonthPicker(Pembukuan_admin.this)
                        .setLocale(Locale.ENGLISH).setMonthType(MonthType.TEXT)
                        .setPositiveButton(new DateMonthDialogListener() {
                            @Override
                            public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                                String text = String.valueOf(monthLabel);
                                String new_str = text.replaceAll(",","");
                                tanggalhasil = new_str;
                                onStart();

                            }
                        })
                        .setNegativeButton(new OnCancelMonthDialogListener() {
                            @Override
                            public void onCancel(AlertDialog dialog) {

                                dialog.dismiss();
                            }

                        }).show();
            }
        });
        //totalbulanan();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<pembukuan_data_item> options =
                new FirebaseRecyclerOptions.Builder<pembukuan_data_item>()
                        .setQuery(ProductsRef.child(tanggalhasil), pembukuan_data_item.class)
                        .build();

        FirebaseRecyclerAdapter<pembukuan_data_item, PembukuanViewHolder> adapter =
                new FirebaseRecyclerAdapter<pembukuan_data_item, PembukuanViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PembukuanViewHolder pembukuanViewHolder, int i, @NonNull pembukuan_data_item pembukuan_data_item) {
                        int a = Integer.parseInt(pembukuan_data_item.getTotaltransaksi());
                        int b = Integer.parseInt(pembukuan_data_item.getKeuntungan());
                        DecimalFormat decim = new DecimalFormat("#,###.##");
                        pembukuanViewHolder.tanggal.setText(pembukuan_data_item.getTanggal());
                        pembukuanViewHolder.hijab.    setText("Hijab⠀⠀⠀⠀⠀⠀⠀: "+pembukuan_data_item.getHijab());
                        pembukuanViewHolder.aksesoris.setText("Aksesoris⠀⠀⠀⠀: "+pembukuan_data_item.getAksesoris());
                        pembukuanViewHolder.pakaian.  setText("Pakaian⠀⠀⠀⠀   : "+pembukuan_data_item.getPakaian());
                        pembukuanViewHolder.alsolat.  setText("Alat Shalat⠀⠀ ⠀: "+pembukuan_data_item.getAlatshalat());
                        pembukuanViewHolder.totaltrx. setText("Rp. "+decim.format(a));
                        pembukuanViewHolder.totalunit.setText(pembukuan_data_item.getTotalunit());
                        pembukuanViewHolder.profit.setText("Rp. "+decim.format(b));


                    }


                    @NonNull
                    @Override
                    public PembukuanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout_pembukuan, parent, false);
                        PembukuanViewHolder holder = new PembukuanViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        searchtotal();

    }

    private void searchtotal(){

        namabulan = findViewById(R.id.nmbulan);
        totaltrx = findViewById(R.id.totaltrx);
        totalunitterjual = findViewById(R.id.totalunit);
        totalkeuntangan=findViewById(R.id.totalkeuntungan);
        myref = FirebaseDatabase.getInstance().getReference("Pembukuan");
        namabulan.setText(tanggalhasil);
        myref.child(tanggalhasil).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sum1 = 0;
                int sum2 =0;
                int sum3 = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){


                    sum1 += Integer.parseInt ( String.valueOf ( dataSnapshot.child ( "totaltransaksi" ).getValue () ) );
                    sum2 += Integer.parseInt ( String.valueOf ( dataSnapshot.child ( "totalunit" ).getValue () ) );
                    sum3 += Integer.parseInt ( String.valueOf ( dataSnapshot.child ( "keuntungan" ).getValue () ) );

                }
                totalbln = String.valueOf(sum1);
                totalunit = String.valueOf(sum2);
                totalkeuntungan = String.valueOf(sum3);

                int a = Integer.parseInt(totalbln);
                int b = Integer.parseInt(totalkeuntungan);
                DecimalFormat decim = new DecimalFormat("#,###.##");
                totaltrx.setText("Transaction - Rp. "+decim.format(a));
                totalunitterjual.setText("Sold - "+totalunit+" Unit");
                totalkeuntangan.setText("Profit - Rp.  "+decim.format(b));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
