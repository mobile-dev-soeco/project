package com.example.soeco.carpentry
import android.util.Log
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

import com.example.soeco.Models.DB_Models.Order_DB
import com.example.soeco.R
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

internal class CarpentryDashBoardAdapter(data: OrderedRealmCollection<Order_DB?>?) :
    RealmRecyclerViewAdapter<Order_DB?,
            CarpentryDashBoardAdapter.DashBoardViewHolder?>(data, true) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashBoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.order_dash_item, parent, false)

        return DashBoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarpentryDashBoardAdapter.DashBoardViewHolder, position: Int) {
        val obj = getItem(position)
        val textView : TextView = holder.view.findViewById(R.id.textView_ordernumber)
        val orderNumber= obj!!.OrderNumber
        textView.text =orderNumber
        holder.data = obj
        val cardView :CardView = holder.view.findViewById(R.id.card_Order)
        cardView.setOnClickListener {
            Log.i("Dashboard-Adapter", orderNumber)

        }
    }
    override fun getItemId(index: Int): Long {
        return getItem(index)!!.OrderNumber.toLong()
    }
    internal inner class DashBoardViewHolder(var view: View) :
        RecyclerView.ViewHolder(view) {
        var data: Order_DB? = null
    }



}