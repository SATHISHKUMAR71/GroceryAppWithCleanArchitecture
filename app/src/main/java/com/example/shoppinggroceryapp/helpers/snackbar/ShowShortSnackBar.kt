package com.example.shoppinggroceryapp.helpers.snackbar

import android.content.Context
import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar

object ShowShortSnackBar {
    fun showRedColor(view: View,text:String){
        Snackbar.make(view,text,Snackbar.LENGTH_SHORT).apply {
            setBackgroundTint(Color.argb(255, 230, 20, 20))
            show()
        }
    }

    fun showGreenColor(view:View,text:String) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).apply {
            setTextColor(Color.BLACK)
            setBackgroundTint(Color.argb(255, 20, 230, 20))
                .show()
        }
    }
}