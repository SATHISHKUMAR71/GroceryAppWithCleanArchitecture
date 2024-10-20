package com.example.shoppinggroceryapp.views.sharedviews.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.domain.search.SearchHistory
import com.core.usecases.searchusecase.AddSearchQueryInDb
import com.core.usecases.searchusecase.GetSearchList
import com.core.usecases.searchusecase.PerformCategorySearch
import com.core.usecases.searchusecase.PerformProductSearch
import com.example.shoppinggroceryapp.MainActivity

class SearchViewModel(var mGetSearchList: GetSearchList, var mPerformProductSearch: PerformProductSearch, var mPerformCategorySearch: PerformCategorySearch, var mAddSearchQueryInDb: AddSearchQueryInDb):ViewModel() {

    var searchedList:MutableLiveData<MutableList<String>> = MutableLiveData()
    fun performSearch(query:String){
        Thread {
            var list = performSearchProduct(query)
            list.addAll(mPerformProductSearch(query)?: listOf<String>().toMutableList())
            searchedList.postValue(list)
            performSearchProduct(query)
        }.start()
    }

    private fun performSearchProduct(query:String):MutableList<String>{
        val list = (mPerformCategorySearch(query)?: mutableListOf<String>()).toMutableList()
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
            for(j in (mGetSearchList(MainActivity.userId.toInt())?: listOf()).reversed()){
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