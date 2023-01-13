package com.example.soeco.ui.carpentry

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
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
import com.example.soeco.data.Models.DB_Models.Deviation_Report_DB
import com.example.soeco.data.Models.DB_Models.Product_DB
import com.example.soeco.data.Models.mongo.Deviation
import com.example.soeco.ui.viewmodels.DevitaionViewmodel
import com.example.soeco.utils.viewModelFactory
import org.bson.types.ObjectId
import java.util.*


class QuestionnaireCarpentry : Fragment() {
    private val DeviationViewmodel by viewModels<DevitaionViewmodel> { viewModelFactory }
    val args: QuestionnaireCarpentryArgs by navArgs()
    private val navigation: NavController by lazy { findNavController() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_questionnaire, container, false)

        val orderNumberTextView : TextView = view.findViewById(R.id.textView_questionnaireOrderNumber)
        val sendDeviationButton : Button = view.findViewById(R.id.button_reportDeviation)
        orderNumberTextView.text = args.orderNumber


        setProductSelector(view)
        setProductText(view)
        setDateListener(view)
        setTimePicker(view)

        // takes back to dashboard
        sendDeviationButton.setOnClickListener {
            val deviation = createDeviation(view)
            Log.e("tag", deviation.orderNumber)
            DeviationViewmodel.addDeviation(deviation)
            DeviationViewmodel.insertDeviation(generateDeviation(view))
            navigation.popBackStack()
        }

        return view
    }

    private fun generateDeviation(view: View): Deviation {
        val timeText: EditText = view.findViewById(R.id.editTextDate_questionnaireTime)
        val dateText : EditText = view.findViewById(R.id.editTextDate_questionnaireDate)
        val problemText : EditText = view.findViewById(R.id.editText_questionnaireProblem)
        val solutionText : EditText = view.findViewById(R.id.editText_questionnaireSolution)
        val costText : EditText = view.findViewById(R.id.editText_questionnaireCost)
        return Deviation(
            ObjectId(),
            owner_id = args.orderNumber,
            date = dateText.text.toString(),
            time = timeText.text.toString(),
            problem = problemText.text.toString(),
            solution = solutionText.text.toString(),
            cost = costText.text.toString()
        )
    }

    private fun createDeviation(view: View): Deviation_Report_DB {
        val timeText: EditText = view.findViewById(R.id.editTextDate_questionnaireTime)
        val dateText : EditText = view.findViewById(R.id.editTextDate_questionnaireDate)
        val problemText : EditText = view.findViewById(R.id.editText_questionnaireProblem)
        val solutionText : EditText = view.findViewById(R.id.editText_questionnaireSolution)
        val costText : EditText = view.findViewById(R.id.editText_questionnaireCost)

        val date = dateText.text.toString()
        val product = getProductSelection(view)
        val time = timeText.text.toString()
        val problem = problemText.text.toString()
        val solution = solutionText.text.toString()
        val cost = costText.text.toString()


        return Deviation_Report_DB(date,time,product?.product_id, problem,solution,cost,args.orderNumber )

    }

    private fun getProductSelection(view: View): Product_DB? {
        val spinner: Spinner = view.findViewById(R.id.spinner_questionnaireProduct)

        val products = DeviationViewmodel.getProducts()
        val selectedIndex = spinner.selectedItemPosition-1
        val product = if (selectedIndex != -1 )  products[spinner.selectedItemPosition-1]
        else Product_DB("empty","empty","empty","0")
        return product
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
        val products = DeviationViewmodel.getProducts()
        val list = mutableListOf(Product_DB("","","","0"))
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


    private fun setDateListener(view: View) {
        val dateText : EditText = view.findViewById(R.id.editTextDate_questionnaireDate)

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