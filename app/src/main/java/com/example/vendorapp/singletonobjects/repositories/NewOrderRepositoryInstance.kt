package com.example.vendorapp.singletonobjects.repositories

import android.app.Application
import com.example.vendorapp.neworderscreen.model.NewOrderRepository

class NewOrderRepositoryInstance {

    companion object{

        var newOrderRepository : NewOrderRepository? = null

        @Synchronized fun getInstance(application: Application) : NewOrderRepository{

            if (newOrderRepository == null)
            {
                newOrderRepository = NewOrderRepository(application)
            }

            return newOrderRepository!!

        }

    }

}