package com.example.vendorapp.loginscreen.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vendorapp.MainScreenActivity
import com.example.vendorapp.R
import com.example.vendorapp.notification.MyFirebaseMessagingService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeApp()

        loginButton.setOnClickListener {
            startActivity(Intent(this , MainScreenActivity::class.java))
        }
    }

    /**
     * This method is used for the initial setup of the notification channels
     * If the notification chanel already exists, no action is taken, and hence it is safe to call this method every time the app starts
     * */
    fun setupNotificationChannel(){
        // Notification Channels are only available for Oreo(Api Level 26) and onwards
        // Since support libraries don't have a library for setting up notification channels, this check is necessary
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val newOrderChanel = NotificationChannel(getString(R.string.chanel_id_newOrder) , getString(R.string.chanel_name_newOrder) , NotificationManager.IMPORTANCE_HIGH)
            newOrderChanel.description = getString(R.string.chanel_desc_newOrder)
            newOrderChanel.canBypassDnd()
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(newOrderChanel)
        }
    }

    /**
     * It is the job of this function to perform all the one-time setups that are needed to be done when the app starts
     * for the first time
     * It sets up notification Channel
     * */
    fun initializeApp(){
        setupNotificationChannel()
        startService(Intent(this,MyFirebaseMessagingService::class.java))
    }
}
