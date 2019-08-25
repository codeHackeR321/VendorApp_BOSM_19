package com.example.vendorapp.shared.singletonobjects

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.lang.Exception

class BaseInterceptor :Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var modifiedRequest: Request?=null
    try {
        modifiedRequest = chain!!.request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("X-Wallet-Token", "ec123dac-339b-41ba-bca4-d3cab464083d")
            .build()

    }
    catch (e: Exception)
    {
        Log.d("ExceptioInterceptor","erro: $e")
    }
        return chain.proceed(modifiedRequest!!)
    }
}
//io.reactivex.exceptions.OnErrorNotImplementedException: The exception was not handled due to missing onError handler in the subscribe() method call
//5D6145580307-0001-3C54-6F3ACC1EED2D