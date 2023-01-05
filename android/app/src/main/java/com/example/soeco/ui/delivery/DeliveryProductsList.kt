package com.example.soeco.ui.delivery

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soeco.R
import com.example.soeco.data.Models.DB_Models.Delivery_Report_DB
import com.example.soeco.data.Models.DB_Models.Product_DB
import com.example.soeco.databinding.FragmentDeliveryProductsListBinding
import com.example.soeco.ui.viewmodels.DeliveryProductsViewModel
import com.example.soeco.utils.viewModelFactory
import io.realm.RealmList

class DeliveryProductsList : Fragment() {


    private val deliveryProductsViewModel by viewModels<DeliveryProductsViewModel> { viewModelFactory }
    val args: DeliveryProductsListArgs by navArgs()
    private val navigation: NavController by lazy { findNavController() }


    private lateinit var binding: FragmentDeliveryProductsListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentDeliveryProductsListBinding.inflate(inflater, container, false)
            .also { this.binding = it }
            .root
    }


    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView_products)
        val adapter = DeliveryProductsListAdapter(deliveryProductsViewModel.getProducts(args.orderNumber))
        val button : Button = itemView.findViewById(R.id.button_delivery_reportTime)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
        recyclerView.adapter = adapter
        button.setOnClickListener{
            confirm(itemView,recyclerView, adapter)
        }
        setTimePicker(itemView)
    }



    private fun confirm(view: View, recyclerView: RecyclerView, adapter: DeliveryProductsListAdapter){
        val timeText: EditText = view.findViewById(R.id.editTextText_orderHours)
        val time = timeText.text.toString()
        val listOfChildren = recyclerView.children.toList()
        val listOfSelected = RealmList<Product_DB>()
        val listOfNotSelected = RealmList<Product_DB>()

        for (i in listOfChildren.indices) {
            val checkBox : CheckBox= listOfChildren[i].findViewById(R.id.checkBox)
            if (checkBox.isChecked)
                adapter.getItem(i)?.let { listOfSelected.add(it) }
            else adapter.getItem(i)?.let { listOfNotSelected.add(it) }

        }
        deliveryProductsViewModel.addDeliveryReport(Delivery_Report_DB(time,listOfSelected,listOfNotSelected, args.orderNumber) )
        navigation.popBackStack()




    }
    private fun setTimePicker(view :View){
        val timeText: EditText = view.findViewById(R.id.editTextText_orderHours)
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
        timeText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) timeText.performClick()

        }
    }
}

