package com.core.usecases.productusecase.retailerproductusecase.setretailerproduct

import com.core.data.repository.ProductRepository
import com.core.domain.products.Product

class UpdateProduct(private val productRepository: ProductRepository) {
    fun invoke(product: Product){
        productRepository.updateProduct(product)
    }
}