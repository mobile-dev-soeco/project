package com.example.soeco

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.soeco.carpentry.CarpentryOrderDetailFragment
import com.example.soeco.dashboard.DashBoardFragment
import com.example.soeco.delivery.DeliveryOrderDetailFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.small_title_activity_layout)
        //setContentView(R.layout.base_layout)
        //setContentView(R.layout.app_intro)

        val fragmentManager: FragmentManager = supportFragmentManager
        //var fragment = fragmentManager.findFragmentByTag("fragment_dash")
        //var fragment = fragmentManager.findFragmentByTag("fragment_instruction")
        //var fragment = fragmentManager.findFragmentByTag("fragment_order_detail")
        //var fragment = fragmentManager.findFragmentByTag("fragment_company_info")
        //var fragment = fragmentManager.findFragmentByTag("fragment_delivery_order_detail")
        val ft: FragmentTransaction = fragmentManager.beginTransaction()
            //fragment = CompanyInfoFragment()
            //fragment = InstructionFragment()
        var fragment = DashBoardFragment()
            //fragment = OrderDetailFragment()
            //var fragment = DeliveryOrderDetailFragment()
            //fragment = CarpentryOrderDetailFragment()

            //ft.add(R.id.card, fragment, "fragment_delivery_order_detail")
        ft.add(R.id.card, fragment)
            //ft.add(R.id.card, fragment, "fragment_dash")
            //ft.add(R.id.card, fragment, "fragment_instruction")
            //ft.add(R.id.card, fragment, "fragment_order_detail")
            //ft.add(R.id.card, fragment, "fragment_company_info")
        ft.commit()
        }

    }
