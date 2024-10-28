package com.example.shoppinggroceryapp.views.userviews.category.adapter

import android.view.View

interface OnItemClick {
    fun onItemClicked(position:Int,itemView:View):Boolean
}