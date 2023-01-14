package com.example.soeco.ui.carpentry.products


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.soeco.R
import com.example.soeco.data.Models.DB_Models.Product_DB
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

internal class ProductsListAdapter(
    data: OrderedRealmCollection<Product_DB?>?,
    private val onProductClickListener: (view: View, product: Product_DB) -> Unit
) : RealmRecyclerViewAdapter<Product_DB?,
        ProductsListAdapter.ProductsViewHolder?>(data, true) {

    internal inner class ProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productId: TextView
        val quantity: TextView
        val notes: TextView

        init {
            productId = itemView.findViewById(R.id.tvProductId)
            quantity = itemView.findViewById(R.id.tvQuantity)
            notes = itemView.findViewById(R.id.tvNotes)
        }

        fun bind(product: Product_DB?) {
            productId.text = product?.product_id
            quantity.text = product?.count
            notes.text = product?.note
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item_2, parent, false)

        return ProductsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {

        holder.apply {
            bind(getItem(position))
            itemView.setOnClickListener { view ->
                onProductClickListener.invoke(view, getItem(position)!!)
            }
        }
//        val product = getItem(position)
//        val textProductCount : TextView = holder.view.findViewById(R.id.product_count)
//        val textProductName : TextView = holder.view.findViewById(R.id.product_name)
//        val textDate: EditText = holder.view.findViewById(R.id.calender)
//        val productCount = product!!.count
//        val productName = product!!.name
//        val confirmButton : Button = holder.view.findViewById(R.id.confirm_product)
//
//        textProductCount.text = productCount
//        textProductName.text = productName
//        holder.data = product
//        setDateListener(textDate)
//        setTimePicker(holder.view)
//        createReportListener(confirmButton, holder)
    }

//    private fun createReportListener(confirmButton: Button, holder: ProductsViewHolder) {
//
//        confirmButton.setOnClickListener{
//            val name = holder.data?.name.toString()
//            val id = holder.data?._id.toString()
//            val orderNumber = holder.data?.orderNumber.toString()
//            val count = holder.data?.count
//            val expectedTime = productsListViewModel.getExpectedTime(orderNumber)
//
//            val textDate: EditText = holder.view.findViewById(R.id.calender)
//            val textTime: EditText = holder.view.findViewById(R.id.actual_hours)
//
//            val date = textDate.text.toString()
//            val time = textTime.text.toString()
//
//            productsListViewModel.addProductReport(Tradesmen_Report_DB(id,orderNumber,expectedTime,count,name,date,time))
//
//        }
//    }

    override fun getItemId(index: Int): Long {
        return getItem(index)!!.product_id.toLong()
    }

//    private fun setDateListener(dateText: EditText) {
//        val calendar = Calendar.getInstance()
//        val year = calendar.get(Calendar.YEAR)
//        val month = calendar.get(Calendar.MONTH)
//        val day = calendar.get(Calendar.DAY_OF_MONTH)
//        dateText.setText(String.format("%d/%d/%d", day, month+1, year))
//        dateText.keyListener=null
//        dateText.setOnClickListener {
//            val activity = it.context as CarpentryActivity
//            val datePickerDialog = DatePickerDialog(activity, { _, year, month, dayOfMonth ->
//                dateText.setText(  String.format("%d/%d/%d", dayOfMonth, month+1, year))
//            }, year, month, day)
//            datePickerDialog.show()
//        }
//        dateText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
//            if (hasFocus) dateText.performClick()
//
//        }
//    }

//    private fun setTimePicker(view :View){
//        val timeText: EditText = view.findViewById(R.id.actual_hours)
//        timeText.keyListener = null
//
//        val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
//            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
//                val newTime = "$hourOfDay tim :  $minute min"
//                timeText.setText(newTime)
//            }
//
//
//        timeText.setOnClickListener {
//            val timePicker: TimePickerDialog =
//                TimePickerDialog(view.context, timePickerDialogListener, 0, 0, true)
//            timePicker.show()
//        }
//        timeText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
//            if (hasFocus) timeText.performClick()
//
//        }
//    }
}