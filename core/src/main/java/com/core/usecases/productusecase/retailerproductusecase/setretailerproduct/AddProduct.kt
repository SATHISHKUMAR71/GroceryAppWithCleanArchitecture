package com.core.usecases.productusecase.retailerproductusecase.setretailerproduct

import com.core.data.repository.ProductRepository
import com.core.domain.products.Product

class AddProduct(private val productRepository: ProductRepository) {
    fun invoke(product: Product){
        productRepository.addProduct(product)
    }
}