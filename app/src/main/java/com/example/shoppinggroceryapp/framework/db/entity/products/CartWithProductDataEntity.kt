package com.example.shoppinggroceryapp.framework.db.entity.products

data class CartWithProductDataEntity (
    val productId:Long,
    val mainImage:String?,
    val productName:String,
    val productDescription:String,
    val totalItems:Int,
    val unitPrice:Float,
    val manufactureDate:String,
    val expiryDate:String,
    val productQuantity:String,
    var brandName:String
)