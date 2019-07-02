package com.example.vendorapp.menu.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.vendorapp.R
import com.example.vendorapp.menu.viewModel.MenuViewModel
import com.example.vendorapp.menu.viewModel.MenuViewModelFactory
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.fragment_fra_new_order.*

class MenuActivity : AppCompatActivity(),MenuAdapter.UpdateMenuListener {
    private lateinit var nMenuViewModel:MenuViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nMenuViewModel=ViewModelProviders.of(this,MenuViewModelFactory(this)).get(MenuViewModel::class.java)
        setContentView(R.layout.activity_menu)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        menu_recycler.adapter=MenuAdapter(this)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
       nMenuViewModel.menuList.observe(this, Observer {menu->
           (menu_recycler.adapter as MenuAdapter).itemList=menu
           (menu_recycler.adapter as MenuAdapter).notifyDataSetChanged()
           if (progBar_menu_screen.isVisible && menu.isNotEmpty()) {
               progBar_menu_screen.visibility = View.INVISIBLE
               window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
           }
       })

        nMenuViewModel.error.observe(this , Observer {
            Toast.makeText(this , it , Toast.LENGTH_LONG).show()
            if (progBar_new_order_screen.isVisible && it.isNotEmpty()) {
                progBar_new_order_screen.visibility = View.INVISIBLE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        })

    }
    override fun onStatusChanged(itemId: String, status: String) {
        nMenuViewModel.updateStatus(itemId,status)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId)
        {
            android.R.id.home ->
                finish()
        }
        return true
    }
}
