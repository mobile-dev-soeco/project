package com.example.soeco.dashboard

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.soeco.MainActivity
import com.example.soeco.Models.DB_Models.Order_DB
import com.example.soeco.R

internal class DashBoardAdapter() :

    RecyclerView.Adapter<DashBoardAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var startTextView: TextView = view.findViewById(R.id.textView_ordernumber)
        var orderCard: CardView = view.findViewById(R.id.card_Order)
    }
    private var orderList: List<Order_DB>? = null

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_dash_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.startTextView.text = orderList?.get(position)?.orderNumber
        // onclick on the box
        holder.orderCard.setOnClickListener(View.OnClickListener { view ->
            val intent = Intent(view.context, MainActivity::class.java) // TODO Change to order_detail
            intent.putExtra("order", orderList?.get(position)?.orderNumber)
            view.context.startActivity(intent)
        })
    }

    override fun getItemCount(): Int {
        if (orderList!=null)
            return orderList!!.size
        return 0
    }

    fun updateOrder(newList: List<Order_DB>) {
        orderList = newList
        notifyDataSetChanged()
    }


}