package com.example.soeco.delivery

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.soeco.Api.MainViewModel
import com.example.soeco.R
import java.util.*

class DeliveryOrderDetailFragment : Fragment() {
    @SuppressLint("MissingInflatedId") // TODO: Remove this line

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_delivery_order_detail, container, false)



        // viewmodel
        val viewmodel: MainViewModel by activityViewModels()
        viewmodel.update() // get new orders from api

        val orderNumberTextView : TextView= view.findViewById(R.id.textView_delivery_products_view_order_number)

        // call (dial screen with number displayed)
        val callButton : Button = view.findViewById(R.id.button_delivery_orderCustomerContact)
        val phoneText: TextView = view.findViewById(R.id.textView_delivery_orderCustomerContact)

        // map
        val mapButton : Button = view.findViewById(R.id.button_delivery_orderCustomerAddress)
        val addressText: TextView = view.findViewById(R.id.textView_delivery_orderCustomerAddress)

        val order_id = this.arguments?.getString("order")
        if (order_id != null) {
            viewmodel.setActiveOrder(order_id)
        }
        val order = viewmodel.getOrder()
        if (order != null) {
            phoneText.text= order.contact?.get(0).toString()
            // for phone number:  order.contact?.get(1).toString()

            orderNumberTextView.text= order.OrderNumber
            addressText.text = order.address

        }



        setCallButtonListener(callButton, phoneText)
        setMapButtonListener(mapButton,addressText)

        return view
    }

    private fun setCallButtonListener(callButton: Button, phoneText: TextView) {
        callButton.setOnClickListener{
            val phoneNumber = phoneText.text
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:$phoneNumber")
            startActivity(dialIntent)
        }
    }

    private fun setMapButtonListener(mapButton: Button, mapText: TextView) {
        mapButton.setOnClickListener{
            val address = mapText.text
            val gmmIntentUri =
                Uri.parse("geo:0,0?q=$address")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }

}