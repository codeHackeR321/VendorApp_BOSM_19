package com.example.vendorapp.singletonobjects

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object{

        var retrofitService: RetrofitApi? = null

        @Synchronized fun getRetroInstance(): RetrofitApi{

            if (retrofitService == null)
            {
                //Base url of test api from sheety
                //RxJava adapter for api calls
                retrofitService = Retrofit.Builder().baseUrl("https://my-json-server.typicode.com/PrarabdhGarg/VendorApp")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(RetrofitApi::class.java)
            }

            return retrofitService!!
        }

    }
}