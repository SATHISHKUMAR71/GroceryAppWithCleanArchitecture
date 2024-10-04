package com.core.usecases.productusecase.retailerproductusecase.getretailerproduct

import com.core.data.repository.ProductRepository
import com.core.domain.products.Images

class GetImage(private val productRepository: ProductRepository) {
    fun invoke(image:String):Images?{
        return productRepository.getSpecificImage(image)
    }
}