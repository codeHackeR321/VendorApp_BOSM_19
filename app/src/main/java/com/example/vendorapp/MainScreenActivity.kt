package com.example.vendorapp

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.graphics.Color
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.vendorapp.acceptedorderscreen.view.AcceptedOrderFragment
import com.example.vendorapp.completedorderscreen.view.CompletedOrdersActivity
import com.example.vendorapp.loginscreen.view.MainActivity
import com.example.vendorapp.menu.view.MenuActivity
import com.example.vendorapp.neworderscreen.view.NewOrderFragment
import com.example.vendorapp.shared.Listeners.NetConnectionChanged
import com.example.vendorapp.shared.utils.NetworkReceiver
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main_screen.*

class MainScreenActivity : AppCompatActivity(),NetConnectionChanged {

    private lateinit var viewPager : ViewPager
    private lateinit var viewPagerAdapter : FragmentPagerAdapter
   private lateinit  var sharedPref:SharedPreferences
    override fun buttonClicked(status: String) {
        if(status=="wifi"||status=="data")
        {
            val snackbar= Snackbar.make(coordinator,"Back Online",Snackbar.LENGTH_LONG)
            snackbar.getView().setBackgroundColor(resources.getColor(R.color.green))

            snackbar.show()

        }
        else
            Snackbar.make(coordinator,status,Snackbar.LENGTH_INDEFINITE).setBehavior(NOSwipe()).show()

    }

    // The BroadcastReceiver that tracks network connectivity changes.
    private lateinit var receiver: NetworkReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        // recive jwt via intent
        sharedPref = this.getSharedPreferences(
            this.getString(R.string.preference_file_login), Context.MODE_PRIVATE
        )
        /*#5B7DF3*/
        viewPager = viewPager_activity_mainScreen
        viewPager.adapter = MyPagerAdapter()
        tabLayout_activity_mainScreen.setBackgroundColor(resources.getColor(R.color.tablayout_back))
        tabLayout_activity_mainScreen.setTabTextColors(resources.getColor(R.color.tab_layout_unselected), resources.getColor(R.color.tab_layout_selected));
        tabLayout_activity_mainScreen.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"))

        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        receiver = NetworkReceiver(this)
        this.registerReceiver(receiver, filter)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.overflow_menu , menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.overflowMenu_toogle_availiblity ->
                startActivity(Intent(this , MenuActivity::class.java))
            R.id.overflowMenu_earnings ->
                startActivity(Intent(this,CompletedOrdersActivity::class.java))

            R.id.overflowMenu_contactUs ->
                Toast.makeText(this , "Contact Us Activity" , Toast.LENGTH_LONG).show()
            R.id.overflowMenu_logout ->
            {
                //add alert dialog box
               sharedPref.edit().clear().apply()
                startActivity(Intent(this@MainScreenActivity, MainActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class MyPagerAdapter : FragmentPagerAdapter(supportFragmentManager) {

        override fun getItem(position: Int): Fragment {
            when(position){
                0 ->
                    return NewOrderFragment()
                1 ->
                    return AcceptedOrderFragment()
            }
            return NewOrderFragment()
        }

        override fun getCount(): Int {
            return resources.getString(R.string.number_of_pages_viewPger).toInt()
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when(position){
                0 ->
                    return resources.getString(R.string.heading_new_order_tab)
                1 ->
                    return resources.getString(R.string.heading_accepted_order_tab)
            }
            return super.getPageTitle(position)
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        // Unregisters BroadcastReceiver when app is destroyed.
        this.unregisterReceiver(receiver)
    }

    class NOSwipe : BaseTransientBottomBar.Behavior(){
        override fun canSwipeDismissView(child: View): Boolean {
            return false
        }

    }
}
