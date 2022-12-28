package com.example.soeco.ui.delivery

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.soeco.R
import com.example.soeco.databinding.FragmentDeliveryOrderDetailBinding
import com.example.soeco.ui.viewmodels.OrderDetailsViewModel
import com.example.soeco.utils.viewModelFactory

class DeliveryOrderDetailFragment : Fragment() {

    val viewmodel by viewModels<OrderDetailsViewModel> { viewModelFactory }

    private lateinit var binding: FragmentDeliveryOrderDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_delivery_order_detail, container, false)

        val orderNumberTextView : TextView= view.findViewById(R.id.textView_delivery_products_view_order_number)

        // call (dial screen with number displayed)
        val callButton : Button = view.findViewById(R.id.button_delivery_orderCustomerContact)
        val phoneText: TextView = view.findViewById(R.id.textView_delivery_orderCustomerContact)

        // map
        val mapButton : Button = view.findViewById(R.id.button_delivery_orderCustomerAddress)
        val addressText: TextView = view.findViewById(R.id.textView_delivery_orderCustomerAddress)

        val order_id = this.arguments?.getString("order")


        val order = order_id?.let { viewmodel.getOrder(it) }
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