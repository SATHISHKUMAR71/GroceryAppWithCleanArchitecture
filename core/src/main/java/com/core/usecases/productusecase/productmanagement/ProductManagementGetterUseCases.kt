package com.core.usecases.productusecase.productmanagement

import com.core.usecases.cartusecase.getcartusecase.GetSpecificProductInCart
import com.core.usecases.productusecase.getproductusecase.GetBrandName
import com.core.usecases.productusecase.getproductusecase.GetImagesForProduct
import com.core.usecases.productusecase.getproductusecase.GetMaxPrice
import com.core.usecases.productusecase.retailerproductusecase.getretailerproduct.GetAllParentCategoryNames
import com.core.usecases.productusecase.retailerproductusecase.getretailerproduct.GetBrandWithName
import com.core.usecases.productusecase.retailerproductusecase.getretailerproduct.GetChildCategoriesForParent
import com.core.usecases.productusecase.retailerproductusecase.getretailerproduct.GetChildCategoryNames
import com.core.usecases.productusecase.retailerproductusecase.getretailerproduct.GetImage
import com.core.usecases.productusecase.retailerproductusecase.getretailerproduct.GetLastProduct
import com.core.usecases.productusecase.retailerproductusecase.getretailerproduct.GetParentCategoryImageUsingChild
import com.core.usecases.productusecase.retailerproductusecase.getretailerproduct.GetParentCategoryImageUsingParentName
import com.core.usecases.productusecase.retailerproductusecase.getretailerproduct.GetParentCategoryNameForChild
import com.core.usecases.productusecase.retailerproductusecase.getretailerproduct.GetUserInfoForModifiedProduct

data class ProductManagementGetterUseCases(var mGetBrandName: GetBrandName,
                                           var mGetAllParentCategoryNames: GetAllParentCategoryNames,
                                           var mGetParentCategoryNameForChild: GetParentCategoryNameForChild,
                                           var mGetChildCategoryNames: GetChildCategoryNames,
                                           var mGetParentCategoryImageUsingChild: GetParentCategoryImageUsingChild,
                                           var mGetParentCategoryImageUsingParentName: GetParentCategoryImageUsingParentName,
                                           var mGetChildCategoriesForParent: GetChildCategoriesForParent,
                                           var mGetImagesForProduct: GetImagesForProduct,
                                           var mGetBandWithName: GetBrandWithName,
                                           var mGetLastProduct: GetLastProduct,
                                           var mGetImage: GetImage,
                                           var mGetMaxPrice: GetMaxPrice,
                                           var mGetSpecificProductInCart: GetSpecificProductInCart,
                                           val mGetUserInfoForModifiedProduct: GetUserInfoForModifiedProduct,
)