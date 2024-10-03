package com.core.data.datasource.searchdatasource

import com.core.domain.search.SearchHistory

interface SearchDataSource {
    fun addSearchQueryInDb(searchHistory: SearchHistory)
    fun getSearchHistory(userId: Int):List<SearchHistory>?
    fun getProductForQuery(query:String):List<String>?
    fun getProductForQueryName(query:String):List<String>?
}