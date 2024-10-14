package com.example.shoppinggroceryapp.views.initialview

import android.content.SharedPreferences
import com.example.shoppinggroceryapp.MainActivity.Companion.isRetailer
import com.example.shoppinggroceryapp.MainActivity.Companion.selectedAddress
import com.example.shoppinggroceryapp.MainActivity.Companion.userEmail
import com.example.shoppinggroceryapp.MainActivity.Companion.userFirstName
import com.example.shoppinggroceryapp.MainActivity.Companion.userId
import com.example.shoppinggroceryapp.MainActivity.Companion.userImage
import com.example.shoppinggroceryapp.MainActivity.Companion.userLastName
import com.example.shoppinggroceryapp.MainActivity.Companion.userPhone

class SetInitialDataForUser(){
    fun invoke(pref:SharedPreferences){
        userFirstName = pref.getString("userFirstName","User").toString()
        userId = pref.getString("userId","userId").toString()
        selectedAddress = pref.getInt("selectedAddress$userId",0)
        println("909090 Selected address pos: $selectedAddress")
        userLastName = pref.getString("userLastName","User").toString()
        userEmail = pref.getString("userEmail","userEmail").toString()
        userPhone = pref.getString("userPhone","userPhone").toString()
        isRetailer = pref.getBoolean("isRetailer",false)
        userImage = pref.getString("userProfile","userProfile").toString()
    }
}