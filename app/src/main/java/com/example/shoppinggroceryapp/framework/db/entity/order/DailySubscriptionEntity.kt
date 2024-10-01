package com.example.shoppinggroceryapp.framework.db.entity.order

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DailySubscriptionEntity(
    @PrimaryKey(autoGenerate = false)
    val orderId:Int
)