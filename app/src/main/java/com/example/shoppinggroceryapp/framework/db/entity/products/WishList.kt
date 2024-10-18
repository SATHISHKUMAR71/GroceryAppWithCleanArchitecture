package com.example.shoppinggroceryapp.framework.db.entity.products

import androidx.room.Entity

@Entity(primaryKeys = ["productId","userId"])
data class WishListEntity(
    val productId:Long,
    val userId:Int
)