package com.example.soeco.ui.carpentry.reports

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.soeco.R
import com.example.soeco.databinding.FragmentTimeReportBinding
import com.example.soeco.ui.carpentry.CarpentryViewModel
import com.example.soeco.utils.ActionResult
import com.example.soeco.utils.viewModelFactory
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

class TimeReportFragment : Fragment() {

    private lateinit var binding: FragmentTimeReportBinding
    private lateinit var carpentryViewModel: CarpentryViewModel
    private val navigation by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            handleSubmit()
        }

        carpentryViewModel.isLoading.observe(viewLifecycleOwner, ::handleLoading)

        carpentryViewModel.insertReportResult.observe(viewLifecycleOwner, ::handleInsertResult)

        initDateListener(binding.calender)
    }

    private fun initDateListener(dateText: EditText) {
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
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        dateText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) dateText.performClick()
        }
    }

    private fun handleSubmit() {
        // Get selected date
        val formatter = DateTimeFormatter.ofPattern("dd/M/yyyy")
        val date = LocalDate.parse(binding.calender.text.toString(), formatter)
        // Get current time
        val currentTime = Instant.now().toString().split("T")[1]
        // Return in Date format
        val dateTime = Date.from(Instant.parse("${date}T${currentTime}"))
        // Add time report to Database
        carpentryViewModel.insertTimeReport(
            dateTime,
            binding.npHours.value,
            binding.npMinutes.value
        )
    }

    private fun handleLoading(loading: Boolean){
        if (loading) {
            binding.pbSpinner.visibility = View.VISIBLE
            binding.btnSubmit.visibility = View.INVISIBLE
        } else {
            binding.pbSpinner.visibility = View.INVISIBLE
            binding.btnSubmit.visibility = View.VISIBLE
        }
    }

    private fun handleInsertResult(result: ActionResult) {
        when(result){
            is ActionResult.Success -> {
                navigation.navigate(R.id.action_timeReportFragment_to_productDetails)
                carpentryViewModel.clearResult()
            }
            is ActionResult.Error -> {
                Toast.makeText(context, getString(R.string.report_insert_error_msg), Toast.LENGTH_SHORT).show()
                carpentryViewModel.clearResult()
            }
            else -> {}
        }
    }
}