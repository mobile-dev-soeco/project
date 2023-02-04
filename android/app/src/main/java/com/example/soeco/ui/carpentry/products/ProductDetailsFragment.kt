package com.example.soeco.ui.carpentry.products

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soeco.R
import com.example.soeco.TAG
import com.example.soeco.data.Models.mongo.TimeReport
import com.example.soeco.databinding.FragmentProductDetailsBinding
import com.example.soeco.ui.carpentry.CarpentryViewModel
import com.example.soeco.utils.viewModelFactory

class ProductDetailsFragment : Fragment() {

    private val navigation: NavController by lazy { findNavController() }

    private lateinit var carpentryViewModel: CarpentryViewModel
    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var adapter: TimeReportAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return FragmentProductDetailsBinding.inflate(inflater, container, false)
            .also { this.binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        carpentryViewModel = ViewModelProvider(requireActivity(), viewModelFactory)[CarpentryViewModel::class.java]

        binding.productCard.tvProductId.text = carpentryViewModel.currentProduct.value?.product_id
        binding.productCard.tvQuantity.text = carpentryViewModel.currentProduct.value?.count
        binding.productCard.tvNotes.text = carpentryViewModel.currentProduct.value?.note

        adapter = TimeReportAdapter(::handleDeleteClick)

        binding.rvTimeReports.adapter = adapter
        binding.rvTimeReports.layoutManager = LinearLayoutManager(
            this.context,
            LinearLayoutManager.VERTICAL,
            false
        )

        carpentryViewModel.timeReportLiveData.observe(viewLifecycleOwner) { reports ->
            adapter.submitList(reports)
        }

        binding.btnReportTime.setOnClickListener {
            navigation.navigate(R.id.action_productDetails_to_timeReportFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        carpentryViewModel.currentProduct.value?.product_id?.let { id ->
            carpentryViewModel.getTimeReports(
                id
            )
        }
    }

    override fun onStop() {
        super.onStop()
        carpentryViewModel.clearTimeReports()
    }

    private fun handleDeleteClick(position: Int, timeReport: TimeReport) {
        Log.v(TAG(), "${timeReport.reportId} clicked")
        val item = adapter.currentList[position]
        val currentList = adapter.currentList.toMutableList()
        currentList.removeAt(position)
        adapter.submitList(currentList)
        carpentryViewModel.deleteTimeReport(item.reportId)
    }

}