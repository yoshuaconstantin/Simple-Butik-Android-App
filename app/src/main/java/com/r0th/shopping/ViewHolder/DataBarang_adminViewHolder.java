package com.r0th.shopping.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.r0th.shopping.R;

public class DataBarang_adminViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

    public TextView rvnmbarang,rvstockbrng,rvhargabrng,discount;
    public ImageView imageView;
    public DataBarang_adminViewHolder(@NonNull View itemView) {
        super(itemView);
        rvnmbarang = itemView.findViewById(R.id.product_name);
        rvstockbrng = itemView.findViewById(R.id.product_description);
        imageView = itemView.findViewById(R.id.product_image);
        rvhargabrng = itemView.findViewById(R.id.product_price);


        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Pilih Menu");
        contextMenu.add(0,0,getAdapterPosition(), "Update");
        contextMenu.add(0,1,getAdapterPosition(), "Delete");
    }
}
