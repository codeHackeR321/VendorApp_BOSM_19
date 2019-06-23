package com.example.vendorapp.menu.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.vendorapp.menu.model.MenuRepository
import com.example.vendorapp.shared.singletonobjects.repositories.MenuRepositoryInstance
import com.example.vendorapp.shared.singletonobjects.repositories.MenuRepositoryInstance.Companion.menuRepository

class MenuViewModel(context: Context) :ViewModel() {

    var order:LiveData<List<>>
    var menuRepository:MenuRepository=MenuRepositoryInstance.getInstance(context)
    init {
        menuRepository.getMenuRoom()
    }
}