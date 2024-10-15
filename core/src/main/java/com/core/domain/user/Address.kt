package com.core.domain.user


data class Address (
    val addressId:Int,
    val userId:Int,
    val addressContactName:String,
    val addressContactNumber:String,
    val buildingName:String,
    val streetName:String,
    val city:String,
    val state:String,
    val country:String,
    val postalCode:String
)