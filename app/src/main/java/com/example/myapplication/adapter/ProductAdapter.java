package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.listener.OnItemProductClickListener;
import com.example.myapplication.model.Product;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products;
    private OnItemProductClickListener onItemProductClickListener;

    public ProductAdapter(List<Product> products, OnItemProductClickListener onItemProductClickListener) {
        this.products = products;
        this.onItemProductClickListener = onItemProductClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product=products.get(position);
        holder.name.setText(product.getName());
        holder.price.setText("$"+product.getPrice());
        String imageUrl = product.getImageCover();
        Picasso.get().load(imageUrl).into(holder.imageCover);
        holder.productItem.setOnClickListener(v -> onItemProductClickListener.onItemProductClick(product.getId()));

    }

    @Override
    public int getItemCount() {
        if(products!=null){
            return products.size();
        }
        return 0;


    }
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageCover;
        TextView name;
        TextView price;
        CardView productItem;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCover=itemView.findViewById(R.id.imageCover);
            name=itemView.findViewById(R.id.name);
            price=itemView.findViewById(R.id.price);
            productItem=itemView.findViewById(R.id.product_item_card);

        }








    }
}
