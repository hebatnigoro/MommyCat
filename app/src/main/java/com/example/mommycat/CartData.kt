package com.example.mommycat

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartData(
    @PrimaryKey val id: String,
    val userId: String,
    val catId: String,
    val catName: String,
    val catPrice: Double,
    var quantity: Int,
    var checkoutId: String? = null
)

