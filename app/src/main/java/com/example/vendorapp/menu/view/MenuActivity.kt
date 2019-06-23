package com.example.vendorapp.menu.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.vendorapp.R
import com.example.vendorapp.menu.viewModel.MenuViewModel
import com.example.vendorapp.menu.viewModel.MenuViewModelFactory
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity(),MenuAdapter.UpdateMenuListener {
    private lateinit var nMenuViewModel:MenuViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nMenuViewModel=ViewModelProviders.of(this,MenuViewModelFactory(this)).get(MenuViewModel::class.java)
        setContentView(R.layout.activity_menu)
        menu_recycler.adapter=MenuAdapter(this)
       nMenuViewModel.menuList.observe(this, Observer {menu->
           (menu_recycler.adapter as MenuAdapter).itemList=menu
           (menu_recycler.adapter as MenuAdapter).notifyDataSetChanged()
       })

    }
    override fun onStatusChanged(itemId: String, status: String) {
        nMenuViewModel.updateStatus(itemId,status)
    }
}
