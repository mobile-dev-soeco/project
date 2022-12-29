package com.example.soeco.ui.carpentry


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.soeco.R
import com.example.soeco.data.Models.DB_Models.Product_DB
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

internal class ProductsListAdapter(data: OrderedRealmCollection<Product_DB?>?) : RealmRecyclerViewAdapter<Product_DB?,
        ProductsListAdapter.ProductsViewHolder?>(data, true) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.product_item, parent, false)

        return ProductsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {

        val obj = getItem(position)
        val textProductId : TextView = holder.view.findViewById(R.id.product_id)
        val textProductName : TextView = holder.view.findViewById(R.id.product_name)
        val productId = obj!!.id
        val productName = obj!!.name
        textProductId.text = productId.toString()
        textProductName.text = productName
        holder.data = obj
        val cardView: CardView = holder.view.findViewById(R.id.card_product)
    }


    override fun getItemId(index: Int): Long {
        return getItem(index)!!.id.toLong()
    }

    internal inner class ProductsViewHolder(var view: View) :
        RecyclerView.ViewHolder(view) {
        var data: Product_DB? = null
    }


}