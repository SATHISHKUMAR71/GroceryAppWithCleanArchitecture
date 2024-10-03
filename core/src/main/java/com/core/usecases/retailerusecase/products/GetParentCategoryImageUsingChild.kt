package com.core.usecases.retailerusecase.products

import com.core.data.repository.ProductRepository

class GetParentCategoryImageUsingChild(private val productRepository: ProductRepository) {
    fun invoke(childName:String):String?{
        return productRepository.getParentCategoryImageForParent(childName)
    }
}