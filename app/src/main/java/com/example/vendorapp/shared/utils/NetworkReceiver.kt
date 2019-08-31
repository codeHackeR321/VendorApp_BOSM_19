package com.example.vendorapp.shared.utils



import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import android.widget.Toast
import com.example.vendorapp.shared.Listeners.NetConnectionChanged

class NetworkReceiver(val listener : NetConnectionChanged) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val conn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = conn.activeNetworkInfo

        // Checks the user prefs and the network connection. Based on the result, decides whether
        // to refresh the display or keep the current display.
        // If the userpref is Wi-Fi only, checks to see if the device has a Wi-Fi connection.
        if (/*WIFI == sPref &&*/ networkInfo?.type == ConnectivityManager.TYPE_WIFI) {
            // If device has its Wi-Fi connection, sets refreshDisplay
            // to true. This causes the display to be refreshed when the user
            // returns to the app.
            // refreshDisplay = true
            listener.buttonClicked("wifi")
            Toast.makeText(context, "wifi connected", Toast.LENGTH_LONG).show()

            // If the setting is ANY network and there is a network connection
            // (which by process of elimination would be mobile), sets refreshDisplay to true.
        }else if (/*ANY == sPref && */networkInfo?.type==ConnectivityManager.TYPE_MOBILE) {
            // refreshDisplay = true

            // Otherwise, the app can't download content--either because there is no network
            // connection (mobile or Wi-Fi), or because the pref setting is WIFI, and there
            // is no Wi-Fi connection.
            // Sets refreshDisplay to false.
            listener.buttonClicked("data")
            Toast.makeText(context, "connected${networkInfo.typeName}", Toast.LENGTH_LONG).show()
        }

        else if (/*ANY == sPref && */networkInfo != null) {
            // refreshDisplay = true

            // Otherwise, the app can't download content--either because there is no network
            // connection (mobile or Wi-Fi), or because the pref setting is WIFI, and there
            // is no Wi-Fi connection.
            // Sets refreshDisplay to false.
            listener.buttonClicked("connected${networkInfo.typeName}")
            Toast.makeText(context, "connected${networkInfo.typeName}", Toast.LENGTH_LONG).show()
        } else {
            // refreshDisplay = false
            listener.buttonClicked("lost_connection")
            Toast.makeText(context, "lost_connection", Toast.LENGTH_LONG).show()
        }

        /*if (*//*ANY == sPref && *//*networkInfo?.subtype==TelephonyManager.NETWORK_TYPE_EDGE) {
            // refreshDisplay = true

            // Otherwise, the app can't download content--either because there is no network
            // connection (mobile or Wi-Fi), or because the pref setting is WIFI, and there
            // is no Wi-Fi connection.
            // Sets refreshDisplay to false.
            listener.buttonClicked("edghe slow internet")
            Toast.makeText(context, "connected${networkInfo.typeName}", Toast.LENGTH_LONG).show()
        }*/
    }
}