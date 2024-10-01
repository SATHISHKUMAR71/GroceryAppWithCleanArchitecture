package com.example.shoppinggroceryapp.framework.db.entity.help

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CustomerRequestEntity(
    @PrimaryKey(autoGenerate = true)
    val helpId:Int,
    val userId:Int,
    val requestedDate:String,
    val orderId:Int,
    val request:String
)