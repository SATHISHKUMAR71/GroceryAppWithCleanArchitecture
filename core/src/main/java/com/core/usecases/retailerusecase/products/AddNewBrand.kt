package com.core.usecases.retailerusecase.products

import com.core.data.repository.RetailerRepository
import com.core.domain.products.BrandData

class AddNewBrand(private val retailerRepository: RetailerRepository){
    fun invoke(brandData: BrandData){
        retailerRepository.addNewBrand(brandData)
    }
}