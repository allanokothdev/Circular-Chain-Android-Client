package com.circularchained.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.circularchained.android.R;
import com.circularchained.android.constants.Constants;
import com.circularchained.android.listeners.ProductItemClickListener;
import com.circularchained.android.models.Esg;
import com.circularchained.android.models.Product;
import com.circularchained.android.models.Rating;
import com.circularchained.android.utils.GetUser;
import com.google.firebase.functions.FirebaseFunctions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{

    private final Context mContext;
    private final List<Product> stringList;
    private final FirebaseFunctions firebaseFunctions = FirebaseFunctions.getInstance();
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
        private final TextView ratingTextView;
        private final RatingBar ratingBar;
        private final Button button;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            subTextView = itemView.findViewById(R.id.subTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
            button = itemView.findViewById(R.id.button);
        }

        void bind(int position) {

            Product product = stringList.get(position);

            imageView.setTransitionName(product.getId());
            Glide.with(mContext.getApplicationContext()).load(product.getPic()).placeholder(R.drawable.placeholder).into(imageView);
            textView.setText(product.getTitle());
            subTextView.setText(product.getSummary());
            fetchRating(GetUser.fetchObject(mContext,Constants.PRIVATE_KEY),product.getBatchId(),ratingBar,ratingTextView);
            button.setOnClickListener(v -> productItemClickListener.onProductItemClick(product,imageView));
        }
    }


    private void fetchRating(String privateKey, int batchId, RatingBar ratingBar, TextView ratingTextView){
        Map<String, Object> data = new HashMap<>();
        data.put("key", privateKey);
        data.put("batch", batchId);
        firebaseFunctions.getHttpsCallable(Constants.CALCULATE_ESG).call(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                try {

                    Map<String, Object> result = (Map<String, Object>) task.getResult().getData();
                    assert result != null;

                    ArrayList<Integer> arrayList = (ArrayList<Integer>) result.get("esg");
                    assert arrayList != null;
                    Integer[] ratings =  arrayList.toArray(new Integer[arrayList.size()]);
                    Esg esgScore = new Esg(ratings[0],ratings[1],ratings[2],ratings[3],ratings[4]);
                    int noOfItems = (Integer.parseInt(result.get("noOfItems").toString()));
                    Rating response = new Rating(esgScore,noOfItems);
                    if (response != null){
                        Esg esg = response.getEsg();
                        float rating = (esg.getNatureScore() + esg.getWasteScore() + esg.getLabourScore() + esg.getCommunityScore() + esg.getWasteScore())/response.getNoOfItems();
                        rating = rating/5;
                        ratingBar.setRating(rating);
                        ratingTextView.setText(mContext.getString(R.string._4_5,rating));
                        ratingBar.setVisibility(View.VISIBLE);
                        ratingTextView.setVisibility(View.VISIBLE);
                    } else {
                        ratingBar.setVisibility(View.INVISIBLE);
                        ratingTextView.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                ratingBar.setVisibility(View.INVISIBLE);
                Toast.makeText(mContext, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
