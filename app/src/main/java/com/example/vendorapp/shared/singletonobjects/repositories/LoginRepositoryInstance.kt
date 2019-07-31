package com.example.vendorapp.shared.singletonobjects.repositories

import android.content.Context
import com.example.vendorapp.loginscreen.model.LoginRepository
import com.example.vendorapp.shared.singletonobjects.model.OrderRepository

class LoginRepositoryInstance {

    companion object{

        var loginRepository : LoginRepository? = null

        @Synchronized fun getInstance(context: Context) : LoginRepository {

            if (loginRepository == null)
            {
                loginRepository = LoginRepository(context)
            }

            return loginRepository!!

        }

    }
}