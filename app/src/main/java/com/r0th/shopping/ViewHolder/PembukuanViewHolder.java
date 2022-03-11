package com.r0th.shopping.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.r0th.shopping.Interface.ItemClickListner;
import com.r0th.shopping.R;

public class PembukuanViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView tanggal,hijab,pakaian,aksesoris,alsolat,totaltrx,totalunit,profit;
    public ItemClickListner listner;
    public PembukuanViewHolder(@NonNull View itemView) {
        super(itemView);

        tanggal = itemView.findViewById(R.id.txttanggal);
        hijab = itemView.findViewById(R.id.txthijab);
        pakaian = itemView.findViewById(R.id.txtpakaian);
        aksesoris = itemView.findViewById(R.id.txtaksesoris);
        alsolat = itemView.findViewById(R.id.txtalsol);
        totaltrx = itemView.findViewById(R.id.datatotaltrx);
        totalunit = itemView.findViewById(R.id.datatotalunit);
        profit = itemView.findViewById(R.id.dataprofit);

    }

    @Override
    public void onClick(View view) {
        listner.onClick(view, getAdapterPosition(), false);
    }
}
