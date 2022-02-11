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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private DatabaseReference ProductsRef;
DateUtils dateUtils;
Button datepicker,tombolcob;
    int mMonth;
    int mYear;
    private DatePickerDialog datePickerDialog;
    TextView textdatepicker;
    String tanggalhasil ="Feb 2022";
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pembukuan);
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat month1 = new SimpleDateFormat("MMM yyy");
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

                            }

                        }).show();
            }
        });

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
                        DecimalFormat decim = new DecimalFormat("#,###.##");
                        pembukuanViewHolder.tanggal.setText(pembukuan_data_item.getTanggal());
                        pembukuanViewHolder.hijab.setText("Hijab : "+pembukuan_data_item.getHijab());
                        pembukuanViewHolder.aksesoris.setText("Aksesoris : "+pembukuan_data_item.getAksesoris());
                        pembukuanViewHolder.pakaian.setText("Pakaian : "+pembukuan_data_item.getPakaian());
                        pembukuanViewHolder.alsolat.setText("Alat Shalat : "+pembukuan_data_item.getAlatshalat());
                        pembukuanViewHolder.totaltrx.setText("Rp. "+decim.format(a));
                        pembukuanViewHolder.totalunit.setText(pembukuan_data_item.getTotalunit());


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
    }
}
