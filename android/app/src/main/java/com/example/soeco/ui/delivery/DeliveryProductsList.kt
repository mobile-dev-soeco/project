package com.example.soeco.ui.delivery

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soeco.R
import com.example.soeco.data.Models.DB_Models.Delivery_Report_DB
import com.example.soeco.data.Models.DB_Models.Product_DB
import com.example.soeco.databinding.FragmentDeliveryProductsListBinding
import com.example.soeco.ui.viewmodels.DeliveryProductsViewModel
import com.example.soeco.utils.viewModelFactory
import io.realm.RealmList

class DeliveryProductsList : Fragment() {


    private val deliveryProductsViewModel by viewModels<DeliveryProductsViewModel> { viewModelFactory }
    val args: DeliveryProductsListArgs by navArgs()
    private val navigation: NavController by lazy { findNavController() }


    private lateinit var binding: FragmentDeliveryProductsListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return FragmentDeliveryProductsListBinding.inflate(inflater, container, false)
            .also { this.binding = it }
            .root
    }


    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView_products)
        val adapter = DeliveryProductsListAdapter(deliveryProductsViewModel.getProducts())
        recyclerView.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
        recyclerView.adapter = adapter


    }
}

