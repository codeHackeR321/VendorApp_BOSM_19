package com.example.vendorapp.singletonObjects

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object{

        var retrofitService: RetrofitApi? = null

        @Synchronized fun getRetroInstance(): RetrofitApi{

            if (retrofitService == null)
            {
                //Base url of test api from sheety
                retrofitService = Retrofit.Builder().baseUrl("https://my-json-server.typicode.com/PrarabdhGarg/VendorApp")
                    .addConverterFactory(GsonConverterFactory.create()).build().create(RetrofitApi::class.java)
            }

            return retrofitService!!
        }

    }
}