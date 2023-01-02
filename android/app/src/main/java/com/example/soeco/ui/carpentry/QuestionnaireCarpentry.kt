package com.example.soeco.ui.carpentry

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.soeco.R
import com.example.soeco.data.Models.DB_Models.Product_DB
import com.example.soeco.ui.viewmodels.ProductsViewModel
import com.example.soeco.utils.viewModelFactory
import java.util.*


class QuestionnaireCarpentry : Fragment() {
    private val productsListViewModel by viewModels<ProductsViewModel> { viewModelFactory }
    val args: QuestionnaireCarpentryArgs by navArgs()
    private val navigation: NavController by lazy { findNavController() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_questionnaire, container, false)

        val orderNumberTextView : TextView = view.findViewById(R.id.textView_questionnaireOrderNumber)
        val sendDeviationButton : Button = view.findViewById(R.id.button_reportDeviation)
        val dateText : EditText = view.findViewById(R.id.editTextDate_questionnaireDate)
        orderNumberTextView.text = args.orderNumber


        setProductSelector(view)
        setProductText(view)
        setDateListener(dateText)
        setTimePicker(view)

        // takes back to dashboard
        sendDeviationButton.setOnClickListener {
            val action = QuestionnaireCarpentryDirections.actionQuestionnaireCarpentryToDashBoardFragment()
            navigation.navigate(action)
        }

        return view
    }

    private fun setTimePicker(view :View){
        val timeText: EditText = view.findViewById(R.id.editTextDate_questionnaireTime)
        timeText.keyListener = null

        val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                val newTime = "$hourOfDay timmar :  $minute minuter"
                timeText.setText(newTime)
            }


        timeText.setOnClickListener {
            val timePicker: TimePickerDialog =
                TimePickerDialog(view.context, timePickerDialogListener, 0, 0, true)
            timePicker.show()
            }
        timeText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) timeText.performClick()

        }
    }


    private fun setProductText(view :View) {
        val spinner: Spinner = view.findViewById(R.id.spinner_questionnaireProduct)
        val productText: EditText = view.findViewById(R.id.editText_questionnaireProduct)
        productText.keyListener = null

        productText.setOnClickListener {
            productText.visibility = View.GONE
            spinner.visibility = View.VISIBLE
            spinner.performClick()
        }
        productText.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) productText.performClick()

        }



    }

    private fun setProductSelector(view :View) {
        val spinner: Spinner = view.findViewById(R.id.spinner_questionnaireProduct)
        val products = productsListViewModel.getProducts(args.orderNumber)
        val list = mutableListOf<Product_DB>(Product_DB("","","",0))
        list.addAll(products)
        val dataAdapter: ArrayAdapter<Product_DB> =
            ArrayAdapter<Product_DB>(view.context, android.R.layout.simple_spinner_item, list)
        spinner.adapter = dataAdapter

        val productText: EditText = view.findViewById(R.id.editText_questionnaireProduct)

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                productText.setText(spinner.selectedItem.toString())
                productText.visibility = View.VISIBLE
                spinner.visibility = View.GONE

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                productText.setText("")
                productText.visibility = View.VISIBLE
                spinner.visibility = View.GONE
            }
        }


    }


    private fun setDateListener(dateText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        dateText.setText(String.format("%d/%d/%d", day, month+1, year))
        dateText.keyListener=null
        dateText.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireActivity(), { _, year, month, dayOfMonth ->
                dateText.setText(  String.format("%d/%d/%d", dayOfMonth, month+1, year))
            }, year, month, day)
            datePickerDialog?.show()
        }
        dateText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) dateText.performClick()

        }
    }
}