package com.example.soeco

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.soeco.dashboard.DashBoardFragment
import com.example.soeco.dashboard.OrderDetailFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_small_title_layout)
        //  setContentView(R.layout.base_layout)

        val fragmentManager: FragmentManager = supportFragmentManager
            val ft: FragmentTransaction = fragmentManager.beginTransaction()
            ft.add(R.id.card, DashBoardFragment())
            ft.commit()

    }
}