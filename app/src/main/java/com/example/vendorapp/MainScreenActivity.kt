package com.example.vendorapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.vendorapp.acceptedorderscreen.view.AcceptedOrderFragment
import com.example.vendorapp.completedorderscreen.view.CompletedOrdersActivity
import com.example.vendorapp.menu.view.MenuActivity
import com.example.vendorapp.neworderscreen.view.NewOrderFragment
import kotlinx.android.synthetic.main.activity_main_screen.*

class MainScreenActivity : AppCompatActivity() {

    private lateinit var viewPager : ViewPager
    private lateinit var viewPagerAdapter : FragmentPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        // recive jwt via intent

        viewPager = viewPager_activity_mainScreen
        viewPager.adapter = MyPagerAdapter()
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
                Toast.makeText(this , "Logout" , Toast.LENGTH_LONG).show()
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
}
