package com.core.data.repository

import com.core.data.datasource.searchdatasource.SearchDataSource
import com.core.domain.search.SearchHistory

class SearchRepository(private val searchDataSource: SearchDataSource) {
    fun getProductsForQuery(query:String):List<String>?{
        return searchDataSource.getProductForQuery(query)
    }

    fun getSearchHistory(userId: Int):List<SearchHistory>?{
        return searchDataSource.getSearchHistory(userId)
    }

    fun getProductForQueryName(query: String):List<String>?{
        return searchDataSource.getProductForQueryName(query)
    }

    fun addSearchQueryInDb(searchHistory: SearchHistory){
        searchDataSource.addSearchQueryInDb(searchHistory)
    }
}