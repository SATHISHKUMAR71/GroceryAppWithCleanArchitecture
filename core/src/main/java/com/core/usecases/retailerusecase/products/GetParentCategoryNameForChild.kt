package com.core.usecases.retailerusecase.products

import com.core.data.repository.ProductRepository

class GetParentCategoryNameForChild(private val productRepository: ProductRepository) {
    fun invoke(childName:String):String?{
        return productRepository.getParentCategoryNameForChild(childName)
    }
}