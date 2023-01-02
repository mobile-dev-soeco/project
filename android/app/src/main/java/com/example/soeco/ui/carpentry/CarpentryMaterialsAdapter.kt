package com.example.soeco.ui.carpentry

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.soeco.R
import com.example.soeco.data.Models.DB_Models.Material_DB
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter


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
        val productName = obj!!.name
        textMaterialName.text = productName

        holder.data = obj
        val cardView: CardView = holder.view.findViewById(R.id.card_material)
    }


    override fun getItemId(index: Int): Long {
        return getItem(index)!!.id.toLong()
    }

    internal inner class MaterialsViewHolder(var view: View) :
        RecyclerView.ViewHolder(view) {
        var data: Material_DB? = null
    }



}