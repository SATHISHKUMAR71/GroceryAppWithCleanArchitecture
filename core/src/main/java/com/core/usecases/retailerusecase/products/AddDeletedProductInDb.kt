package com.core.usecases.retailerusecase.products

import com.core.data.repository.RetailerRepository
import com.core.domain.products.DeletedProductList
import com.core.domain.products.Product

class AddDeletedProductInDb(private val retailerRepository: RetailerRepository) {
    fun invoke(deletedProductList: DeletedProductList){
        retailerRepository.addDeletedProduct(deletedProductList)
    }
}