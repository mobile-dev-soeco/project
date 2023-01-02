package com.example.soeco.ui.carpentry


import android.app.DatePickerDialog
import android.app.PendingIntent.getActivity
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
import com.example.soeco.data.Models.DB_Models.Product_DB
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import java.security.AccessController.getContext
import java.util.*

internal class ProductsListAdapter(data: OrderedRealmCollection<Product_DB?>?) : RealmRecyclerViewAdapter<Product_DB?,
        ProductsListAdapter.ProductsViewHolder?>(data, true) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.product_item, parent, false)

        return ProductsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {

        val obj = getItem(position)
        val textProductId : TextView = holder.view.findViewById(R.id.product_id)
        val textProductName : TextView = holder.view.findViewById(R.id.product_name)
        val textDate: EditText = holder.view.findViewById(R.id.calender)
        val productId = obj!!.product_id
        val productName = obj!!.name
        textProductId.text = productId.toString()
        textProductName.text = productName
        holder.data = obj
        val cardView: CardView = holder.view.findViewById(R.id.card_product)
        setDateListener(textDate)
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
}