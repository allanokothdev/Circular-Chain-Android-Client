package com.circularchained.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.circularchained.android.adapters.StageAdapter;
import com.circularchained.android.constants.Constants;
import com.circularchained.android.models.Esg;
import com.circularchained.android.models.Product;
import com.circularchained.android.models.Stage;
import com.circularchained.android.utils.GetUser;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.wang.avi.AVLoadingIndicatorView;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProductDetail extends AppCompatActivity {

    private final Context mContext = ProductDetail.this;
    private final FirebaseFunctions firebaseFunctions = FirebaseFunctions.getInstance();
    private AVLoadingIndicatorView progressBar;

    private final List<Stage> objectList = new ArrayList<>();
    private StageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Product product = (Product) bundle.getSerializable("object");
        String privateKey = GetUser.fetchObject(mContext, Constants.PRIVATE_KEY);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(product.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(v -> finishAfterTransition());
        progressBar = findViewById(R.id.progressBar);
        ImageView imageView = findViewById(R.id.imageView);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL));
        adapter = new StageAdapter(mContext, objectList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        imageView.setTransitionName(product.getId());
        Glide.with(mContext.getApplicationContext()).load(product.getPic()).centerCrop().dontAnimate().listener(new RequestListener<Drawable>() {@Override public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) { supportStartPostponedEnterTransition();return false; }@Override public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) { supportStartPostponedEnterTransition();return false; }}).into(imageView);
        fetchStages(privateKey, product.getBatchId());
    }

    private void fetchStages(String privateKey, int batchId){
        Map<String, Object> data = new HashMap<>();
        data.put("key", privateKey);
        data.put("batch", batchId);
        firebaseFunctions.getHttpsCallable(Constants.FETCH_STAGES).call(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                ArrayList<Object> result = (ArrayList<Object>) task.getResult().getData();
                Toast.makeText(mContext, result.toString(), Toast.LENGTH_SHORT).show();

                if (result != null){
                    for (Object response: result){
                        try {
                            int stageId = new BigInteger(String.valueOf(response.getClass().getField(""))).intValue();
                            int batch = new BigInteger(String.valueOf(response.getClass().getField(""))).intValue();
                            String title = response.getClass().getField("").toString();
                            String publisher = response.getClass().getField("").toString();;
                            String summary = response.getClass().getField("").toString();;
                            long timestamp = new BigInteger(String.valueOf(response.getClass().getField(""))).longValue();
                            String location = response.getClass().getField("").toString();;
                            Esg esg = response.getClass().getField("").get(Object);
                            

                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }

                        Stage stage = new Stage(stageId,title,summary,publisher,timestamp,location,esg,batch);


                        if (!objectList.contains(stage)){
                            objectList.add(0,stage);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    Toast.makeText(mContext, "Sorry, we are yet to verify the Sustainability of this product", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);

            } else {
                Toast.makeText(mContext, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }


}