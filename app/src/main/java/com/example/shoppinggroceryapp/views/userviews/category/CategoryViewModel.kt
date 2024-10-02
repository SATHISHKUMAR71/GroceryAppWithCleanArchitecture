package com.example.shoppinggroceryapp.views.userviews.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.products.ParentCategory
import com.core.usecases.userusecase.GetChildNames
import com.core.usecases.userusecase.GetParentCategories
import com.example.shoppinggroceryapp.framework.db.dao.ProductDao
import com.example.shoppinggroceryapp.framework.db.dataclass.ChildCategoryName
import com.example.shoppinggroceryapp.framework.db.entity.products.ParentCategoryEntity

class CategoryViewModel(private val mGetParentCategories: GetParentCategories,
                        private val mGetChildNames: GetChildNames):ViewModel() {

    var parentList:MutableLiveData<List<ParentCategory>> = MutableLiveData()
    var childList:MutableLiveData<List<List<String>>> = MutableLiveData()
    var parentCategoryEntity:ParentCategoryEntity? = null
    fun getParentCategory(){
        Thread{
            parentList.postValue(mGetParentCategories.invoke())
        }.start()
    }

    fun getChildWithParentName(){
        Thread{
            var list = mutableListOf<List<String>>()
            for(i in parentList.value!!){
                mGetChildNames.invoke(i.parentCategoryName)?.let { list.add(it) }
            }
            childList.postValue(list)
        }.start()
    }

}