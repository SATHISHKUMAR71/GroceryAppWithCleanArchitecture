package com.core.usecases.productusecase.retailerproductusecase.getretailerproduct

import com.core.data.repository.ProductRepository

class GetParentCategoryImageUsingChild(private val productRepository: ProductRepository) {
    fun invoke(childName:String):String?{

        return productRepository.getParentCategoryImageForParent(childName)
    }
}