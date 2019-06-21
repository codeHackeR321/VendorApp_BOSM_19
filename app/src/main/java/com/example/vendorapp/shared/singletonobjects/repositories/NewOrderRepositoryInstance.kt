package com.example.vendorapp.shared.singletonobjects.repositories

import android.app.Application
import android.content.Context
import com.example.vendorapp.neworderscreen.model.NewOrderRepository

class NewOrderRepositoryInstance {

    companion object{

        var newOrderRepository : NewOrderRepository? = null

        @Synchronized fun getInstance(context: Context) : NewOrderRepository{

            if (newOrderRepository == null)
            {
                newOrderRepository = NewOrderRepository(context)
            }

            return newOrderRepository!!

        }

    }

}