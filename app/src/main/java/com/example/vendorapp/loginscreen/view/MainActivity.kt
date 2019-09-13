package com.example.vendorapp.loginscreen.view

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context.NOTIFICATION_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.vendorapp.MainScreenActivity

import com.example.vendorapp.R
import com.example.vendorapp.loginscreen.viewModel.LoginViewModel
import com.example.vendorapp.loginscreen.viewModel.LoginViewModelFactory

import com.example.vendorapp.notification.MyFirebaseMessagingService
import com.example.vendorapp.shared.UIState
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.iid.FirebaseInstanceId

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private lateinit var viewModel: LoginViewModel
    private var isLoggingIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showLoadingStateActivity()
        viewModel =
            ViewModelProviders.of(this, LoginViewModelFactory(this)).get(LoginViewModel::class.java)

        initializeApp()

        viewModel.loginStatus.observe(this, Observer {
            when (it!!) {
                UIState.GoToMainScreen -> {
                    removeLoadingStateActivity()
                    Log.d("LoginActivity", "go to msin screen ")
                    Toast.makeText(this@MainActivity, "Login Successfull", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this@MainActivity, MainScreenActivity::class.java))
                    finish()

                }

                is UIState.ErrorState -> {
                    removeLoadingStateActivity()
                    Log.d("LoginActivity2", "error state${(it as UIState.ErrorState).message} ")
                    Snackbar.make(coordinatorLayout,"ERROR:${(it as UIState.ErrorState).message} ",Snackbar.LENGTH_LONG).show()

                }

                is UIState.NoInternetConnection->{
                    removeLoadingStateActivity()
                    showAlertDialogBox()
                  //  Toast.makeText(this,"Alert: Internet Connection Not found.Please connect and Restart the App",Toast.LENGTH_LONG).show()
                }
            }
        })

        buttonSignIn.setOnClickListener {
            onSignInButtonPressed()
        }
    }

    private fun onSignInButtonPressed() {
        Log.d("Testing", "Entered onClick listener for sign in button")
        showLoadingStateActivity()
        if (editTextUsername.text.toString().isBlank() && editTextPassword.text.toString().isBlank()) {
            Snackbar.make(coordinatorLayout,"Enter valid username/password",Snackbar.LENGTH_LONG).show()
            removeLoadingStateActivity()
        }
        else if(viewModel.getFirebaseRegToken().isEmpty()){
            Log.d("Testing", "Entered empty reg token condition")
            showLoadingStateActivity()
            isLoggingIn = true
        }
        else{
            Log.d("LoginActivity1", "viewModel.login called")
            Log.d("Testing", "Starting to log in user")
            viewModel.login(editTextUsername.text.toString(),editTextPassword.text.toString(),viewModel.getFirebaseRegToken())
        }
    }

    /**
     * This method is used for the initial setup of the notification channels
     * If the notification chanel already exists, no action is taken, and hence it is safe to call this method every time the app starts
     * */
    fun setupNotificationChannel() {
        // Notification Channels are only available for Oreo(Api Level 26) and onwards
        // Since support libraries don't have a library for setting up notification channels, this check is necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val newOrderChanel = NotificationChannel(
                getString(R.string.chanel_id_newOrder),
                getString(R.string.chanel_name_newOrder),
                NotificationManager.IMPORTANCE_HIGH
            )
            newOrderChanel.description = getString(R.string.chanel_desc_newOrder)
            newOrderChanel.canBypassDnd()
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(newOrderChanel)

        }

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            Log.d("Testing", "Recived New Token = ${it.token}}")
            viewModel.saveFirebaseRegToken(it.token)
            if (isLoggingIn) {
                Log.d("Testing", "Entered isLogging in condition")
                onSignInButtonPressed()
            }
        }.addOnFailureListener {
            Log.e("Main Activity", "Failed to recive token$it")
            setupNotificationChannel()
        }

    }

    /**
     * It is the job of this function to perform all the one-time setups that are needed to be done when the app starts
     * for the first time
     * It sets up notification Channel
     * */
    fun initializeApp(){

        if (viewModel.getJWT().equals(getString(R.string.default_jwt_value)) || viewModel.getVendorId().equals(getString(R.string.default_vendor_id))){
            removeLoadingStateActivity()
            Snackbar.make(coordinatorLayout,"Please login to continue",Snackbar.LENGTH_LONG).show()
        } else {
            // login karwa
            Snackbar.make(coordinatorLayout,"Welcome",Snackbar.LENGTH_LONG).show()
            removeLoadingStateActivity()
            val intent = Intent(this, MainScreenActivity::class.java)
            startActivity(intent)
            finish()
        }
        setupNotificationChannel()
        startService(Intent(this, MyFirebaseMessagingService::class.java))

    }

    /**
     * This method enables the Progress Bar and makes disables the screen
     * */
    private fun showLoadingStateActivity() {
        buttonSignIn.isClickable = false
        if (!prog_bar_main_activity.isVisible) {
            prog_bar_main_activity.visibility = View.VISIBLE
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }
    }

    /**
     * This method removes the Progress Bar and re-enables the screen
    **/
    private fun removeLoadingStateActivity() {
        buttonSignIn.isClickable = true
        if (prog_bar_main_activity.isVisible) {
            prog_bar_main_activity.visibility = View.INVISIBLE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    private fun showAlertDialogBox(){
        val alertDialog: AlertDialog? = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply{

                setNeutralButton("ok") { dialog, id ->
                    dialog.dismiss()
                }
            }

            builder.setMessage("Please connect and Restart the App")
            builder.setTitle("No Internet Connection Found")
            builder.create()
        }
        alertDialog?.setCanceledOnTouchOutside(false)
        alertDialog?.show()
    }
    }



