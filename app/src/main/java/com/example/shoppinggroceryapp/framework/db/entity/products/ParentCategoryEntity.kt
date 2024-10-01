package com.example.shoppinggroceryapp.framework.db.entity.products

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ParentCategoryEntity(
    @PrimaryKey
    val parentCategoryName:String,
    val parentCategoryImage:String,
    val parentCategoryDescription:String,
    val isEssential:Boolean
)