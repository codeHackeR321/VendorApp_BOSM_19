package com.example.vendorapp.loginscreen.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.vendorapp.MainScreenActivity

import com.example.vendorapp.R
import com.example.vendorapp.loginscreen.viewModel.LoginViewModel
import com.example.vendorapp.loginscreen.viewModel.LoginViewModelFactory

import com.example.vendorapp.notification.MyFirebaseMessagingService

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_fra_new_order.*

class MainActivity : AppCompatActivity() {

private lateinit var  viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showLoadingStateFragment()
        viewModel= ViewModelProviders.of(this, LoginViewModelFactory(this)).get(LoginViewModel::class.java)

        initializeApp()

        viewModel.loginStatus.observe(this , Observer {
            when(it!!)
            {
                UIState.GoToMainScreen -> {
                   removeLoadingStateFragment()
                    Toast.makeText(this@MainActivity, "Login Successfull",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@MainActivity,MainScreenActivity::class.java))
                    finish()

                }

              is UIState.ErrorState -> {
             removeLoadingStateFragment()
              Toast.makeText(this@MainActivity,(it as UIState.ErrorState).message,Toast.LENGTH_SHORT).show()
            }
            }
        })

        buttonSignIn.setOnClickListener {
            showLoadingStateFragment()
            var username=editTextUsername.text.toString()
            var password = editTextPassword.text.toString()
            if (username.isBlank()&&password.isBlank())
            {
                Toast.makeText(this@MainActivity,"Enter valid username/password",Toast.LENGTH_SHORT).show()
                removeLoadingStateFragment()
            }
            else
            {

                viewModel.login(username,password)

            }
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

        val jwt_token=viewModel.getJWT()
        if(jwt_token.equals(getString(R.string.default_jwt_value)))
        {
            //Loader hata
            //snack bar dikhana h
            removeLoadingStateFragment()
           Toast.makeText(this@MainActivity,"Please Login to Continue",Toast.LENGTH_LONG).show()        }
        else
        {
            // login karwa
            Toast.makeText(this,"Already Logged in",Toast.LENGTH_LONG).show()
            removeLoadingStateFragment()
            val intent=Intent(this , MainScreenActivity::class.java)
            startActivity(intent)
            finish()
        }
        setupNotificationChannel()
        startService(Intent(this,MyFirebaseMessagingService::class.java))

    }

    private fun showLoadingStateFragment(){
        if (!prog_bar_main_activity.isVisible) {
            prog_bar_main_activity.visibility = View.VISIBLE
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }}

    private fun removeLoadingStateFragment(){
        if (prog_bar_main_activity.isVisible) {
            prog_bar_main_activity.visibility = View.INVISIBLE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }


}
