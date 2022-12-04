package com.circularchained.android;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.circularchained.android.constants.Constants;
import com.circularchained.android.models.Product;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class CreateProduct extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout container;
    private final Context mContext = CreateProduct.this;
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private Button button;
    private ImageView imageView;
    private Spinner spinner;
    private TextInputEditText titleTextInputEditText;
    private TextInputEditText summaryTextInputEditText;
    private TextInputEditText batchTextInputEditText;
    private AVLoadingIndicatorView progressBar;
    private Uri mainImageUri = null;
    private String downloadUrlString = null;
    public final static int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        container = findViewById(R.id.container);
        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);
        titleTextInputEditText = findViewById(R.id.titleTextInputEditText);
        summaryTextInputEditText = findViewById(R.id.summaryTextInputEditText);
        batchTextInputEditText = findViewById(R.id.batchTextInputEditText);
        spinner = findViewById(R.id.spinner);
        progressBar = findViewById(R.id.progressBar);
        button.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    @TargetApi(23)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {

            String title = titleTextInputEditText.getText().toString().trim();
            String summary = summaryTextInputEditText.getText().toString().trim();
            String batch = batchTextInputEditText.getText().toString().trim();
            String category = spinner.getSelectedItem().toString().trim();

            progressBar.setVisibility(View.VISIBLE);

            if (TextUtils.isEmpty(title)) {

                titleTextInputEditText.setError(getString(R.string.enter_product_name));
                titleTextInputEditText.requestFocus();
                progressBar.setVisibility(View.GONE);

            } else if (TextUtils.isEmpty(summary)){

                summaryTextInputEditText.setError(getString(R.string.enter_product_summary));
                progressBar.setVisibility(View.GONE);
                summaryTextInputEditText.requestFocus();

            } else if (TextUtils.isEmpty(batch)){

                batchTextInputEditText.setError(getString(R.string.enter_batch_number));
                progressBar.setVisibility(View.GONE);
                batchTextInputEditText.requestFocus();

            } else if (mainImageUri==null){

                imageView.requestFocus();
                Toast.makeText(mContext, "Add Product Photo Profile Photo",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            } else if (TextUtils.isEmpty(category)){

                imageView.requestFocus();
                Toast.makeText(mContext, "Select Product Category",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            } else {
                button.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                createProduct(title, summary, batch, category);
            }
        } else if (v.getId()==R.id.imageView){

            Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(galleryIntent, "Select Brand Image"),PICK_IMAGE);

        }
    }

    private void createProduct(String title, String summary, String batch, String category){
        try {
            ArrayList<String> tags = new ArrayList<>();
            tags.add(batch);
            tags.add(title);
            tags.add(category);
            String productID = firebaseFirestore.collection(Constants.PRODUCTS).document().getId();
            if (downloadUrlString != null){
                Product product = new Product(productID,title,downloadUrlString,summary,category,Integer.parseInt(batch),0,tags);
                firebaseFirestore.collection(Constants.PRODUCTS).document(productID).set(product).addOnSuccessListener(unused -> {
                    progressBar.setVisibility(View.GONE);
                    finishAfterTransition();
                }).addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    finishAfterTransition();
                    Toast.makeText(mContext,"Try Again",Toast.LENGTH_SHORT).show();
                });

            } else {
                final StorageReference ref = FirebaseStorage.getInstance().getReference().child(Constants.PRODUCTS).child(System.currentTimeMillis() + ".png");
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),mainImageUri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                byte[] data = byteArrayOutputStream.toByteArray();
                UploadTask uploadTask = ref.putBytes(data);
                uploadTask.addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        Product product = new Product(productID,title,downloadUri.toString(),summary,category,Integer.parseInt(batch),0,tags);
                        firebaseFirestore.collection(Constants.PRODUCTS).document(productID).set(product).addOnSuccessListener(unused -> {
                            progressBar.setVisibility(View.GONE);
                            finishAfterTransition();
                        }).addOnFailureListener(e -> {
                            progressBar.setVisibility(View.GONE);
                            finishAfterTransition();
                            Toast.makeText(mContext,"Try Again",Toast.LENGTH_SHORT).show();
                        });
                    }
                })).addOnFailureListener(e -> {
                    button.setEnabled(true);
                    Snackbar snackbar = Snackbar.make(container, Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_SHORT);snackbar.show();
                    progressBar.setVisibility(View.INVISIBLE);
                });
            }
        } catch (IOException e){e.printStackTrace(); }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode ==PICK_IMAGE && resultCode == RESULT_OK && data != null){
                mainImageUri = data.getData();
                imageView.setImageURI(mainImageUri);
                postImage(mainImageUri);
            }
        }catch (Exception e){e.printStackTrace(); }
    }

    private void postImage(Uri mainImageUri){
        try {
            final StorageReference ref = FirebaseStorage.getInstance().getReference().child(Constants.PRODUCTS).child(System.currentTimeMillis() + ".png");
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),mainImageUri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
            byte[] data = byteArrayOutputStream.toByteArray();
            UploadTask uploadTask = ref.putBytes(data);
            uploadTask.addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    downloadUrlString = downloadUri.toString();
                }
            })).addOnFailureListener(e -> {
                Snackbar snackbar = Snackbar.make(container, Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_SHORT);snackbar.show();
                progressBar.setVisibility(View.INVISIBLE);
                button.setEnabled(true);
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}