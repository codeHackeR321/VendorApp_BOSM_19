package com.example.vendorapp.loginscreen.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendorapp.loginscreen.view.UIState
import com.example.vendorapp.shared.singletonobjects.repositories.LoginRepositoryInstance
import io.reactivex.android.schedulers.AndroidSchedulers


class LoginViewModel(val context : Context) : ViewModel(){


    var loginRepo = LoginRepositoryInstance.getInstance(context)
    var loginStatus  : LiveData<UIState> = MutableLiveData()


init {

}
    fun getJWT():String{
return loginRepo.getJWTfromSharedPref()
    }
    @SuppressLint("CheckResult")
    fun login(username:String,password:String){

        loginRepo.loginWithAuth(username, password).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Log.d("LoginViewodel7","UiState $it")
            (loginStatus as MutableLiveData).postValue(it)

        },{
            (loginStatus as MutableLiveData).postValue(UIState.ErrorState(it.toString()))
            Log.d("LoginViewodel6","error$it")
        })
    }


}