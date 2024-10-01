package com.example.shoppinggroceryapp.framework.db.entity.order

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OrderDetailsEntity(
    @PrimaryKey(autoGenerate = true)
    val orderId:Int,
    val cartId:Int,
    val addressId:Int,
    val paymentMode:String,
    val deliveryFrequency:String,
    val paymentStatus:String,
    val deliveryStatus:String,
    val deliveryDate:String,
    val orderedDate:String
)