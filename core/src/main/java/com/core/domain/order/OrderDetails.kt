package com.core.domain.order

data class OrderDetails(
    val orderId:Int,
    val cartId:Int,
    val addressId:Int,
    val paymentMode:String,
    val deliveryFrequency:String,
    val paymentStatus:String,
    val deliveryStatus:String,
    val deliveryDate:String,
    val orderedDate:String
)