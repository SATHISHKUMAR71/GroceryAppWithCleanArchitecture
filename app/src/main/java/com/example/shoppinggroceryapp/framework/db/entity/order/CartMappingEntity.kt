package com.example.shoppinggroceryapp.framework.db.entity.order

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CartMappingEntity(
    @PrimaryKey(autoGenerate = true)
    val cartId:Int,
    val userId:Int,
    val status:String
)
