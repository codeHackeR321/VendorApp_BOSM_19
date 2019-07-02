package com.example.vendorapp.shared.singletonobjects

import okhttp3.Interceptor
import okhttp3.Response

class BaseInterceptor :Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val modifiedRequest = chain!!.request().newBuilder()
            .addHeader("HTTP_X_WALLET_TOKEN","ec123dac-339b-41ba-bca4-d3cab464083d")
            .build()
        return chain.proceed(modifiedRequest)
    }
}