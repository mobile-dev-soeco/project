package com.example.soeco.ui.carpentry

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soeco.R
import com.example.soeco.databinding.FragmentCarpentryMaterialsBinding
import com.example.soeco.databinding.FragmentProductsListBinding
import com.example.soeco.ui.viewmodels.MaterialsViewModel
import com.example.soeco.ui.viewmodels.ProductsViewModel
import com.example.soeco.utils.viewModelFactory

class CarpentryMaterials : Fragment() {


    private val materialsViewModel by viewModels<MaterialsViewModel> { viewModelFactory }
    private val navigation: NavController by lazy { findNavController() }
    private lateinit var binding: FragmentCarpentryMaterialsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentCarpentryMaterialsBinding.inflate(inflater, container, false)
            .also { this.binding = it }
            .root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        val recyclerView : RecyclerView = itemView.findViewById(R.id.recyclerView_materials)
        val adapter = CarpentryMaterialsAdapter(materialsViewModel.materials)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
        recyclerView.adapter = adapter
    }

}