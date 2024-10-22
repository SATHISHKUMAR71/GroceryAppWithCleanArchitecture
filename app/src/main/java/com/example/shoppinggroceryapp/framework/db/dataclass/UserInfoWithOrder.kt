package com.example.shoppinggroceryapp.framework.db.dataclass

data class UserInfoWithOrder(
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

