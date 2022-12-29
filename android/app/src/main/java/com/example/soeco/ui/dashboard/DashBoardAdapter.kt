package com.example.soeco.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.soeco.MainActivity
import com.example.soeco.data.Models.DB_Models.Order_DB
import com.example.soeco.R
import com.example.soeco.ui.carpentry.CarpentryOrderDetailFragment
import com.example.soeco.ui.delivery.DeliveryOrderDetailFragment
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter


internal class DashBoardAdapter(data: OrderedRealmCollection<Order_DB?>?, userRole: String?) :
    RealmRecyclerViewAdapter<Order_DB?,
            DashBoardAdapter.DashBoardViewHolder?>(data, true) {
    val userRole = userRole

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashBoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.order_dash_item, parent, false)

        return DashBoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashBoardViewHolder, position: Int) {
        val obj = getItem(position)
        val textView : TextView = holder.view.findViewById(R.id.textView_ordernumber)
        val orderNumber= obj!!.OrderNumber
        textView.text =orderNumber
        holder.data = obj
        val cardView :CardView = holder.view.findViewById(R.id.card_Order)
        cardView.setOnClickListener {
            // depending on the user role, navigate to different fragments
            navigateOrderdetail(navigation, userRole, holder.data!!.OrderNumber)


        }
    }

    private fun navigateOrderdetail(navigation: NavController, userRole: String?, ordernumber: String) {
        when(userRole){
            "carpenter" -> {
                val action = DashBoardFragmentDirections.actionDashBoardFragmentToCarpentryOrderDetailFragment(ordernumber)
                navigation.navigate(action)
            }



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