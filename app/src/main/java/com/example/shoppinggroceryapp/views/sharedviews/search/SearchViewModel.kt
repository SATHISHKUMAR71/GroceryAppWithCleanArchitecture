package com.example.shoppinggroceryapp.views.sharedviews.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.search.SearchHistory
import com.core.usecases.searchusecase.AddSearchQueryInDb
import com.core.usecases.searchusecase.GetSearchList
import com.core.usecases.searchusecase.PerformCategorySearch
import com.core.usecases.searchusecase.PerformProductSearch
import com.example.shoppinggroceryapp.MainActivity
import com.example.shoppinggroceryapp.views.sharedviews.search.adapter.SearchListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(var mGetSearchList: GetSearchList, var mPerformProductSearch: PerformProductSearch, var mPerformCategorySearch: PerformCategorySearch, var mAddSearchQueryInDb: AddSearchQueryInDb):ViewModel() {

    var searchedList:MutableLiveData<MutableList<String>> = MutableLiveData()
    fun performSearch(query:String){
        viewModelScope.launch(Dispatchers.IO) {
            var list = performSearchProduct(query)
            list.addAll(mPerformProductSearch(query)?: listOf<String>().toMutableList())
            searchedList.postValue(list)
            performSearchProduct(query)
        }
//        Thread {
//            var list = performSearchProduct(query)
//            list.addAll(mPerformProductSearch(query)?: listOf<String>().toMutableList())
//            searchedList.postValue(list)
//            performSearchProduct(query)
//        }.start()
    }

    private fun performSearchProduct(query:String):MutableList<String>{
        val list = (mPerformCategorySearch(query)?: mutableListOf<String>()).toMutableList()
        SearchListAdapter.hideIcon = false
        return list
    }

    fun addItemInDb(query:String){
        viewModelScope.launch(Dispatchers.IO) {
            mAddSearchQueryInDb(SearchHistory(query,MainActivity.userId.toInt()))
        }
//        Thread {
//            mAddSearchQueryInDb(SearchHistory(query,MainActivity.userId.toInt()))
//        }.start()
    }

    fun getSearchedList(){
        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<String>()
            var i = 0
            for(j in (mGetSearchList(MainActivity.userId.toInt())?: listOf()).reversed()){

                list.add(j.searchText)
                i++
                if(i==8){
                    break
                }
            }
            if(list.isNotEmpty()){
                SearchListAdapter.hideIcon = true
            }
            else{
                SearchListAdapter.hideIcon = false
            }
            searchedList.postValue(list)
        }
//        Thread{
//            val list = mutableListOf<String>()
//            var i = 0
//            for(j in (mGetSearchList(MainActivity.userId.toInt())?: listOf()).reversed()){
//
//                list.add(j.searchText)
//                i++
//                if(i==8){
//                    break
//                }
//            }
//            if(list.isNotEmpty()){
//                SearchListAdapter.hideIcon = true
//            }
//            else{
//                SearchListAdapter.hideIcon = false
//            }
//            searchedList.postValue(list)
//        }.start()
    }
}