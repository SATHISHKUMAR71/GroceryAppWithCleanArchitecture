package com.core.usecases.productusecase.retailerproductusecase.getretailerproduct

import com.core.data.repository.ProductRepository

class GetParentCategoryImageUsingParentName(private val productRepository: ProductRepository) {
    fun invoke(parentCategoryName:String):String?{
        println("GET PARENT IMAGE CALLED: in class ${productRepository.getParentCategoryImage(parentCategoryName)}")
        return productRepository.getParentCategoryImage(parentCategoryName)
    }
}