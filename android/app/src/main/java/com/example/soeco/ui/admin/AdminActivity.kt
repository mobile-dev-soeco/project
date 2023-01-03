package com.example.soeco.ui.admin

import android.os.Bundle
import com.example.soeco.R
import com.example.soeco.ui.base.RoleActivity

class AdminActivity: RoleActivity(
    R.layout.activity_admin, // The activity layout resource file
    R.id.admin_host_fragment, // The _id of the Fragment container view in the layout resource file
    R.id.admin_nav_view, // The Id of the Navigation view in the layout resource file
    R.id.admin_drawer_layout, // The _id of the Drawer layout in the layout resource file
    R.id.admin_toolbar
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}