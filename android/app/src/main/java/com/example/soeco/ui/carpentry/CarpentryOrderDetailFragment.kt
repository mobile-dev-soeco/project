package com.example.soeco.ui.carpentry

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.soeco.R

import java.util.*

class CarpentryOrderDetailFragment : Fragment() {

    val args: CarpentryOrderDetailFragmentArgs by navArgs()

    private val navigation: NavController by lazy { findNavController() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_carpentry_order_detail, container, false)

        val orderNumberTextView : TextView = view.findViewById(R.id.textView_carpentry_products_view_order_number)
        val viewProductsButton : Button = view.findViewById(R.id.button_carpentry_viewProducts)
        val viewMaterialsButton : Button = view.findViewById(R.id.button_carpentry_viewMaterials)
        val reportDeviationButton: Button = view.findViewById(R.id.button_carpentry_reportDeviation)

        val order_id = args.orderNumber
        orderNumberTextView.text= order_id

        viewProductsButton.setOnClickListener {
            navigation.navigate(R.id.action_carpentryOrderDetailFragment_to_productsList)

        }

        viewMaterialsButton.setOnClickListener {
            navigation.navigate(R.id.action_carpentryOrderDetailFragment_to_carpentryMaterials)

        }


        return view
    }

     private fun setDateButtonListener(dateButton: Button, dateText: TextView) {
         val calendar = Calendar.getInstance()
         val year = calendar.get(Calendar.YEAR)
         val month = calendar.get(Calendar.MONTH)
         val day = calendar.get(Calendar.DAY_OF_MONTH)
         dateText.text = String.format("%d/%d/%d", day, month+1, year)
         dateButton.setOnClickListener {
             val datePickerDialog = activity?.let { it1 ->
                 DatePickerDialog(it1, { _, year, month, dayOfMonth ->
                     dateText.text = String.format("%d/%d/%d", dayOfMonth, month+1, year)
                                       }, year, month, day)
             }
             datePickerDialog?.show()
         }
     }
}