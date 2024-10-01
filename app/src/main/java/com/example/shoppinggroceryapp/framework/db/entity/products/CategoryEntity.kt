package com.example.shoppinggroceryapp.framework.db.entity.products

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity

data class CategoryEntity(
    @PrimaryKey
    val categoryName:String,
    val parentCategoryName:String,
    val categoryDescription:String
)