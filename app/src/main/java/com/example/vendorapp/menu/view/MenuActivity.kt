package com.example.vendorapp.menu.view

import android.annotation.SuppressLint
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
import com.example.vendorapp.menu.model.MenuStatus
import com.example.vendorapp.menu.viewModel.MenuViewModel
import com.example.vendorapp.menu.viewModel.MenuViewModelFactory
import com.example.vendorapp.shared.UIState
import com.example.vendorapp.shared.dataclasses.roomClasses.MenuItemData
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.fragment_fra_new_order.*

class MenuActivity : AppCompatActivity(),MenuAdapter.UpdateMenuListener {
    private lateinit var nMenuViewModel:MenuViewModel
    var newStatusItemList= mutableListOf<MenuItemData>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nMenuViewModel=ViewModelProviders.of(this,MenuViewModelFactory(this)).get(MenuViewModel::class.java)
        setContentView(R.layout.activity_menu)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        menu_recycler.adapter=MenuAdapter(this)
        showLoadingStateActivity()
        observeUIState()

        nMenuViewModel.menuList.observe(this, Observer {menu->
            (menu_recycler.adapter as MenuAdapter).itemList=menu

            (menu_recycler.adapter as MenuAdapter).notifyDataSetChanged()
           removeLoadingStateActivity()
        })

        nMenuViewModel.error.observe(this , Observer {
            Toast.makeText(this , it , Toast.LENGTH_LONG).show()
           removeLoadingStateActivity()
        })

        saveChanges.setOnClickListener{

                showLoadingStateActivity()

                if(newStatusItemList.isEmpty())
                {
                    Toast.makeText(this,"No changes made to be saved",Toast.LENGTH_SHORT).show()
                    removeLoadingStateActivity()
                }

                else
                    nMenuViewModel.updateStatus(newStatusItemList)
            }
    }

    override fun onStatusChanged(item: MenuItemData, newStatus: Int) {
        Log.d("Listener", "Entered Listener")
        item.status=newStatus
        val position=newStatusItemList.indexOfFirst { it.itemId==item.itemId }
        if (position!=-1)
        {
            newStatusItemList.removeAt(position)
        }
        else
        {
            newStatusItemList.add(item)
        }

        if (newStatusItemList.isEmpty())
             saveChanges.setTextColor(resources.getColor(R.color.tab_layout_unselected))
        else
            saveChanges.setTextColor(resources.getColor(R.color.tab_layout_selected))

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId)
        {
            android.R.id.home ->
                finish()
        }
        return true
    }

    @SuppressLint("CheckResult")
    private fun observeUIState() {

        nMenuViewModel.observeUIState().observeOn(AndroidSchedulers.mainThread()).subscribe({
            Log.d("Firestore78", "in Neworderfrag ui state$it")
            when (it!!) {
                UIState.ShowLoadingState -> {
                    //to be enabled in group view holder
                    // progBar_new_order_screen.visibility = View.VISIBLE
                    // not yet decided for group view holder
                    Log.d("Firestore76", "in MenuActivity ui loading state")

                }


                is UIState.ErrorStateChangeStatus->{
                    removeLoadingStateActivity()
                    saveChanges.setTextColor(resources.getColor(R.color.tab_layout_selected))

                    Log.d("MenuActivity1","Error Change Status:${(it as UIState.ErrorStateChangeStatus).message}")
                    Toast.makeText(this,"Error: Try Again After SomeTime${(it as UIState.ErrorStateChangeStatus).message}",Toast.LENGTH_LONG).show()

                }

                is UIState.SuccessStateChangeStatus->{
                    removeLoadingStateActivity()

                    newStatusItemList.clear()
                    saveChanges.setTextColor(resources.getColor(R.color.tab_layout_unselected))
                    Log.d("MenuActivity2","Success Change Status:${(it as UIState.SuccessStateChangeStatus).message}")
                    Toast.makeText(this,"Success change status${(it as UIState.SuccessStateChangeStatus).message}",Toast.LENGTH_LONG).show()

                }
            }

        }, {
            Log.d("MenuActivity9", "observe observe ui state  MenUAcrtivity$it")
            Toast.makeText(this,"Error observing UI State menuActivity$it",Toast.LENGTH_LONG).show()
        })
    }

    /**
     * This method enables the Progress Bar and makes disables the screen
     * */
    private fun showLoadingStateActivity() {
        saveChanges.isClickable = false
        if (!prog_bar_menu_activity.isVisible) {
            prog_bar_menu_activity.visibility = View.VISIBLE
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }
    }

    /**
     * This method removes the Progress Bar and re-enables the screen
     **/
    private fun removeLoadingStateActivity() {
        saveChanges.isClickable = true
        if (prog_bar_menu_activity.isVisible) {
            prog_bar_menu_activity.visibility = View.INVISIBLE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }
}