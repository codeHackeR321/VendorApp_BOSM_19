package com.example.vendorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.vendorapp.acceptedorderscreen.view.AcceptedOrderFragment
import com.example.vendorapp.neworderscreen.view.NewOrderFragment
import kotlinx.android.synthetic.main.activity_main_screen.*

class MainScreenActivity : AppCompatActivity() {

    private lateinit var viewPager : ViewPager
    private lateinit var viewPagerAdapter : FragmentPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        viewPager = viewPager_activity_mainScreen
        viewPager.adapter = MyPagerAdapter()
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
