package com.example.shoppinggroceryapp.helpers.toast

import android.content.Context
import android.widget.Toast

object ShowShortToast {

        fun show(text: String, context: Context) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }

}