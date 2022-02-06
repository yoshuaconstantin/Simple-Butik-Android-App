package com.r0th.shopping;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.r0th.shopping.Model.Cart;

import java.util.List;

public class data_item_barang_admin extends RecyclerView.Adapter<data_item_barang_admin.ViewHolder> {
    private Context context;
    private List<Cart> items;

    public data_item_barang_admin(Context context, List<Cart> carts){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_items_layout,parent,false);
        return new data_item_barang_admin.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Cart item = items.get(position);
        holder.rvnamabrng.setText(item);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView rvnamabrng,rvhargabrng,rvstockbrng,rvimg,rvdesc,rvcategory,rvdate,rvtime,rvgetpid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rvnamabrng = itemView.findViewById(R.id.product_name);
            rvhargabrng = itemView.findViewById(R.id.product_name);
            rvstockbrng = itemView.findViewById(R.id.product_name);
            rvdesc = itemView.findViewById(R.id.product_name);
            rvcategory = itemView.findViewById(R.id.product_name);
            rvdate = itemView.findViewById(R.id.product_name);
            rvtime = itemView.findViewById(R.id.product_name);
            rvgetpid = itemView.findViewById(R.id.product_name);
        }
    }
}
