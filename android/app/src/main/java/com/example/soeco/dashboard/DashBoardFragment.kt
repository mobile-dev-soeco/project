package com.example.soeco.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soeco.R

class DashBoardFragment : Fragment() {
    private val rowsList = ArrayList<String>()
    private lateinit var customAdapter: DashBoardAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dash, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView_Orders)
        customAdapter = DashBoardAdapter(rowsList)
        val layoutManager = LinearLayoutManager(context) // applicationContext
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = customAdapter
        prepareItems()
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun prepareItems() {
        rowsList.add("Order1")
        rowsList.add("Order2")
        rowsList.add("Order3")
        rowsList.add("Order4")
        rowsList.add("Order1")
        rowsList.add("Order2")
        rowsList.add("Order3")
        rowsList.add("Order4")
        rowsList.add("Order1")
        rowsList.add("Order2")
        rowsList.add("Order3")
        rowsList.add("Order4")
        rowsList.add("Order1")
        rowsList.add("Order2")
        rowsList.add("Order3")
        rowsList.add("Order4")
        customAdapter.notifyDataSetChanged()
    }
}