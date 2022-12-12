package com.example.soeco

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.soeco.dashboard.DashBoardFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_layout)
        //  setContentView(R.layout.base_layout)

        val fragmentManager: FragmentManager = supportFragmentManager
        var fragment = fragmentManager.findFragmentByTag("fragment_dash")
        if (fragment == null) {
            val ft: FragmentTransaction = fragmentManager.beginTransaction()
            fragment = DashBoardFragment()
            ft.add(R.id.card, fragment, "fragment_dash")
            ft.commit()
        }
    }
}