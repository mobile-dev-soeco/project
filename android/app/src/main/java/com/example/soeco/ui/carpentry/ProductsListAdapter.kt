package com.example.soeco.ui.carpentry


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.View.OnFocusChangeListener
import android.widget.Button
import android.widget.EditText
import com.example.soeco.R
import com.example.soeco.data.Models.DB_Models.Material_Report_DB
import com.example.soeco.data.Models.DB_Models.Product_DB
import com.example.soeco.data.Models.DB_Models.Product_Report_DB
import com.example.soeco.ui.viewmodels.ProductsViewModel
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import java.util.*

internal class ProductsListAdapter(
    data: OrderedRealmCollection<Product_DB?>?,
    val productsListViewModel: ProductsViewModel
) : RealmRecyclerViewAdapter<Product_DB?,
        ProductsListAdapter.ProductsViewHolder?>(data, true) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.product_item, parent, false)

        return ProductsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {

        val product = getItem(position)
        val textProductCount : TextView = holder.view.findViewById(R.id.product_count)
        val textProductName : TextView = holder.view.findViewById(R.id.product_name)
        val textDate: EditText = holder.view.findViewById(R.id.calender)
        val productCount = product!!.count
        val productName = product!!.name
        val confirmButton : Button = holder.view.findViewById(R.id.confirm_product)

        textProductCount.text = productCount.toString()
        textProductName.text = productName
        holder.data = product
        setDateListener(textDate)
        setTimePicker(holder.view)
        createReportListener(confirmButton, holder)
    }

    private fun createReportListener(confirmButton: Button, holder: ProductsViewHolder) {

        confirmButton.setOnClickListener{
            val name = holder.data?.name.toString()
            val id = holder.data?._id.toString()
            val orderNumber = holder.data?.orderNumber.toString()
            val count = holder.data?.count


            val textDate: EditText = holder.view.findViewById(R.id.calender)
            val textTime: EditText = holder.view.findViewById(R.id.actual_hours)

            val date = textDate.text.toString()
            val time = textTime.text.toString()

            productsListViewModel.addProductReport(Product_Report_DB(id,orderNumber,count,name,date,time))

        }
    }
    override fun getItemId(index: Int): Long {
        return getItem(index)!!.product_id.toLong()
    }

    internal inner class ProductsViewHolder(var view: View) :
        RecyclerView.ViewHolder(view) {
        var data: Product_DB? = null
    }
    private fun setDateListener(dateText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        dateText.setText(String.format("%d/%d/%d", day, month+1, year))
        dateText.keyListener=null
        dateText.setOnClickListener {
            val activity = it.context as CarpentryActivity
            val datePickerDialog = DatePickerDialog(activity, { _, year, month, dayOfMonth ->
                dateText.setText(  String.format("%d/%d/%d", dayOfMonth, month+1, year))
            }, year, month, day)
            datePickerDialog.show()
        }
        dateText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) dateText.performClick()

        }
    }

    private fun setTimePicker(view :View){
        val timeText: EditText = view.findViewById(R.id.actual_hours)
        timeText.keyListener = null

        val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                val newTime = "$hourOfDay tim :  $minute min"
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


}