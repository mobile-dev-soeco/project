package com.example.soeco.ui.carpentry

import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.soeco.R
import com.example.soeco.data.Models.DB_Models.Material_DB
import android.view.View.OnFocusChangeListener
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import java.util.*


internal class CarpentryMaterialsAdapter(data: OrderedRealmCollection<Material_DB?>?) : RealmRecyclerViewAdapter<Material_DB?,
        CarpentryMaterialsAdapter.MaterialsViewHolder?>(data, true) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.material_item, parent, false
        )

        return MaterialsViewHolder(view)
    }

    override fun onBindViewHolder(holder: MaterialsViewHolder, position: Int) {

        val obj = getItem(position)
        val textMaterialName: TextView = holder.view.findViewById(R.id.material_name)
        val textDate: EditText = holder.view.findViewById(R.id.calender_material)
        val productName = obj!!.name
        textMaterialName.text = productName

        holder.data = obj
        val cardView: CardView = holder.view.findViewById(R.id.card_material)
        setDateListener(textDate)
    }


    override fun getItemId(index: Int): Long {
        return getItem(index)!!._id.toLong()
    }

    internal inner class MaterialsViewHolder(var view: View) :
        RecyclerView.ViewHolder(view) {
        var data: Material_DB? = null
    }

    private fun setDateListener(dateText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        dateText.setText(String.format("%d/%d/%d", day, month+1, year))
        dateText.keyListener=null
        dateText.setOnClickListener {
            val activity = it.context as CarpentryActivity
            val datePickerDialog = DatePickerDialog(activity, { _, year, month, dayOfMonth ->
                dateText.setText(  String.format("%d/%d/%d", dayOfMonth, month+1, year))
            }, year, month, day)
            datePickerDialog.show()
        }
        dateText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) dateText.performClick()

        }
    }

}