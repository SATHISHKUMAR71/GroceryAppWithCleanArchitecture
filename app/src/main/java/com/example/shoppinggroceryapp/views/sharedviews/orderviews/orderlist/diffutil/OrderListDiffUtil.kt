package com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderlist.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.core.domain.order.OrderDetails
import com.example.shoppinggroceryapp.framework.db.entity.order.OrderDetailsEntity

class OrderListDiffUtil(
    private val oldList:MutableList<OrderDetails>,
    private val newList:MutableList<OrderDetails>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].hashCode() == newList[newItemPosition].hashCode()
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }


}

