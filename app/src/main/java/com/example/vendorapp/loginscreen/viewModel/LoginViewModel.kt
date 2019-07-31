package com.example.vendorapp.loginscreen.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendorapp.loginscreen.view.LoginUIState
import com.example.vendorapp.shared.singletonobjects.repositories.LoginRepositoryInstance
import io.reactivex.schedulers.Schedulers
import kotlin.math.log


class LoginViewModel(val context : Context) : ViewModel(){


    var loginRepo = LoginRepositoryInstance.getInstance(context)
    var loginStatus  : LiveData<LoginUIState> = MutableLiveData()


init {

}
    fun getJWT():String{
return loginRepo.getJWTfromSharedPref()
    }
    @SuppressLint("CheckResult")
    fun login(username:String,password:String){
       // (loginStatus as MutableLiveData<String>).postValue(loginRepo.loginStatusRepo)
loginRepo.loginWithAuth(username, password).subscribe({
    (loginStatus as MutableLiveData).postValue(it)

},{
    Log.d("LoginViewodel6","error$it")
})
    }

}