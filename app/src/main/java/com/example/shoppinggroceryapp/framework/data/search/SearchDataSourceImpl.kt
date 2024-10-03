package com.example.shoppinggroceryapp.framework.data.search

import com.core.data.datasource.searchdatasource.SearchDataSource
import com.core.domain.search.SearchHistory
import com.core.domain.user.User
import com.example.shoppinggroceryapp.framework.db.dao.UserDao
import com.example.shoppinggroceryapp.framework.db.entity.search.SearchHistoryEntity

class SearchDataSourceImpl(private val userDao:UserDao):SearchDataSource {
    override fun addSearchQueryInDb(searchHistory: SearchHistory) {
        userDao.addSearchQueryInDb(SearchHistoryEntity(searchHistory.searchText,searchHistory.userId))
    }

    override fun getSearchHistory(userId: Int): List<SearchHistory>? {
        return userDao.getSearchHistory(userId)?.map { SearchHistory(it.searchText,it.userId) }
    }
    override fun getProductForQuery(query: String): List<String>? {
        return userDao.getProductForQuery(query)
    }

    override fun getProductForQueryName(query: String): List<String>? {
        return userDao.getProductForQueryName(query)
    }
}