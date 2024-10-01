package com.example.shoppinggroceryapp.framework.db.entity.order

import androidx.room.Entity

@Entity(primaryKeys = ["cartId","productId"])
data class CartEntity(
    val cartId:Int,
    val productId:Int,
    val totalItems:Int,
    val unitPrice:Float
)