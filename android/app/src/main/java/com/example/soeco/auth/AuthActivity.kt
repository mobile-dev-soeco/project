package com.example.soeco.auth

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import com.example.soeco.realmAppServices
import com.example.soeco.admin.AdminActivity

class AuthActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = realmAppServices.currentUser()

        if (user == null) {
            startActivity(Intent(application, LoginActivity::class.java))
        }
        else {
            // Get users role
            val userRole = user.customData["role"].toString()
            // Navigate user depending on their role
            when(userRole) {
                "admin" -> startActivity(Intent(application, AdminActivity::class.java))

                /* TODO -> Add your activity here depending on what role is met
                *   The available roles at the moment are
                *   - admin
                *   - delivery
                *   - carpenter
                *   - fabricator
                *   fabricator == blacksmith
                *   It will be in the same form as above as shown below
                *   "delivery" -> startActivity(Intent(application, DeliveryActivity::class.java))
                * */

                else -> startActivity(Intent(application, LoginActivity::class.java))
            }
        }
        finish()
    }
}