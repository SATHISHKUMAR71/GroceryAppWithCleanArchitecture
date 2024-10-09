package com.example.shoppinggroceryapp.helpers.alertdialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.fragment.app.FragmentManager

class DataLossAlertDialog {
    fun showDataLossAlertDialog(context: Context,parentFragmentManager:FragmentManager){
        AlertDialog.Builder(context)
            .setTitle("Confirm Exit")
            .setMessage("Your changes will not be saved. Do you want to exit?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                parentFragmentManager.popBackStack()
            })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            .show()
    }
}