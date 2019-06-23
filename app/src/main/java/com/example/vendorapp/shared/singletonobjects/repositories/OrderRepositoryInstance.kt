package com.example.vendorapp.shared.singletonobjects.repositories

import android.content.Context
import com.example.vendorapp.shared.singletonobjects.model.OrderRepository

class OrderRepositoryInstance {

    companion object{

        var orderRepository : OrderRepository? = null

        @Synchronized fun getInstance(context: Context) : OrderRepository {

            if (orderRepository == null)
            {
                orderRepository = OrderRepository(context)
            }

            return orderRepository!!

        }

    }
}