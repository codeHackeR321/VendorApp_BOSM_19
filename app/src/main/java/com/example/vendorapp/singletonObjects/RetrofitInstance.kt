package com.example.vendorapp.singletonObjects

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object{

        var retrofitInstance: Retrofit? = null

        @Synchronized fun getRetroInstance(): Retrofit{

            if (retrofitInstance == null)
            {
                //Base url of test api from sheety
                retrofitInstance = Retrofit.Builder().baseUrl("https://my-json-server.typicode.com/PrarabdhGarg/VendorApp")
                    .addConverterFactory(GsonConverterFactory.create()).build()
            }

            return retrofitInstance!!
        }

    }
}