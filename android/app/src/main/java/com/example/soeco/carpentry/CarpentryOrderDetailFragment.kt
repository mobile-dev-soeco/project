package com.example.soeco.carpentry

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.soeco.MainActivity
import com.example.soeco.R
import java.util.*

class CarpentryOrderDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_carpentry_order_detail, container, false)

        val orderNumberTextView : TextView = view.findViewById(R.id.textView_carpentry_products_view_order_number)
        val viewProductsButton : Button = view.findViewById(R.id.button_carpentry_viewProducts)
        val viewMaterialsButton : Button = view.findViewById(R.id.button_carpentry_viewMaterials)
        val reportDeviationButton: Button = view.findViewById(R.id.button_carpentry_reportDeviation)

        viewProductsButton.setOnClickListener{view->
            val intent =
            Intent(view.context, MainActivity::class.java) // TODO Change to PRODUCTS FRAGMENT
            intent.putExtra("orderNumber", orderNumberTextView.text)
            view.context.startActivity(intent)
        }

        viewMaterialsButton.setOnClickListener{view->
            val intent =
                Intent(view.context, MainActivity::class.java) // TODO Change to MATERIALS FRAGMENT
            intent.putExtra("orderNumber", orderNumberTextView.text)
            view.context.startActivity(intent)
        }

        reportDeviationButton.setOnClickListener{view->
            val intent =
                Intent(view.context, MainActivity::class.java) // TODO Change to DEVIATION FRAGMENT
            intent.putExtra("orderNumber", orderNumberTextView.text)
            view.context.startActivity(intent)
        }

        return view
    }

}