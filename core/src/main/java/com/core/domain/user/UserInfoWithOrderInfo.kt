package com.core.domain.user

data class UserInfoWithOrderInfo(
    val userId:Int,
    val userFirstName:String,
    val userLastName:String,
    val userEmail:String,
    val userPhone:String,
    val orderId:Int,
    val cartId:Int,
    val addressId:Int,
    val paymentMode:String,
    val deliveryFrequency:String,
    val paymentStatus:String,
    val deliveryStatus:String,
    val deliveryDate:String,
    val orderedDate:String)

