package com.example.soeco.ui.dashboard

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
import com.example.soeco.R
import com.example.soeco.databinding.FragmentDashBinding
import com.example.soeco.utils.viewModelFactory

class DashBoardFragment : Fragment() {

    private val dashBoardViewModel by viewModels<DashBoardViewModel> { viewModelFactory }
    private val navigation: NavController by lazy { findNavController() }

    private lateinit var binding: FragmentDashBinding
    private lateinit var customAdapter: DashBoardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return FragmentDashBinding.inflate(inflater, container, false)
            .also { this.binding = it }
            .root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView_Orders)
        customAdapter = DashBoardAdapter()
        val layoutManager = LinearLayoutManager(context) // applicationContext
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = customAdapter
        observerSetup()
        dashBoardViewModel.fetchData()
    }
    /*  @SuppressLint("NotifyDataSetChanged")
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

      */



    private fun observerSetup() {
        dashBoardViewModel.orders.observe(viewLifecycleOwner) { order ->
            order?.let {
                customAdapter.updateOrder(it)

            }
        }
    }

}