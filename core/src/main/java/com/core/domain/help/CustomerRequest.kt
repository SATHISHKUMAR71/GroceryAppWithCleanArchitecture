package com.core.domain.help

data class CustomerRequest(
    val helpId:Int,
    val userId:Int,
    val requestedDate:String,
    val orderId:Int,
    val request:String
)