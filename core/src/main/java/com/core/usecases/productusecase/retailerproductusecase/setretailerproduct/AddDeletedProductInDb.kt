package com.core.usecases.productusecase.retailerproductusecase.setretailerproduct

import com.core.data.repository.ProductRepository
import com.core.domain.products.DeletedProductList

class AddDeletedProductInDb(private val productRepository: ProductRepository) {
    fun invoke(deletedProductList: DeletedProductList){
        productRepository.addDeletedProduct(deletedProductList)
    }
}