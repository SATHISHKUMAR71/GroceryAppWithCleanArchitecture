package com.core.usecases.filterusecases

import com.core.data.repository.ProductRepository
import com.core.domain.products.BrandData

class GetAllBrands(var productRepository: ProductRepository) {
    fun invoke():List<BrandData>{
        return productRepository.getAllBrands()
    }
}