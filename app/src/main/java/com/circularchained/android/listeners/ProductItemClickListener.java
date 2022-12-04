package com.circularchained.android.listeners;

import android.widget.ImageView;

import com.circularchained.android.models.Product;

public interface ProductItemClickListener {

    void onProductItemClick(Product product, ImageView imageView);
}
