package com.example.soeco.ui.carpentry

import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.soeco.R
import com.example.soeco.data.Models.DB_Models.Material_DB
import com.example.soeco.data.Models.DB_Models.Material_Report_DB
import com.example.soeco.ui.viewmodels.MaterialsViewModel
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import java.util.*


internal class CarpentryMaterialsAdapter(
    data: OrderedRealmCollection<Material_DB?>?,
    val orderNumber: String,
    val materialsViewModel: MaterialsViewModel
) : RealmRecyclerViewAdapter<Material_DB?,
        CarpentryMaterialsAdapter.MaterialsViewHolder?>(data, true) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.material_item, parent, false
        )

        return MaterialsViewHolder(view)
    }

    override fun onBindViewHolder(holder: MaterialsViewHolder, position: Int) {

        val material = getItem(position)
        val textMaterialName: TextView = holder.view.findViewById(R.id.material_name)
        val textDate: EditText = holder.view.findViewById(R.id.calender_material)
        val confirmButton : Button = holder.view.findViewById(R.id.confirm_material)
        val textCount : TextView = holder.view.findViewById(R.id.materialAntal)
        val antal = textCount.text.toString()
        val enhet = material?.unit
        val concatenate = "$antal ($enhet)"
        textCount.text= concatenate

        val productName = material!!.name
        textMaterialName.text = productName
        holder.data = material
        setDateListener(textDate)
        createReportListener(confirmButton,holder)
    }

    private fun createReportListener(confirmButton: Button, holder: MaterialsViewHolder) {

        confirmButton.setOnClickListener{
            val name = holder.data?.name.toString()
            val id = holder.data?._id.toString()
            val unit = holder.data?.unit.toString()

            val textDate: EditText = holder.view.findViewById(R.id.calender_material)
            val textMaterial: EditText = holder.view.findViewById(R.id.material_unit)

            val date = textDate.text.toString()
            val amount = textMaterial.text.toString()

            materialsViewModel.addMaterialReport(Material_Report_DB(orderNumber, id, amount, name, unit, date))

        }
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