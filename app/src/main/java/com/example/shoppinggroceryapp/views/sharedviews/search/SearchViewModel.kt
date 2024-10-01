package com.example.shoppinggroceryapp.views.sharedviews.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.search.SearchHistory
import com.core.usecases.userusecase.AddSearchQueryInDb
import com.core.usecases.userusecase.GetSearchList
import com.core.usecases.userusecase.PerformCategorySearch
import com.core.usecases.userusecase.PerformProductSearch
import com.example.shoppinggroceryapp.MainActivity
import com.example.shoppinggroceryapp.framework.db.dao.UserDao

class SearchViewModel(var mGetSearchList:GetSearchList, var mPerformProductSearch: PerformProductSearch, var mPerformCategorySearch: PerformCategorySearch, var mAddSearchQueryInDb: AddSearchQueryInDb):ViewModel() {

    var searchedList:MutableLiveData<MutableList<String>> = MutableLiveData()
    fun performSearch(query:String){
        Thread {
            var list = performSearchProduct(query)
            list.addAll(mPerformProductSearch(query).toMutableList())
            searchedList.postValue(list)
            performSearchProduct(query)
        }.start()
    }

    private fun performSearchProduct(query:String):MutableList<String>{
        val list = mPerformCategorySearch(query).toMutableList()
        return list
    }

    fun addItemInDb(query:String){
        println("SearchHistory: $query ${MainActivity.userId.toInt()}")
        Thread {
            mAddSearchQueryInDb(SearchHistory(query,MainActivity.userId.toInt()))
        }.start()
    }

    fun getSearchedList(){
        Thread{
            val list = mutableListOf<String>()
            var i = 0
            for(j in mGetSearchList(MainActivity.userId.toInt()).reversed()){
                list.add(j.searchText)
                i++
                if(i==10){
                    break
                }
            }
            searchedList.postValue(list)
        }.start()
    }
}