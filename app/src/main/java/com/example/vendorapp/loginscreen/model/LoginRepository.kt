package com.example.vendorapp.loginscreen.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vendorapp.R
import com.example.vendorapp.loginscreen.view.UIState
import com.example.vendorapp.shared.singletonobjects.RetrofitInstance
import com.google.gson.JsonObject
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeoutException

class LoginRepository(val activity: Context) {

    //  private val menuDao: MenuDao
    private val loginApiCall=RetrofitInstance.getRetroInstance()
    private val sharedPref = activity.getSharedPreferences(
        activity.getString(R.string.preference_file_login), Context.MODE_PRIVATE
    )
   private val defaultJWTValue = activity.getString(R.string.default_jwt_value)
    var loginStatusRepo  : LiveData<String> = MutableLiveData()





    fun getJWTfromSharedPref():String {
        val jwt_token = sharedPref.getString(activity.getString(R.string.saved_jwt), defaultJWTValue)
   return jwt_token!!
    }


fun loginWithAuth(username: String, password: String): Single<UIState>{
        val body = JsonObject().also {
            it.addProperty("username", username)
            it.addProperty("password",  password)
        }

    Log.d("checkLogin Repo","ij,jhvhj")
var x = activity.getString(R.string.login_failed)
   return loginApiCall.getJWTfromAuth(body).subscribeOn(Schedulers.io()).flatMap{
       Log.d("checkLogin Repo2","code ${it.code()}")
       when(it.code())
       {

             200 -> {
                 sharedPref.edit().putString(activity.getString(R.string.saved_jwt), it.body()!!.jwt).apply()
                 Log.d("FirestoreLogin", "Jwt = ${it.body()!!.jwt}")
                 sharedPref.edit().putString(activity.getString(R.string.saved_vendor_id), it.body()!!.id).apply()
                 Single.just(UIState.GoToMainScreen)
             }

           in 400..499-> Single.just(UIState.ErrorState("login error if 401 code: ${it.code()}"))

           in 500..599 -> Single.just(UIState.ErrorState("Server error"))

           else -> Single.just(UIState.ErrorState("Unknown  error"))


       }
     }.doOnError {}.flatMap {
       Single.just(UIState.ErrorState("Unknown  error"))

   }





    }










}