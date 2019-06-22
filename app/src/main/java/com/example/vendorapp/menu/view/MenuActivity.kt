package com.example.vendorapp.menu.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.example.vendorapp.R
import com.example.vendorapp.menu.viewModel.MenuViewModel

class MenuActivity : AppCompatActivity() {
    private lateinit var nMenuViewModel:MenuViewModel
    private lateinit var menuAdapter: MenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nMenuViewModel=ViewModelProviders.of(this).get(MenuViewModel::class.java)
        setContentView(R.layout.activity_menu)
       
    }
}
