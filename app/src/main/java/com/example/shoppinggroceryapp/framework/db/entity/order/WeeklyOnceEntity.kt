package com.example.shoppinggroceryapp.framework.db.entity.order

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeeklyOnceEntity(
    @PrimaryKey(autoGenerate = false)
    val orderId:Int,
    val weekId:Int
)