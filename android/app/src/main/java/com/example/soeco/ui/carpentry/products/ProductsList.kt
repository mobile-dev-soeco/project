package com.example.soeco.ui.carpentry.products

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soeco.R
import com.example.soeco.data.Models.DB_Models.Product_DB
import com.example.soeco.databinding.FragmentProductsListBinding
import com.example.soeco.ui.carpentry.CarpentryViewModel
import com.example.soeco.ui.viewmodels.ProductsViewModel
import com.example.soeco.utils.viewModelFactory

class ProductsList : Fragment() {

    private lateinit var carpentryViewModel: CarpentryViewModel
    private val navigation: NavController by lazy { findNavController() }

    private lateinit var binding: FragmentProductsListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentProductsListBinding.inflate(inflater, container, false)
            .also { this.binding = it }
            .root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        carpentryViewModel = ViewModelProvider(requireActivity(), viewModelFactory)[CarpentryViewModel::class.java]
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView_products)
        val adapter = ProductsListAdapter(carpentryViewModel.getProducts(), ::onProductItemClick)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
        recyclerView.adapter = adapter
    }

    private fun onProductItemClick(view: View, product: Product_DB) {
        carpentryViewModel.setCurrentProduct(product)
        navigation.navigate(R.id.action_productsList_to_productDetails)
    }

}

