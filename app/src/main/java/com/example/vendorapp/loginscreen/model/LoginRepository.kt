package com.example.vendorapp.loginscreen.model

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.CheckResult
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vendorapp.R
import com.example.vendorapp.loginscreen.view.LoginUIState
import com.example.vendorapp.menu.model.room.MenuDao
import com.example.vendorapp.shared.dataclasses.retroClasses.MenuPojo
import com.example.vendorapp.shared.dataclasses.roomClasses.MenuItemData
import com.example.vendorapp.shared.singletonobjects.RetrofitInstance
import com.example.vendorapp.shared.singletonobjects.VendorDatabase
import com.google.gson.JsonObject
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_fra_new_order.*
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.http.Body

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


fun loginWithAuth(username: String, password: String): Single<LoginUIState>{
        val body = JsonObject().also {
            it.addProperty("username", username)
            it.addProperty("password",  password)
        }

    Log.d("checkLogin Repo","ij,jhvhj")
var x = activity.getString(R.string.login_failed)
   return loginApiCall.getJWTfromAuth(body).subscribeOn(Schedulers.io()).flatMap{
       when(it.code())
       {
             200 -> {
                 sharedPref.edit().putString(activity.getString(R.string.saved_jwt), it.body()!!.jwt).apply()
                 sharedPref.edit().putString(activity.getString(R.string.saved_vendor_id), it.body()!!.id).apply()
                 Single.just(LoginUIState.GoToMainScreen)
             }

           in 400..499-> Single.just(LoginUIState.ErrorState(it.body()!!.message))

           in 500..599 -> Single.just(LoginUIState.ErrorState("Server error"))

           else -> Single.just(LoginUIState.ErrorState("Unknown  error"))


       }
     }
         /*doOnSuccess{

            Log.d("LoginViewModel5","hd${it.body()} code ${it.code()} boo ${it.isSuccessful()}" +
                    "mess  ${it.message()} errorBody${it.errorBody()}" )
          if (it.isSuccessful())
          {
              x=activity.getString(R.string.login_sucess)
          }
          else if (it.code()>= 400 &&it.code() < 500)
          {
              x=it.message()
          }
          else if (it.code()>=500)
          {
              x=activity.getString(R.string.login_server_error)
          }

        }.doOnError {
          x= "Try Again $it"
            Log.d("checkLogin Repo","ijfdeg${it.message}")
        }*///.ignoreElement()



    }










}