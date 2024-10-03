package com.core.usecases.retailerusecase.products

import com.core.data.repository.ProductRepository
import com.core.domain.products.BrandData

class GetBrandWithName(private val productRepository: ProductRepository) {
    fun invoke(brandName:String):BrandData?{
        return productRepository.getBrandWithName(brandName)
    }
}