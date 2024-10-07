package com.example.shoppinggroceryapp.helpers.extensions

import android.os.Bundle
import com.core.domain.user.Address

fun Bundle.putAddress(key:String, value: Address){
    this.putAddress("address",value)
}

fun Bundle.getAddress(): Address {
    return this.getAddress()
}