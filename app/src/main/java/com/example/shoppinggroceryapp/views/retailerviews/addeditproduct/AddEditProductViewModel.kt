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
    fun subCategoryChecker(childCategory: String,childArray: Array<String>):Boolean{
        for(i in childArray){
            if(childCategory==i){
                return true
            }
        }
        return false
    }

    fun parentCategoryChecker(parentCategory: String,parentArray: Array<String>):Boolean{
        for(i in parentArray){
            if(parentCategory==i){
                return true
            }
        }
        return false
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


//    fun tmp( imageList:MutableMap<Int, IntWithCheckedData>,mainImageBitmap:Bitmap?,imageStringList:List<String>,isNewParentCategory:Boolean,isNewSubCategory:Boolean){
//        val imageListNames = mutableListOf<String>()
//        for (i in imageList) {
//            if ((i.value.bitmap != mainImageBitmap) && (i.value.fileName !in imageStringList)) {
//                val tmpName = System.currentTimeMillis().toString()
//                imageLoader.storeImageInApp(requireContext(), i.value.bitmap, tmpName)
//                imageListNames.add(tmpName)
//            }
//        }
//        val brandNameStr = brandName.text.toString()
//        val subCategoryName = productSubCat.text.toString()
//        if (isNewParentCategory) {
//            val filName = "${System.currentTimeMillis()}"
//            if (parentCategoryImage != null) {
//                imageLoader.storeImageInApp(
//                    requireContext(),
//                    parentCategoryImage!!,
//                    filName
//                )
//            }
//            if (imageLoader.getImageInApp(requireContext(), filName) == null) {
//                isCategoryImageAdded = false
//            }
//            addEditProductViewModel.addParentCategory(ParentCategory(productParentCategory.text.toString(), filName,                             "", false))
//        }
//        if (isNewSubCategory) {
//            addEditProductViewModel.addSubCategory(
//                Category(productSubCat.text.toString(), productParentCategory.text.toString(), "")
//            )
//        }
//        if (isCategoryImageAdded) {
//            addEditProductViewModel.updateInventory(brandNameStr, (ProductListFragment.selectedProductEntity.value == null), Product(0, 0, subCategoryName, productName.text.toString(), productDescription.text.toString(), productPrice.text.toString().toFloat(), productOffer.text.toString().toFloat(), productQuantity.text.toString(), mainImage, isVeg.isChecked, rawManufactureDate, rawExpiryDate, productAvailableItems.text.toString().toInt()), ProductListFragment.selectedProductEntity.value?.productId, imageListNames, deletedImageList)
//            parentFragmentManager.popBackStack()
//            Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()
//        }
//        else{
//            Toast.makeText(context, "Please add the Category Image", Toast.LENGTH_SHORT).show()
//        }
//    }
//    }


}