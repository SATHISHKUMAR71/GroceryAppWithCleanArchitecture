package com.core.usecases.retailerusecase.products

import com.core.data.repository.RetailerRepository
import com.core.domain.products.BrandData

class GetBrandWithName(private val retailerRepository: RetailerRepository) {
    fun invoke(brandName:String):BrandData?{
        return retailerRepository.getBrandWithName(brandName)
    }
}