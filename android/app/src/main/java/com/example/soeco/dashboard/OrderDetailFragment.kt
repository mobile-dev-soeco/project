package com.example.soeco.dashboard

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.soeco.R
import java.util.*
/*
class OrderDetailFragment : Fragment() {
    @SuppressLint("MissingInflatedId") // TODO: Remove this line

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_detail, container, false)
        // calendar
        val dateButton: Button = view.findViewById(R.id.button_modifyDate)
        val dateText: TextView = view.findViewById(R.id.textView_date)
        setDateButtonListener(dateButton,dateText)

        //TODO change visibility for this page according to the role of the user
        // Only delivery shall have the call and map functions, they should not have time worked, complete the delivery too?
        // Only in house workers should have materials list

        // call (dial screen with number displayed)
        val callButton : Button = view.findViewById(R.id.button_orderCustomerContact)
        val phoneText: TextView = view.findViewById(R.id.textView_orderCustomerContact)
        setCallButtonListener(callButton, phoneText)

        // map
        val mapButton : Button = view.findViewById(R.id.button_orderCustomerAddress)
        val addressText: TextView = view.findViewById(R.id.textView_orderCustomerAddress)
        setMapButtonListener(mapButton,addressText)

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
                DatePickerDialog(it1, { view, year, month, dayOfMonth ->
                    dateText.text = String.format("%d/%d/%d", dayOfMonth, month+1, year)
                }, year, month, day)
            }
            datePickerDialog?.show()
        }
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
        //val address = "Kanalgatan 2 Kristianstad"
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

 */