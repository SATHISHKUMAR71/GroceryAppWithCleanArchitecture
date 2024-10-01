package com.example.shoppinggroceryapp.framework.db.entity.deals

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DealsEntity (
    @PrimaryKey(autoGenerate = true)
    val dealId:Long,
    val productId:Long,
    val dealImage:String,
    val dealDescription:String,
    val extraText:String,
)