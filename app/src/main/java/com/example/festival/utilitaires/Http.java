package com.example.festival.utilitaires;

import okhttp3.OkHttpClient;

public class Http {

    private static OkHttpClient instance = null;

    public synchronized static OkHttpClient getInstance(){
        if(instance==null) instance = new OkHttpClient();
        return instance;
    }
}