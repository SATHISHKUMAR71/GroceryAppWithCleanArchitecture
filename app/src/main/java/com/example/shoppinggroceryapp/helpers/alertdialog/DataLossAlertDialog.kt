package com.example.shoppinggroceryapp.helpers.alertdialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import androidx.fragment.app.FragmentManager
import com.example.shoppinggroceryapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DataLossAlertDialog {
    fun showDataLossAlertDialog(context: Context,parentFragmentManager:FragmentManager){

        MaterialAlertDialogBuilder(context)
            .setTitle("Confirm to Exit?")
            .setMessage("Your changes will not be saved. Do you want to exit?")
            .setPositiveButton("Exit", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                parentFragmentManager.popBackStack()
            })

            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            .show()

//        dialog.setOnShowListener {
//            var positiveBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
//            positiveBtn.setTextColor(Color.WHITE)
//            positiveBtn.setBackgroundColor(com.google.android.material.R.attr.colorErrorContainer)
//        }
//        dialog.show()

    }
}