package com.r0th.shopping.Prevalent;



import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.r0th.shopping.Interface.ItemClickListner;
import com.r0th.shopping.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductName,txtProductPrice,txtProductQuantity,displaytotalhrg;
    private ItemClickListner itemClickListner;

    public CartViewHolder(View itemView) {
        super(itemView);
        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProductPrice = itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
        displaytotalhrg =itemView.findViewById(R.id.cartproducttotalprice);
    }

    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view,getAdapterPosition(),false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}


