package com.core.usecases.retailerusecase.products

import com.core.data.repository.ProductRepository

class GetParentCategoryImageUsingParentName(private val productRepository: ProductRepository) {
    fun invoke(parentCategoryName:String):String?{
        return productRepository.getParentCategoryImage(parentCategoryName)
    }
}