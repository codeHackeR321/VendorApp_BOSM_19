package com.example.vendorapp.loginscreen.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendorapp.shared.UIState
import com.example.vendorapp.shared.singletonobjects.repositories.LoginRepositoryInstance
import com.example.vendorapp.shared.utils.NetworkConnectivityCheck
import io.reactivex.android.schedulers.AndroidSchedulers


class LoginViewModel(val context : Context) : ViewModel(){


    var loginRepo = LoginRepositoryInstance.getInstance(context)
    var loginStatus  : LiveData<UIState> = MutableLiveData()


    fun getJWT():String{
        return loginRepo.getJWTfromSharedPref()
    }

    fun getVendorId():String{
        return loginRepo.getVendorIdfromSharedPref()
    }

    fun getFirebaseRegToken():String{
        return loginRepo.getFirebaseRegTokenFromSharedPref()
    }
    fun saveFirebaseRegToken(token: String){
        loginRepo.saveFirebaseRegTokenToSharedPref(token)
    }
    @SuppressLint("CheckResult")
    fun login(username:String,password:String,token:String){

        if (NetworkConnectivityCheck().checkIntenetConnection(context))
        {

            loginRepo.loginWithAuth(username, password,token).observeOn(AndroidSchedulers.mainThread()).subscribe({
                Log.d("LoginViewodel1","UiState $it")
                (loginStatus as MutableLiveData).postValue(it)

            },{
                (loginStatus as MutableLiveData).postValue(UIState.ErrorState("OnError: ${it.message}"))
                Log.d("LoginViewodel2","error$it")
            })

        }
        else{
            Log.d("LoginViewodel3", "No internet connection while Login")
            (loginStatus as MutableLiveData).postValue(UIState.NoInternetConnection)
        }


    }


}