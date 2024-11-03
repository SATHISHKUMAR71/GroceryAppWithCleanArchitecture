
package com.example.shoppinggroceryapp.views.retailerviews.addeditproduct

import android.graphics.Bitmap
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.core.domain.products.BrandData
import com.core.domain.products.Category
import com.core.domain.products.Images
import com.core.domain.products.ParentCategory
import com.core.domain.products.Product
import com.core.domain.user.UserInfoWithOrderInfo
import com.core.usecases.productusecase.productmanagement.ProductManagementDeleteUseCases
import com.core.usecases.productusecase.productmanagement.ProductManagementGetterUseCases
import com.core.usecases.productusecase.productmanagement.ProductManagementSetterUseCases
import com.example.shoppinggroceryapp.framework.db.database.AppDatabase
import com.example.shoppinggroceryapp.framework.db.dataclass.IntWithCheckedData
import com.example.shoppinggroceryapp.views.sharedviews.filter.FilterPrice
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productdetail.ProductDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddEditProductViewModel(private var productGetters: ProductManagementGetterUseCases, private var productSetters: ProductManagementSetterUseCases, private var productDeleteUseCases: ProductManagementDeleteUseCases):ViewModel() {

    var brandName:MutableLiveData<String> = MutableLiveData()
    var parentArray:MutableLiveData<Array<String>> = MutableLiveData()
    var imageList:MutableLiveData<List<Images>> = MutableLiveData()
    var parentCategory:MutableLiveData<String> = MutableLiveData()
    var childArray:MutableLiveData<Array<String>> = MutableLiveData()
    var categoryImage:MutableLiveData<String> = MutableLiveData()
//    var alertNotification:MutableLiveData<Product> =MutableLiveData()

    fun getBrandName(brandId:Long){
        viewModelScope.launch(Dispatchers.IO){
            synchronized(ProductDetailViewModel.brandLock) {
                brandName.postValue(productGetters.mGetBrandName.invoke(brandId))
            }
        }
//        Thread{
//            synchronized(ProductDetailViewModel.brandLock) {
//                brandName.postValue(productGetters.mGetBrandName.invoke(brandId))
//            }
//        }.start()
    }

    fun getParentArray(){
        viewModelScope.launch (Dispatchers.IO){
            parentArray.postValue(productGetters.mGetAllParentCategoryNames.invoke())
        }
//        Thread{
//            parentArray.postValue(productGetters.mGetAllParentCategoryNames.invoke())
//        }.start()
    }

    fun getParentCategory(childName:String){
        viewModelScope.launch (Dispatchers.IO){
            parentCategory.postValue(productGetters.mGetParentCategoryNameForChild.invoke(childName))
        }
//        Thread{
//            parentCategory.postValue(productGetters.mGetParentCategoryNameForChild.invoke(childName))
//        }.start()
    }

    fun getChildArray(){
        viewModelScope.launch (Dispatchers.IO){
            childArray.postValue(productGetters.mGetChildCategoryNames.invoke())
        }
//        Thread {
//            childArray.postValue(productGetters.mGetChildCategoryNames.invoke())
//        }.start()
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
        viewModelScope.launch(Dispatchers.IO){
            println("#@#@ parent image: image got in view model ${productGetters.mGetParentCategoryImageUsingChild.invoke(childCategoryName)}")
            categoryImage.postValue(productGetters.mGetParentCategoryImageUsingChild.invoke(childCategoryName))
        }
//        Thread{
//            println("#@#@ parent image: image got in view model ${productGetters.mGetParentCategoryImageUsingChild.invoke(childCategoryName)}")
//            categoryImage.postValue(productGetters.mGetParentCategoryImageUsingChild.invoke(childCategoryName))
//        }.start()
    }

    fun getParentCategoryImageForParent(parentCategoryName:String){
        viewModelScope.launch (Dispatchers.IO){
            categoryImage.postValue(productGetters.mGetParentCategoryImageUsingParentName.invoke(parentCategoryName))
        }
//        Thread{
//            categoryImage.postValue(productGetters.mGetParentCategoryImageUsingParentName.invoke(parentCategoryName))
//        }.start()
    }

    fun getChildArray(parentName:String){
        viewModelScope.launch (Dispatchers.IO){
            childArray.postValue(productGetters.mGetChildCategoriesForParent.invoke(parentName))
        }
//        Thread {
//            childArray.postValue(productGetters.mGetChildCategoriesForParent.invoke(parentName))
//        }.start()
    }

    fun addParentCategory(parentCategory: ParentCategory){
        viewModelScope.launch (Dispatchers.IO){
            productSetters.mAddParentCategory.invoke(parentCategory)
        }
//        Thread{
//            productSetters.mAddParentCategory.invoke(parentCategory)
//        }.start()
    }

    fun addSubCategory(category: Category){
        viewModelScope.launch (Dispatchers.IO){
            productSetters.mAddSubCategory.invoke(category)
        }
//        Thread{
//            productSetters.mAddSubCategory.invoke(category)
//        }.start()
    }

    fun getImagesForProduct(productId: Long){
        viewModelScope.launch (Dispatchers.IO){
            println("IMAGES VALUE IN VM: ${productGetters.mGetImagesForProduct.invoke(productId)}")
            imageList.postValue(productGetters.mGetImagesForProduct.invoke(productId))
        }
//        Thread{
//            println("IMAGES VALUE IN VM: ${productGetters.mGetImagesForProduct.invoke(productId)}")
//            imageList.postValue(productGetters.mGetImagesForProduct.invoke(productId))
//        }.start()
    }



    fun updateInventory(brandName:String, isNewProduct:Boolean, product: Product, productId:Long?, imageList: List<String>, deletedImageList:MutableList<String>,oldMainImage:String){
        var brand: BrandData?
        viewModelScope.launch(Dispatchers.IO) {
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
                viewModelScope.launch (Dispatchers.IO){
                    FilterPrice.MAX_PRICE_VALUE = productGetters.mGetMaxPrice.invoke()
                    FilterPrice.priceEndTo = FilterPrice.MAX_PRICE_VALUE
                }

                ProductListFragment.selectedProductEntity.postValue(prod)
//                getOrdersForTheProducts(prod)
            }
        }
//        Thread{
//            synchronized(ProductDetailViewModel.brandLock) {
//                brand = productGetters.mGetBandWithName.invoke(brandName)
//                var prod:Product = product
//                var lastProduct: Product? = product
//                if (brand == null) {
//                    productSetters.mAddNewBrand.invoke(BrandData(0,brandName))
//                    brand = productGetters.mGetBandWithName.invoke(brandName)
//                }
//
//                if (isNewProduct) {
//                    brand?.let {
//                        prod = product.copy(brandId = it.brandId)
//                        productSetters.mAddProduct.invoke(prod)
//                        lastProduct = productGetters.mGetLastProduct.invoke()
//                    }
//
//                } else {
//                    brand?.let {
//                        prod = product.copy(brandId = it.brandId, productId = productId!!)
//                        productSetters.mUpdateProduct.invoke(prod)
//                        lastProduct = prod
//                    }
//                }
//
//                for(j in deletedImageList){
//                    deleteImage(j)
//                }
//
//                for(i in imageList){
//                    println("IMAGES LIST IN ADD EDIT FRAGMENT: $i old main image: $oldMainImage product main image: ${product.mainImage}")
//                    lastProduct?.let {
//                        productSetters.mAddProductImage.invoke(Images(0,it.productId,i))
//                    }
//                }
//                ProductListFragment.selectedProductEntity.postValue(prod)
////                getOrdersForTheProducts(prod)
//            }
//        }.start()
    }

    fun deleteImage(imageValue:String){
        viewModelScope.launch (Dispatchers.IO){
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
        }
//        Thread {
//            productGetters.mGetImage.invoke(imageValue)
//                ?.let {
//                    println(
//                        "4545 DELETE REQUESTED IMAGES in non null: $imageValue ${
//                            productGetters.mGetImage.invoke(
//                                imageValue
//                            )
//                        }"
//                    )
//                    productDeleteUseCases.mDeleteProductImage.invoke(it)
//                }
//        }.start()
    }



}