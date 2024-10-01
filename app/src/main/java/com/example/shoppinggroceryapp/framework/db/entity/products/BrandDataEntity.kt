package com.example.shoppinggroceryapp.framework.db.entity.products

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class BrandDataEntity(
    @PrimaryKey(autoGenerate = true)
    var brandId:Long,
    var brandName:String
)