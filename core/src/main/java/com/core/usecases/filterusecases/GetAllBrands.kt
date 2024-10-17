package com.core.usecases.filterusecases

import com.core.data.repository.ProductRepository

class GetAllBrands(var productRepository: ProductRepository) {
    fun invoke():List<String>{
        return productRepository.getAllBrands()
    }
}