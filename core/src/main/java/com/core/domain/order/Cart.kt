package com.core.domain.order

data class Cart(
    val cartId:Int,
    val productId:Int,
    val totalItems:Int,
    val unitPrice:Float
)