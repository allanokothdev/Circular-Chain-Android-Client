package com.circularchained.android;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class Circular extends Application {

    private static Circular mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mInstance = this;
    }

    public static synchronized Circular getInstance() {
        return mInstance;
    }



}


