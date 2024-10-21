package com.example.shoppinggroceryapp.helpers

import android.os.Bundle
import com.core.domain.products.Product

class PutExtras {
    companion object{
        fun putProductExtras(product:Product,bundle: Bundle):Bundle{
            bundle.putString("productName",product.productName)
            bundle.putLong("productId",product.productId)
            bundle.putLong("brandId",product.brandId)
            bundle.putString("categoryName",product.categoryName)
            bundle.putString("productName",product.productName)
            bundle.putString("productDescription",product.productDescription)
            bundle.putFloat("price",product.price)
            bundle.putFloat("offer",product.offer)
            bundle.putString("productQuantity",product.productQuantity)
            bundle.putString("mainImage",product.mainImage)
            bundle.putBoolean("isVeg",product.isVeg)
            bundle.putString("manufactureDate",product.manufactureDate)
            bundle.putString("expiryDate",product.expiryDate)
            bundle.putInt("availableItems",product.availableItems)
            return bundle
        }
        fun getProductFromExtras(bundle: Bundle):Product{
            val productId: Long = bundle.getLong("productId", 1)
            val brandId: Long = bundle.getLong("brandId", 1)
            val categoryName: String = bundle.getString("categoryName").toString()
            val productName: String = bundle.getString("productName").toString()
            val productDescription: String = bundle.getString("productDescription").toString()
            val price: Float = bundle.getFloat("price", 0f)
            val offer: Float = bundle.getFloat("offer", 0f)
            val productQuantity: String = bundle.getString("productQuantity").toString()
            val mainImage: String = bundle.getString("mainImage").toString()
            val isVeg: Boolean = bundle.getBoolean("isVeg", false)
            val manufactureDate: String = bundle.getString("manufactureDate").toString()
            val expiryDate: String = bundle.getString("expiryDate").toString()
            val availableItems: Int = bundle.getInt("availableItems", 0)
            return Product(
                productId,
                brandId,
                categoryName,
                productName,
                productDescription,
                price,
                offer,
                productQuantity,
                mainImage,
                isVeg,
                manufactureDate,
                expiryDate,
                availableItems
            )
        }
    }
}