package com.example.soeco.ui.carpentry

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.soeco.R
import com.example.soeco.ui.base.RoleActivity

class CarpentryActivity: RoleActivity(
    R.layout.activity_carpentry,
    R.id.carpentry_host_fragment,
    R.id.carpentry_nav_view,
    R.id.carpentry_drawer_layout,
    R.id.toolbar
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}