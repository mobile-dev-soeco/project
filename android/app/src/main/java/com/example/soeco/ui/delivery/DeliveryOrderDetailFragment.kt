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
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.soeco.R
import com.example.soeco.data.Models.DB_Models.Order_DB
import com.example.soeco.databinding.FragmentDeliveryOrderDetailBinding
import com.example.soeco.ui.viewmodels.OrderDetailsViewModel
import com.example.soeco.utils.viewModelFactory

class DeliveryOrderDetailFragment : Fragment() {


    val args: DeliveryOrderDetailFragmentArgs by navArgs()
    private val navigation: NavController by lazy { findNavController() }
    private val OrderDetailsViewModel by viewModels<OrderDetailsViewModel> { viewModelFactory }

    private lateinit var binding: FragmentDeliveryOrderDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_delivery_order_detail, container, false)
        OrderDetailsViewModel.updateProducts(args.orderNumber)

        val orderNumberTextView : TextView = view.findViewById(R.id.textView_delivery_products_view_order_number)
        val customerName : TextView = view.findViewById(R.id.textView_delivery_orderName)

        val showProductsButton : Button = view.findViewById(R.id.button_delivery_viewProducts)

        // call (dial screen with number displayed)
        val callButton : Button = view.findViewById(R.id.button_delivery_orderCustomerContact)
        val phoneText: TextView = view.findViewById(R.id.textView_delivery_orderCustomerContact)
        // map
        val mapButton : Button = view.findViewById(R.id.button_delivery_orderCustomerAddress)
        val addressText: TextView = view.findViewById(R.id.textView_delivery_orderCustomerAddress)

        val reportButton : Button = view.findViewById(R.id.button_delivery_reportDeviation)
        val order = OrderDetailsViewModel.getOrder(args.orderNumber)
        orderNumberTextView.text = args.orderNumber
        customerName.text = "Andreas"
        phoneText.text = order?.phone
        addressText.text = order?.address

        setCallButtonListener(callButton, phoneText)
        setMapButtonListener(mapButton,order)

        showProductsButton.setOnClickListener {
            val action = DeliveryOrderDetailFragmentDirections.actionDeliveryOrderDetailFragmentToDeliveryProductsList(args.orderNumber)
            navigation.navigate(action)
        }

        reportButton.setOnClickListener {
            val action = DeliveryOrderDetailFragmentDirections.actionDeliveryOrderDetailFragmentToQuestionnaireCarpentry(args.orderNumber)
            navigation.navigate(action)
        }

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

    private fun setMapButtonListener(mapButton: Button, order: Order_DB?,) {
        mapButton.setOnClickListener{
            val address = order?.address
            val city = order?.city
            val gmmIntentUri =
                Uri.parse("geo:0,0?q=$address,$city")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }

}