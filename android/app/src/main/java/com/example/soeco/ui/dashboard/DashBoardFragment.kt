package com.example.soeco.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.soeco.R
import com.example.soeco.ui.carpentry.CarpentryOrderDetailFragmentArgs
import com.example.soeco.ui.viewmodels.DashBoardViewModel
import com.example.soeco.utils.viewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DashBoardFragment : Fragment() {
    private val dashBoardViewModel by viewModels<DashBoardViewModel> { viewModelFactory }
    private val navigation: NavController by lazy { findNavController() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =when (dashBoardViewModel.userRole) {
            "leverans" -> {
                inflater.inflate(R.layout.fragment_dash_delivery, container, false)
            }
            else ->inflater.inflate(R.layout.fragment_dash, container, false)
        }
        if(dashBoardViewModel.userRole == "leverans") {
            setDateTime(view)
            val nextDate: ImageButton = view.findViewById(R.id.nextDate)
            val prevDate: ImageButton = view.findViewById(R.id.prevDate)
            nextDate.setOnClickListener {
                dashBoardViewModel.nextDay()
                dashBoardViewModel.updateDB()
                setDateTime(view)

            }
            prevDate.setOnClickListener {
                dashBoardViewModel.prevDay()
                dashBoardViewModel.updateDB()
                setDateTime(view)
            }
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView_Orders)
        val adapter =
            DashBoardAdapter(dashBoardViewModel.orders, dashBoardViewModel.userRole, navigation)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
        recyclerView.adapter = adapter
        val swipeRefresh : SwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefresh.setOnRefreshListener {
            dashBoardViewModel.updateDB()
            swipeRefresh.isRefreshing = false;
        }
        return view
    }


    fun setDateTime(view:View){
        val textDate: TextView = view.findViewById(R.id.textDate)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatedDate = dashBoardViewModel.date.format(formatter)
        textDate.text = formatedDate
    }

}