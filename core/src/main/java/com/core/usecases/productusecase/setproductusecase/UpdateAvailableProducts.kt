package com.core.usecases.productusecase.setproductusecase

import com.core.data.repository.ProductRepository
import com.core.domain.products.Product

class UpdateAvailableProducts(private var productRepository: ProductRepository) {
    fun invoke(product:Product){
        productRepository.updateAvailableProducts(product)
    }
}