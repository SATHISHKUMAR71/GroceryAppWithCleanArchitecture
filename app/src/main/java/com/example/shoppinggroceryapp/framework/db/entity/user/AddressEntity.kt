package com.example.shoppinggroceryapp.framework.db.entity.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
//    foreignKeys = [
//        ForeignKey(entity = User::class,
//            parentColumns = ["userId"],
//            childColumns = ["userId"])
//    ]
//)
data class AddressEntity (
    @PrimaryKey(autoGenerate = true)
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