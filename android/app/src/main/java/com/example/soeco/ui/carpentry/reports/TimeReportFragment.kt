package com.example.soeco.ui.carpentry.reports

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.soeco.databinding.FragmentTimeReportBinding
import com.example.soeco.ui.carpentry.CarpentryViewModel
import com.example.soeco.utils.viewModelFactory
import java.util.*

class TimeReportFragment : Fragment() {

    private lateinit var binding: FragmentTimeReportBinding
    private lateinit var carpentryViewModel: CarpentryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentTimeReportBinding.inflate(inflater, container, false)
            .also { this.binding = it }
            .also { carpentryViewModel = ViewModelProvider(requireActivity(), viewModelFactory)[CarpentryViewModel::class.java] }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.npHours.minValue = 0
        binding.npHours.maxValue = 12
        binding.npMinutes.minValue = 0
        binding.npMinutes.maxValue = 59

        binding.btnSubmit.setOnClickListener {
            val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val date = dateFormatter.parse(binding.calender.text.toString())
            carpentryViewModel.insertTimeReport(
                date,
                binding.npHours.value,
                binding.npMinutes.value
            )
        }
        setDateListener(binding.calender)
    }

    private fun setDateListener(dateText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        dateText.setText(String.format("%d/%d/%d", day, month + 1, year))
        dateText.keyListener = null
        dateText.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                dateText.setText(String.format("%d/%d/%d", dayOfMonth, month + 1, year))
            }, year, month, day)
            datePickerDialog.show()
        }
        dateText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) dateText.performClick()
        }
    }
}