package com.example.soeco.ui.delivery


import android.app.DatePickerDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.soeco.R
import com.example.soeco.data.Models.DB_Models.Order_DB
import com.example.soeco.data.Models.DB_Models.Product_DB
import com.example.soeco.ui.dashboard.DashBoardAdapter
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import java.util.*

internal class DeliveryProductsListAdapter(data: OrderedRealmCollection<Product_DB?>?) : RealmRecyclerViewAdapter<Product_DB?,
        DeliveryProductsListAdapter.ProductsViewHolder?>(data, true) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.delivery_product_item, parent, false)

        return ProductsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val obj = getItem(position)
        val textProductCount : TextView = holder.view.findViewById(R.id.product_count)
        val textProductName : TextView = holder.view.findViewById(R.id.product_name)
        textProductCount.text = obj!!.count.toString()
        textProductName.text = obj!!.name
        holder.data = obj
    }


    override fun getItemId(index: Int): Long {
        return getItem(index)!!.product_id.toLong()
    }




    internal inner class ProductsViewHolder(var view: View) :
        RecyclerView.ViewHolder(view) {
        var data: Product_DB? = null
    }


}