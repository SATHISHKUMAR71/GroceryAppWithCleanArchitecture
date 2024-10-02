package com.example.shoppinggroceryapp.views.retailerviews.addeditproduct

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.products.BrandData
import com.core.domain.products.Category
import com.core.domain.products.Images
import com.core.domain.products.ParentCategory
import com.core.domain.products.Product
import com.core.usecases.retailerusecase.products.productmanagement.ProductManagementDeleteUseCases
import com.core.usecases.retailerusecase.products.productmanagement.ProductManagementGetterUseCases
import com.core.usecases.retailerusecase.products.productmanagement.ProductManagementSetterUseCases
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productdetail.ProductDetailViewModel

class AddEditProductViewModel(private var productGetters: ProductManagementGetterUseCases, private var productSetters: ProductManagementSetterUseCases, private var productDeleteUseCases: ProductManagementDeleteUseCases):ViewModel() {

    var brandName:MutableLiveData<String> = MutableLiveData()
    var parentArray:MutableLiveData<Array<String>> = MutableLiveData()
    var imageList:MutableLiveData<List<Images>> = MutableLiveData()
    var parentCategory:MutableLiveData<String> = MutableLiveData()
    var childArray:MutableLiveData<Array<String>> = MutableLiveData()
    var categoryImage:MutableLiveData<String> = MutableLiveData()

    fun getBrandName(brandId:Long){
        Thread{
            synchronized(ProductDetailViewModel.brandLock) {
                brandName.postValue(productGetters.mGetBrandName.invoke(brandId))
            }
        }.start()
    }

    fun getParentArray(){
        Thread{
            parentArray.postValue(productGetters.mGetAllParentCategoryNames.invoke())
        }.start()
    }

    fun getParentCategory(childName:String){
        Thread{
            parentCategory.postValue(productGetters.mGetParentCategoryNameForChild.invoke(childName))
        }.start()
    }

    fun getChildArray(){
        Thread {
            childArray.postValue(productGetters.mGetChildCategoryNames.invoke())
        }.start()
    }

    fun getParentCategoryImage(childCategoryName:String){
        Thread{
            categoryImage.postValue(productGetters.mGetParentCategoryImageUsingChild.invoke(childCategoryName))
        }.start()
    }

    fun getParentCategoryImageForParent(parentCategoryName:String){
        Thread{
            categoryImage.postValue(productGetters.mGetParentCategoryImageUsingParentName.invoke(parentCategoryName))
        }.start()
    }

    fun getChildArray(parentName:String){
        Thread {
            childArray.postValue(productGetters.mGetChildCategoriesForParent.invoke(parentName))
        }.start()
    }

    fun addParentCategory(parentCategory: ParentCategory){
        Thread{
            productSetters.mAddParentCategory.invoke(parentCategory)
        }.start()
    }

    fun addSubCategory(category: Category){
        Thread{
            productSetters.mAddSubCategory.invoke(category)
        }.start()
    }

    fun getImagesForProduct(productId: Long){
        Thread{
            imageList.postValue(productGetters.mGetImagesForProduct.invoke(productId))
        }.start()
    }

    fun updateInventory(brandName:String, isNewProduct:Boolean, product: Product, productId:Long?, imageList: List<String>, deletedImageList:MutableList<String>){
        var brand: BrandData?
        Thread{
            synchronized(ProductDetailViewModel.brandLock) {

                brand = productGetters.mGetBandWithName.invoke(brandName)
                var prod:Product = product
                var lastProduct: Product? = product
                if (brand == null) {
                    productSetters.mAddNewBrand.invoke(BrandData(0,brandName))
                    brand = productGetters.mGetBandWithName.invoke(brandName)
                }

                if (isNewProduct) {
                    brand?.let {
                        prod = product.copy(brandId = it.brandId)
                        productSetters.mAddProduct.invoke(prod)
                        lastProduct = productGetters.mGetLastProduct.invoke()
                    }

                } else {
                    brand?.let {
                        prod = product.copy(brandId = it.brandId, productId = productId!!)
                        productSetters.mUpdateProduct.invoke(prod)
                        lastProduct = prod
                    }
                }

                for(j in deletedImageList){
                    productGetters.mGetImage.invoke(j)
                        ?.let { productDeleteUseCases.mDeleteProductImage.invoke(it) }
                }

                for(i in imageList){
                    lastProduct?.let {
                        productSetters.mAddProductImage.invoke(Images(0,it.productId,i))
                    }
                }
                ProductListFragment.selectedProductEntity.postValue(prod)
            }

        }.start()
    }



}