package com.example.shoppinggroceryapp.framework.db.dataclass

data class ProductWithDeletedCounts(
    val productCount:Int,
    val deletedProductCount:Int
)