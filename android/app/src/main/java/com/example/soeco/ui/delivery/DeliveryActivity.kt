package com.example.soeco.ui.delivery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.soeco.R
import com.example.soeco.ui.base.RoleActivity

class DeliveryActivity : RoleActivity (
    R.layout.activity_carpentry,
    R.id.carpentry_host_fragment,
    R.id.carpentry_nav_view,
    R.id.carpentry_drawer_layout){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}