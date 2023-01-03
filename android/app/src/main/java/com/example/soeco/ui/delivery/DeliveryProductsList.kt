package com.example.soeco.ui.delivery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soeco.R
import com.example.soeco.databinding.FragmentProductsListBinding
import com.example.soeco.ui.viewmodels.ProductsViewModel
import com.example.soeco.utils.viewModelFactory

class DeliveryProductsList : Fragment() {


    private val productsListViewModel by viewModels<ProductsViewModel> { viewModelFactory }
    val args: DeliveryProductsListArgs by navArgs()


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
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView_products)
        val adapter = DeliveryProductsListAdapter(productsListViewModel.getProducts(args.orderNumber))
        recyclerView.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
        recyclerView.adapter = adapter
    }
}

