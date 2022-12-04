package com.circularchained.android.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.circularchained.android.constants.Constants;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

public class GetUser extends Application {

    public static void saveObject(Context context, String key, String value) {
        try{
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(Constants.CIRCULAR_CHAIN, masterKeyAlias, context, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
        } catch (GeneralSecurityException | IOException exception){
            exception.printStackTrace();
        }
    }

    public static String fetchObject(Context context, String key) {
        String file = Constants.PRIVATE_KEY;
        try{
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(Constants.CIRCULAR_CHAIN, masterKeyAlias, context, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            file = sharedPreferences.getString(key, Constants.PRIVATE_KEY);
        } catch (GeneralSecurityException | IOException exception){
            exception.printStackTrace();
        }
        return file;
    }


}
