package com.example.vendorapp.shared.utils

import android.content.Context
import androidx.core.content.ContextCompat.getSystemService
import android.net.ConnectivityManager



class NetworkConnectivityCheck {

    fun checkIntenetConnection(context: Context) : Boolean
    {
        var connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connManager.activeNetworkInfo != null)
        {
            return true
        }
        return false
    }

}