package com.circularchained.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.circularchained.android.constants.Constants;
import com.circularchained.android.utils.GetUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.functions.FirebaseFunctions;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

public class Splash extends AppCompatActivity implements View.OnClickListener{

    private final Context mContext = Splash.this;
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFunctions firebaseFunctions = FirebaseFunctions.getInstance();
    private GoogleSignInClient googleSignInClient;
    private final String TAG = this.getClass().getSimpleName();
    private RelativeLayout container;
    private Button button;
    private AVLoadingIndicatorView progressBar;
    private static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progressBar);
        container = findViewById(R.id.container);
        button = findViewById(R.id.button);
        button.setOnClickListener(this);

        getState();
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(mContext, googleSignInOptions);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Timber.d("Google sign in success");
                firebaseAuthWithGoogle(account);
            }catch (ApiException e){
                Timber.tag(TAG).w(e, "Google sign in failed");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        boolean isNewUser = Objects.requireNonNull(task.getResult().getAdditionalUserInfo()).isNewUser();
                        sendUserToCreateActivity(isNewUser);
                    }else {
                        Snackbar snackbar = Snackbar.make(container, Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()), Snackbar.LENGTH_SHORT);snackbar.show();
                    }
                });
    }



    private void getState(){
        if (firebaseUser != null){
            button.setVisibility(View.GONE);
            new Handler().postDelayed(() -> {
                sendUserToCreateActivity(false);
            }, 100);
        }else {
            button.setVisibility(View.VISIBLE);
        }
    }

    private void sendUserToCreateActivity(boolean status) {
        if (status){
            createWallet();
        }else {
            startActivity(new Intent(mContext, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideSystemUi(){
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            hideSystemUi();
        }
    }

    private void createWallet() {
        firebaseFunctions.getHttpsCallable(Constants.CREATE_WALLET).call().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Map<String, Object> result = (Map<String, Object>) task.getResult().getData();
                assert result != null;
                String privateKey = Objects.requireNonNull(result.get("privateKey")).toString();
                String address = Objects.requireNonNull(result.get("address")).toString();
                Toast.makeText(mContext,address,Toast.LENGTH_SHORT).show();
                GetUser.saveObject(mContext,Constants.PRIVATE_KEY,privateKey);
                GetUser.saveObject(mContext,Constants.ADDRESS,address);
                startActivity(new Intent(mContext, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

            } else {
                Snackbar snackbar = Snackbar.make(container, Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()), Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
    }
}

