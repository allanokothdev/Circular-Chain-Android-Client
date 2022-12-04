package com.circularchained.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.circularchained.android.R;
import com.circularchained.android.listeners.ProductItemClickListener;
import com.circularchained.android.models.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{

    private final Context mContext;
    private final List<Product> stringList;
    private final ProductItemClickListener productItemClickListener;

    public ProductAdapter(Context mContext, List<Product> stringList, ProductItemClickListener productItemClickListener){
        this.mContext = mContext;
        this.stringList = stringList;
        this.productItemClickListener = productItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ((ViewHolder) holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imageView;
        private final TextView textView;
        private final TextView subTextView;
        private final RatingBar ratingBar;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            subTextView = itemView.findViewById(R.id.subTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }

        void bind(int position) {

            Product product = stringList.get(position);

            imageView.setTransitionName(product.getId());
            Glide.with(mContext.getApplicationContext()).load(product.getPic()).placeholder(R.drawable.placeholder).into(imageView);
            textView.setText(product.getTitle());
            subTextView.setText(product.getSummary());
            ratingBar.setRating(product.getRating());
            itemView.setOnClickListener(v -> productItemClickListener.onProductItemClick(product,imageView));

        }
    }

}
