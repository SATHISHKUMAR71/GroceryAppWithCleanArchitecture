package com.example.shoppinggroceryapp.framework.db.entity.recentlyvieweditems

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecentlyViewedItemsEntity(
    @PrimaryKey(autoGenerate = true)
    var recentlyViewedId:Long,
    var userId:Int,
    var productId:Long
)