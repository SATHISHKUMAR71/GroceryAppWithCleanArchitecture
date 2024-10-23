package com.example.shoppinggroceryapp.views.userviews.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.products.Category
import com.core.domain.products.ParentCategory
import com.core.usecases.productusecase.getproductusecase.GetParentAndChildCategories
import com.example.shoppinggroceryapp.framework.db.entity.products.ParentCategoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(private val mGetParentAndChildNames: GetParentAndChildCategories):ViewModel() {
    var mappedList:MutableLiveData<Map<ParentCategory,List<Category>>> = MutableLiveData()
    var parentCategoryEntity:ParentCategoryEntity? = null

    fun getParentAndChildNames(){
        viewModelScope.launch(Dispatchers.IO) {
            mappedList.postValue(mGetParentAndChildNames.invoke())
        }
//        Thread {
//            mappedList.postValue(mGetParentAndChildNames.invoke())
//        }.start()
    }
    fun getParentList(map:Map<ParentCategory,List<Category>>):List<ParentCategory>{
        return map.keys.toMutableList()
    }
    fun getChildList(mapValue:Map<ParentCategory,List<Category>>):List<List<String>>{
        return mapValue.values.map { it.map { data-> data.categoryName } }
    }
}