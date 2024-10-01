package com.core.domain.products


data class Category(
    val categoryName:String,
    val parentCategoryName:String,
    val categoryDescription:String
)