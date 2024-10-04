package com.core.usecases.productusecase.retailerproductusecase.getretailerproduct

import com.core.data.repository.ProductRepository
import com.core.domain.products.Product

class GetLastProduct(private val productRepository: ProductRepository) {
    fun invoke():Product?{
        return productRepository.getLastProduct()
    }
}