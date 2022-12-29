package com.example.soeco.ui.delivery

import android.os.Bundle
import com.example.soeco.R
import com.example.soeco.ui.base.RoleActivity

class DeliveryActivity: RoleActivity(
    R.layout.activity_delivery,
    R.id.delivery_host_fragment,
    R.id.delivery_nav_view,
    R.id.delivery_drawer_layout
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}