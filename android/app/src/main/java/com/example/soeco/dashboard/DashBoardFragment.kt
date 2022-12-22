package com.example.soeco.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soeco.R
import com.example.soeco.Api.MainViewModel

class DashBoardFragment : Fragment() {

    private val viewmodel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewmodel.userRole= "delivery"
        viewmodel.update()

        return inflater.inflate(R.layout.fragment_dash, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        val recyclerView :RecyclerView = itemView.findViewById(R.id.recyclerView_Orders)
        val adapter = DashBoardAdapter(viewmodel.orders,viewmodel.userRole)
        recyclerView.layoutManager =LinearLayoutManager(requireActivity().applicationContext)
        recyclerView.adapter = adapter
    }

}