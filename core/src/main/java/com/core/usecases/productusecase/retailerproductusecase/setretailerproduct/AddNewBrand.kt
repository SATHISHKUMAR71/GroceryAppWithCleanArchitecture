package com.core.usecases.productusecase.retailerproductusecase.setretailerproduct

import com.core.data.repository.ProductRepository
import com.core.domain.products.BrandData

class AddNewBrand(private val productRepository: ProductRepository) {
    fun invoke(brandData: BrandData){
        productRepository.addNewBrand(brandData)
    }
}