package com.example.vendorapp.loginscreen.model

import android.content.Context
import android.util.Log
import com.example.vendorapp.R
import com.example.vendorapp.shared.UIState
import com.example.vendorapp.shared.singletonobjects.RetrofitInstance
import com.google.gson.JsonObject
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class LoginRepository(val activity: Context) {

    //  private val menuDao: MenuDao
    private val loginApiCall=RetrofitInstance.getRetroInstance()
    private val sharedPref = activity.getSharedPreferences(
        activity.getString(R.string.preference_file_login), Context.MODE_PRIVATE
    )
   private val defaultJWTValue = activity.getString(R.string.default_jwt_value)
    private val defaultVendorId=activity.getString(R.string.default_vendor_id)
    fun getJWTfromSharedPref():String {
        val jwt_token = sharedPref.getString(activity.getString(R.string.saved_jwt), defaultJWTValue)
        return jwt_token!!
    }

    fun getVendorIdfromSharedPref():String {
        val vendorId=sharedPref.getString(activity.getString(R.string.saved_jwt), defaultJWTValue)
        return vendorId!!
    }

    fun getFirebaseRegTokenFromSharedPref():String{
        val regToken=sharedPref.getString("token","")
        return regToken
    }

    fun  saveFirebaseRegTokenToSharedPref(token:String){
        sharedPref.edit().putString("token", token).apply()

    }
    fun loginWithAuth(username: String, password: String,token:String): Single<UIState>{
        val body = JsonObject().also {
            it.addProperty("username", username)
            it.addProperty("password",  password)
            it.addProperty( "reg_token",token)

    }


    Log.d("checkLogin Repo","bodysent : $body")
var x = activity.getString(R.string.login_failed)
   return loginApiCall.getJWTfromAuth(body).subscribeOn(Schedulers.io()).flatMap{
       Log.d("checkLogin Repo2","code ${it.code()}")
       when(it.code())
       {
             200 -> {
                 sharedPref.edit().putString(activity.getString(R.string.saved_jwt), it.body()!!.jwt).apply()
                 Log.d("FirestoreLogin", "Jwt and id = ${it.body()!!}, ")
                 sharedPref.edit().putString(activity.getString(R.string.saved_vendor_id), it.body()!!.id).apply()
                 Single.just(UIState.GoToMainScreen)
             }

             401-> Single.just(UIState.ErrorState("Code: ${it.code()} Invalid Login Credentials"))
           in 402..499-> Single.just(UIState.ErrorState("Code: ${it.code()} "))
           in 500..599 -> Single.just(UIState.ErrorState("Server error"))
           else -> Single.just(UIState.ErrorState("Unknown  error api call"))
       }
     }.doOnError {
       Log.d("Login","error in apicall ${it.printStackTrace()}")} }
    

}






