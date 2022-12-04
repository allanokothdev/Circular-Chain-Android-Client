package com.circularchained.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.circularchained.android.constants.Constants;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.circularchained.android.adapters.ProductAdapter;
import com.circularchained.android.listeners.ProductItemClickListener;
import com.circularchained.android.models.Product;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements ProductItemClickListener, View.OnClickListener {

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private ListenerRegistration registration;
    private final Context mContext = MainActivity.this;
    private final List<Product> objectList = new ArrayList<>();
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(mContext, R.drawable.posts_divider)));
        recyclerView.addItemDecoration(divider);

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(this);
        ExtendedFloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(this);

        adapter = new ProductAdapter(mContext, objectList, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        fetchObjects();
    }


    private void fetchObjects(){
        Query query = firebaseFirestore.collection(Constants.PRODUCTS).orderBy("id", Query.Direction.DESCENDING);
        registration = query.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (queryDocumentSnapshots != null){
                for (DocumentChange documentChange: queryDocumentSnapshots.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        Product object = documentChange.getDocument().toObject(Product.class);
                        if (!objectList.contains(object)){
                            objectList.add(object);
                            adapter.notifyDataSetChanged();
                        }
                    }else if (documentChange.getType()==DocumentChange.Type.MODIFIED){
                        Product object = documentChange.getDocument().toObject(Product.class);
                        if (objectList.contains(object)){
                            objectList.set(objectList.indexOf(object),object);
                            adapter.notifyItemChanged(objectList.indexOf(object));
                        }
                    }else if (documentChange.getType()==DocumentChange.Type.REMOVED){
                        Product object = documentChange.getDocument().toObject(Product.class);
                        if (objectList.contains(object)){
                            objectList.remove(object);
                            adapter.notifyItemRemoved(objectList.indexOf(object));
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchObjects();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (registration != null){
            registration.remove();
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.floatingActionButton) {
            startActivity(new Intent(mContext, CreateProduct.class));
        }else if (v.getId() == R.id.imageView){
            startActivity(new Intent(mContext, ScanQR.class));
        }
    }

    @Override
    public void onProductItemClick(Product product, ImageView imageView) {
        Intent intent = new Intent(mContext, ProductDetail.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object", product);
        intent.putExtras(bundle);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, Pair.create(imageView, product.getId()));
        startActivity(intent,activityOptionsCompat.toBundle());
    }
}
