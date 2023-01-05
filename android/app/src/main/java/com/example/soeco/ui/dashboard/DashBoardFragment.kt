package com.example.soeco.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soeco.R
import com.example.soeco.databinding.FragmentDashBinding
import com.example.soeco.ui.viewmodels.DashBoardViewModel
import com.example.soeco.utils.viewModelFactory

class DashBoardFragment : Fragment() {

    private val dashBoardViewModel by viewModels<DashBoardViewModel> { viewModelFactory }

    private lateinit var binding: FragmentDashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentDashBinding.inflate(inflater, container, false)
            .also { this.binding = it }
            .root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        val recyclerView :RecyclerView = itemView.findViewById(R.id.recyclerView_Orders)
        val adapter = DashBoardAdapter(dashBoardViewModel.orders, dashBoardViewModel.userRole)
        recyclerView.layoutManager =LinearLayoutManager(requireActivity().applicationContext)
        recyclerView.adapter = adapter
    }

    fun onItemClicked() {

    }

}