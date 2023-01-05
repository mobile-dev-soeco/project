package com.example.soeco.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soeco.R
import com.example.soeco.ui.carpentry.CarpentryOrderDetailFragmentArgs
import com.example.soeco.ui.viewmodels.DashBoardViewModel
import com.example.soeco.utils.viewModelFactory

class DashBoardFragment : Fragment() {

    private val dashBoardViewModel by viewModels<DashBoardViewModel> { viewModelFactory }
    private val navigation: NavController by lazy { findNavController() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dash, container, false)


        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView_Orders)
        val adapter =
            DashBoardAdapter(dashBoardViewModel.orders, dashBoardViewModel.userRole, navigation)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
        recyclerView.adapter = adapter

        return view
    }

    fun onItemClicked() {

    }

}