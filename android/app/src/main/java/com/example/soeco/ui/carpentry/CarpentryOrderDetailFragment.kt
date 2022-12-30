package com.example.soeco.ui.carpentry
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.soeco.R
import com.example.soeco.ui.delivery.DeliveryOrderDetailFragmentDirections
import com.example.soeco.ui.viewmodels.DashBoardViewModel
import com.example.soeco.ui.viewmodels.OrderDetailsViewModel
import com.example.soeco.utils.viewModelFactory
import java.util.*

class CarpentryOrderDetailFragment : Fragment() {

    val args: CarpentryOrderDetailFragmentArgs by navArgs()
    private val navigation: NavController by lazy { findNavController() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_carpentry_order_detail, container, false)

        val orderNumberTextView : TextView = view.findViewById(R.id.textView_carpentry_products_view_order_number)
        val viewProductsButton : Button = view.findViewById(R.id.button_carpentry_viewProducts)
        val viewMaterialsButton : Button = view.findViewById(R.id.button_carpentry_viewMaterials)
        val reportDeviationButton: Button = view.findViewById(R.id.button_carpentry_reportDeviation)
        //val order_id = this.arguments?.getString("order")
        val order_id = args.orderNumber
        orderNumberTextView.text= order_id.toString()
//        val order = order_id.let { viewmodel.getOrder(it) }
//        if (order != null) {
//            orderNumberTextView.text= order.OrderNumber
//        }

        viewProductsButton.setOnClickListener {
            val action = CarpentryOrderDetailFragmentDirections.actionCarpentryOrderDetailFragmentToProductsList(order_id)
            navigation.navigate(action)

        }
        viewMaterialsButton.setOnClickListener {
            navigation.navigate(R.id.action_carpentryOrderDetailFragment_to_carpentryMaterials)
        }

        reportDeviationButton.setOnClickListener {
            val action = CarpentryOrderDetailFragmentDirections.actionCarpentryOrderDetailFragmentToQuestionnaireCarpentry(args.orderNumber)
            navigation.navigate(action)
        }
        return view
    }


}