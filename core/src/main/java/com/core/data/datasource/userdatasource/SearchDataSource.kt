package com.core.data.datasource.userdatasource

import com.core.domain.search.SearchHistory

interface SearchDataSource {
    fun addSearchQueryInDb(searchHistory: SearchHistory)
    fun getSearchHistory(userId: Int):List<SearchHistory>
}