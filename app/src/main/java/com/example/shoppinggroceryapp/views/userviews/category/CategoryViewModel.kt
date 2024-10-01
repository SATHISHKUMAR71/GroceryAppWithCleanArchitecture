package com.example.shoppinggroceryapp.views.userviews.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinggroceryapp.framework.db.dao.ProductDao
import com.example.shoppinggroceryapp.framework.db.dataclass.ChildCategoryName
import com.example.shoppinggroceryapp.framework.db.entity.products.ParentCategoryEntity

class CategoryViewModel(var productDao: ProductDao):ViewModel() {

    var parentList:MutableLiveData<List<ParentCategoryEntity>> = MutableLiveData()
    var childList:MutableLiveData<List<List<ChildCategoryName>>> = MutableLiveData()
    var parentCategoryEntity:ParentCategoryEntity? = null
    fun getParentCategory(){
        Thread{
            parentList.postValue(productDao.getParentCategoryList())
        }.start()
    }

    fun getChildWithParentName(){
        Thread{
            var list = mutableListOf<List<ChildCategoryName>>()
            for(i in parentList.value!!){
                list.add(productDao.getChildName(i.parentCategoryName))
            }
            childList.postValue(list)
        }.start()
    }

    fun updateParentCategory(parentCategoryEntity: ParentCategoryEntity){
        Thread{
            productDao.updateParentCategory(parentCategoryEntity)
        }.start()
    }
}