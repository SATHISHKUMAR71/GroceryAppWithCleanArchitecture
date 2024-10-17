package com.core.domain.products

data class CartWithProductData (
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