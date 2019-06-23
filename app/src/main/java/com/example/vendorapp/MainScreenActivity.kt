package com.example.vendorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.vendorapp.neworderscreen.view.NewOrderFragment
import kotlinx.android.synthetic.main.activity_main_screen.*

class MainScreenActivity : AppCompatActivity() {

    private lateinit var viewPager : ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
    }
}
