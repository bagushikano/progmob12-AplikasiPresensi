package com.progmobklp12.aplikasipresensi.api;

import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String baseURL = "https://bagushikano-test.herokuapp.com/api/";
    private static retrofit2.Retrofit retrofit;

    public static retrofit2.Retrofit buildRetrofit() {
        if(retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
