package com.example.vendorapp.shared.singletonobjects.repositories

import android.content.Context
import com.example.vendorapp.menu.model.MenuRepository

class MenuRepositoryInstance  {

    companion object{
        var menuRepository: MenuRepository?=null

        fun getInstance(context: Context):MenuRepository{
            if(menuRepository==null){
                menuRepository= MenuRepository(context)
            }
          return menuRepository!!
        }
    }
}