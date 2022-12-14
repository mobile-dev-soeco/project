package com.example.soeco.dashboard

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.soeco.R
import java.time.Year
import java.util.Calendar

class OrderDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_order_detail, container, false)
        val dateButton: Button = view.findViewById(R.id.button_modifyDate)
        val dateText: TextView = view.findViewById(R.id.textView_date)
        setDateButtonListener(dateButton,dateText)

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
}