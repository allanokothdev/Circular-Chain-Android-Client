package com.circularchained.android;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.circularchained.android.constants.Constants;
import com.circularchained.android.models.Product;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

public class ScanQR extends AppCompatActivity implements View.OnClickListener {

    private CodeScanner mCodeScanner;
    private final Context mContext = ScanQR.this;
    private FrameLayout container;
    private CodeScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        try {

            ImageView closeImageView = findViewById(R.id.closeImageView);
            container = findViewById(R.id.container);
            closeImageView.setOnClickListener(this);
            scannerView = findViewById(R.id.scanner_view);
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ScanQR.this, new String[]{Manifest.permission.CAMERA}, 1);
            }
            initialize();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initialize(){
        try {
            mCodeScanner = new CodeScanner(this, scannerView);
            mCodeScanner.startPreview();
            mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> fetchObject(result.getText())));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (mCodeScanner != null){
                mCodeScanner.startPreview();
            } else {
                initialize();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        if (mCodeScanner != null){
            mCodeScanner.startPreview();
        } else {
            initialize();
        }
    }

    @Override
    protected void onPause() {
        if (mCodeScanner != null){
            mCodeScanner.releaseResources();
        } else {
            initialize();
        }
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.closeImageView){
            finishAfterTransition();
        }
    }

    private void fetchObject(String objectID){
        FirebaseFirestore.getInstance().collection(Constants.PRODUCTS).document(objectID).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()){
                Product product = documentSnapshot.toObject(Product.class);
                assert product != null;
                Bundle bundle = new Bundle();
                bundle.putSerializable("object", product);
                startActivity(new Intent(mContext, ProductDetail.class).putExtras(bundle));
                finishAfterTransition();
            } else {
                Snackbar snackbar = Snackbar.make(container,"Sorry, we are yet to verify the Sustainability of this product", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });


    }




}