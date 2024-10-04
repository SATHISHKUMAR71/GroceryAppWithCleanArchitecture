package com.core.usecases.productusecase.retailerproductusecase.getretailerproduct

import com.core.data.repository.ProductRepository

class GetParentCategoryNameForChild(private val productRepository: ProductRepository) {
    fun invoke(childName:String):String?{
        return productRepository.getParentCategoryNameForChild(childName)
    }
}