package com.example.soeco.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController

import androidx.recyclerview.widget.RecyclerView
import com.example.soeco.data.Models.DB_Models.Order_DB
import com.example.soeco.R
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter


internal class DashBoardAdapter(data: OrderedRealmCollection<Order_DB?>?, userRole: String?, navigation: NavController) :
    RealmRecyclerViewAdapter<Order_DB?,
            DashBoardAdapter.DashBoardViewHolder?>(data, true) {
    val userRole = userRole
    val navigation = navigation

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashBoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.order_dash_item, parent, false)
        return DashBoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashBoardViewHolder, position: Int) {
        val obj = getItem(position)
        val textView : TextView = holder.view.findViewById(R.id.textView_ordernumber)
        val orderNumber = obj!!._id
        textView.text = orderNumber
        holder.data = obj
        val cardView :CardView = holder.view.findViewById(R.id.card_Order)
        cardView.setOnClickListener {
            // depending on the user role, navigate to different fragments
            when(userRole?.lowercase()){
                "tapetserare" -> {
                    navigateCarpenterOrderDetail(navigation, orderNumber)
                }
                "snickare" -> {
                    navigateCarpenterOrderDetail(navigation, orderNumber)
                }
                "leverans" -> {
                    navigateDeliveryOrderDetail(navigation, orderNumber)
                    }
                }
            }
        }

    private fun navigateDeliveryOrderDetail(navigation: NavController, ordernumber: String){
        val action = DashBoardFragmentDirections.actionDashBoardFragmentToDeliveryOrderDetailFragment(ordernumber)
        navigation.navigate(action)
    }

    private fun navigateCarpenterOrderDetail(navigation: NavController, ordernumber: String){
        val action = DashBoardFragmentDirections.actionDashBoardFragmentToCarpentryOrderDetailFragment(ordernumber)
        navigation.navigate(action)
    }

    override fun getItemId(index: Int): Long {
        return getItem(index)!!._id.toLong()
    }

    internal inner class DashBoardViewHolder(var view: View) :
        RecyclerView.ViewHolder(view) {
        var data: Order_DB? = null
    }
}