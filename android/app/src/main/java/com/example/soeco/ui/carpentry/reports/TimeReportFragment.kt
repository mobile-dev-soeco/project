package com.example.soeco.ui.carpentry.reports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.soeco.databinding.FragmentTimeReportBinding

class TimeReportFragment : Fragment() {

    private lateinit var binding: FragmentTimeReportBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentTimeReportBinding.inflate(inflater, container, false)
            .also { this.binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}