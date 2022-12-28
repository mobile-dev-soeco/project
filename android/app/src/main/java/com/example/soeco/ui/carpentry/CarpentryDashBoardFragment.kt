package com.example.soeco.ui.carpentry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soeco.databinding.FragmentDashBinding
import com.example.soeco.utils.viewModelFactory

class CarpentryDashBoardFragment() : Fragment() {

    private val carpentryDashboardViewModel by viewModels<CarpentryDashBoardViewModel> { viewModelFactory }
    private val navigation: NavController by lazy { findNavController() }

    private lateinit var binding: FragmentDashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDashBinding.inflate(inflater, container, false)
            .also { this.binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderList: RecyclerView = binding.recyclerViewOrders
        val adapter = CarpentryDashBoardAdapter(carpentryDashboardViewModel.orders)
        orderList.layoutManager =LinearLayoutManager(requireActivity().applicationContext)
        orderList.adapter = adapter

    }



}