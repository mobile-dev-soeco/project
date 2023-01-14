package com.example.soeco.ui.carpentry.products

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soeco.TAG
import com.example.soeco.data.Models.mongo.TimeReport
import com.example.soeco.databinding.FragmentProductDetailsBinding
import com.example.soeco.utils.viewModelFactory

class ProductDetailsFragment : Fragment() {

    private val productDetailsViewModel by viewModels<ProductDetailsViewModel> { viewModelFactory }
    private val args: ProductDetailsFragmentArgs by navArgs()
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

        binding.productCard.tvProductId.text = args.productId
        binding.productCard.tvQuantity.text = args.quantity
        binding.productCard.tvNotes.text = args.notes

        adapter = TimeReportAdapter(::handleDeleteClick)

        binding.rvTimeReports.adapter = adapter
        binding.rvTimeReports.layoutManager = LinearLayoutManager(
            this.context,
            LinearLayoutManager.VERTICAL,
            false
        )

        productDetailsViewModel.timeReportLiveData.observe(viewLifecycleOwner) { reports ->
            adapter.submitList(reports)
        }

        binding.btnReportTime.setOnClickListener {
            productDetailsViewModel.insertTimeReport(args.productId)
        }
    }

    override fun onResume() {
        super.onResume()
        productDetailsViewModel.getTimeReports(args.productId)
    }

    private fun handleDeleteClick(view: View, timeReport: TimeReport) {
        Log.v(TAG(), "${timeReport._id} clicked")
    }

}