package com.example.soeco.delivery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soeco.R
import com.example.soeco.Api.MainViewModel

class DeliveryDashBoardFragment : Fragment() {

    private val viewmodel: MainViewModel by viewModels()

    private lateinit var customAdapter: DeliveryDashBoardAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dash, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView_Orders)
        customAdapter = DeliveryDashBoardAdapter()
        val layoutManager = LinearLayoutManager(context) // applicationContext
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = customAdapter
        observerSetup()
        viewmodel.update()
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
        viewmodel.orders.observe(viewLifecycleOwner) { order ->
            order?.let {
                customAdapter.updateOrder(it)

            }
        }
    }

}