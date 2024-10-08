
package com.example.shoppinggroceryapp.views.retailerviews.addeditproduct

import android.graphics.Bitmap
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.products.BrandData
import com.core.domain.products.Category
import com.core.domain.products.Images
import com.core.domain.products.ParentCategory
import com.core.domain.products.Product
import com.core.usecases.productusecase.productmanagement.ProductManagementDeleteUseCases
import com.core.usecases.productusecase.productmanagement.ProductManagementGetterUseCases
import com.core.usecases.productusecase.productmanagement.ProductManagementSetterUseCases
import com.example.shoppinggroceryapp.framework.db.dataclass.IntWithCheckedData
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

    fun parentCategoryChecker(parentCategory: String,parentArray: Array<String>):Boolean{
        for(i in parentArray){
            if(parentCategory==i){
                return true
            }
        }
        return false
    }

    fun subCategoryChecker(childCategory: String,childArray: Array<String>):Boolean{
        for(i in childArray){
            if(childCategory==i){
                return true
            }
        }
        return false
    }

    fun getParentCategoryImage(childCategoryName:String){
        Thread{
            println("#@#@ parent image: image got in view model ${productGetters.mGetParentCategoryImageUsingChild.invoke(childCategoryName)}")
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
            println("IMAGES VALUE IN VM: ${productGetters.mGetImagesForProduct.invoke(productId)}")
            imageList.postValue(productGetters.mGetImagesForProduct.invoke(productId))
        }.start()
    }


    fun updateInventory(brandName:String, isNewProduct:Boolean, product: Product, productId:Long?, imageList: List<String>, deletedImageList:MutableList<String>,oldMainImage:String){
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
                    deleteImage(j)
                }

                for(i in imageList){
                    println("IMAGES LIST IN ADD EDIT FRAGMENT: $i old main image: $oldMainImage product main image: ${product.mainImage}")
                    lastProduct?.let {
                        productSetters.mAddProductImage.invoke(Images(0,it.productId,i))
                    }
                }
                ProductListFragment.selectedProductEntity.postValue(prod)
            }

        }.start()
    }

    fun deleteImage(imageValue:String){
        Thread {
            productGetters.mGetImage.invoke(imageValue)
                ?.let {
                    println(
                        "4545 DELETE REQUESTED IMAGES in non null: $imageValue ${
                            productGetters.mGetImage.invoke(
                                imageValue
                            )
                        }"
                    )
                    productDeleteUseCases.mDeleteProductImage.invoke(it)
                }
        }.start()
    }



}