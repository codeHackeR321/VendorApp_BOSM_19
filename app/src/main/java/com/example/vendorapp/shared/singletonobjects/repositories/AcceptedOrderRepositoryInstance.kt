package com.example.vendorapp.shared.singletonobjects.repositories

import android.content.Context
import com.example.vendorapp.acceptedorderscreen.model.AcceptedOrderRepository
import com.example.vendorapp.neworderscreen.model.NewOrderRepository

class AcceptedOrderRepositoryInstance {
    companion object{

        var acceptedOrderRepository : AcceptedOrderRepository? = null

        @Synchronized fun getInstance(context: Context) : AcceptedOrderRepository {

            if (acceptedOrderRepository == null)
            {
                acceptedOrderRepository = AcceptedOrderRepository(context)
            }

            return acceptedOrderRepository!!

        }

    }
}