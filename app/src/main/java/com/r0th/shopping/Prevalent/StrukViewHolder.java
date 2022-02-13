package com.r0th.shopping.Prevalent;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.r0th.shopping.R;

public class StrukViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView namabrng,qtybrng,hargasatuan;
    public StrukViewHolder(@NonNull View itemView) {
        super(itemView);
        namabrng = itemView.findViewById(R.id.txtbarangstruk);
        qtybrng = itemView.findViewById(R.id.qtystruk);
        hargasatuan = itemView.findViewById(R.id.hargasatuanstruk);

    }

    @Override
    public void onClick(View view) {

    }
}
