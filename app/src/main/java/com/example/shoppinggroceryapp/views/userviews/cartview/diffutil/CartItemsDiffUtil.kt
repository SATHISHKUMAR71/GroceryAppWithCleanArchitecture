package com.example.shoppinggroceryapp.views.userviews.cartview.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.core.domain.products.Product
import com.example.shoppinggroceryapp.framework.db.entity.products.ProductEntity


class CartItemsDiffUtil(
    private val oldList:List<Product>,
    private val newList:List<Product>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

//    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        return ((oldList[oldItemPosition].productId== newList[newItemPosition].productId))
//    }
//
//    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        return (oldItemPosition == newItemPosition && oldList[oldItemPosition]==newList[newItemPosition])
//    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return ((oldList[oldItemPosition].productId== newList[newItemPosition].productId))
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldItemPosition == newItemPosition && oldList[oldItemPosition]==newList[newItemPosition])
    }
}

