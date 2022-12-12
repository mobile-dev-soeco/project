package com.example.soeco.RoomDb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.soeco.Models.DB_Models.Material_DB
import com.example.soeco.Models.DB_Models.Order_DB
import com.example.soeco.Models.DB_Models.Product_DB

@Dao
interface DAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertContact(contact: Material_DB)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrder(order: Order_DB)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProduct(product: Product_DB)

    @Query("SELECT * FROM materialTable")
    fun getMaterials(): LiveData<List<Material_DB>>

    @Query("DELETE FROM orderTable")
    fun clearOrders()

    @Query("SELECT * FROM orderTable")
    fun getOrders(): LiveData<List<Order_DB>>

    @Query("SELECT * FROM productTable")
    fun getProducts(): LiveData<List<Product_DB>>


}

