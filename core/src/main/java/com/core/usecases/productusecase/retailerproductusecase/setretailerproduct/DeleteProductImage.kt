package com.core.usecases.productusecase.retailerproductusecase.setretailerproduct

import com.core.data.repository.ProductRepository
import com.core.domain.products.Images

class DeleteProductImage(private val productRepository: ProductRepository) {
    fun invoke(images: Images){
        println("IMAGES VALUE: $images")
        productRepository.deleteProductImage(images)
    }
}