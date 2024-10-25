package com.example.shoppinggroceryapp.helpers.toast

import android.content.Context
import android.widget.Toast

object ShowShortToast {

    var toast:Toast? = null
    fun show(text: String, context: Context) {
        toast?.cancel()
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        toast?.show()
    }

}