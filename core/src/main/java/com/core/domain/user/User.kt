package com.core.domain.user

data class User(
    val userId:Int,
    val userImage:String,
    val userFirstName:String,
    val userLastName:String,
    val userEmail:String,
    val userPhone:String,
    val userPassword:String,
    val dateOfBirth:String,
    val isRetailer:Boolean
)