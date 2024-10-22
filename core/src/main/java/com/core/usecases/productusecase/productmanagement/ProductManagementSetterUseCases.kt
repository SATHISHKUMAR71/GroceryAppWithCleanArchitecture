package com.core.usecases.productusecase.productmanagement

import com.core.usecases.productusecase.retailerproductusecase.setretailerproduct.AddNewBrand
import com.core.usecases.productusecase.retailerproductusecase.setretailerproduct.AddParentCategory
import com.core.usecases.productusecase.retailerproductusecase.setretailerproduct.AddProduct
import com.core.usecases.productusecase.retailerproductusecase.setretailerproduct.AddProductImage
import com.core.usecases.productusecase.retailerproductusecase.setretailerproduct.AddSubCategory
import com.core.usecases.productusecase.retailerproductusecase.setretailerproduct.UpdateProduct

class ProductManagementSetterUseCases(var mAddParentCategory: AddParentCategory,
                                      var mAddSubCategory: AddSubCategory,
                                      var mAddProduct: AddProduct,
                                      var mUpdateProduct: UpdateProduct,
                                      var mAddProductImage: AddProductImage,
                                      var mAddNewBrand: AddNewBrand)
