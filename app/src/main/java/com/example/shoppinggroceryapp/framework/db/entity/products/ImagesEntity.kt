package com.example.shoppinggroceryapp.framework.db.entity.products

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImagesEntity (
    @PrimaryKey(autoGenerate = true)
    val imageId:Long,
    val productId:Long,
    val images:String
)