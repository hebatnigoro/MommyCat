package com.example.mommycat

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CartDao {

    @Query("SELECT * FROM cart WHERE userId = :userId AND checkoutId IS NULL")
    suspend fun getCartItemsForUser(userId: String): List<CartData>

    @Insert
    suspend fun insertCartItem(cartItem: CartData)

    @Query("UPDATE cart SET quantity = :quantity WHERE id = :id")
    suspend fun updateCartItemQuantity(id: String, quantity: Int)

    @Query("DELETE FROM cart WHERE id = :id")
    suspend fun deleteCartItem(id: String)

    @Query("UPDATE cart SET checkoutId = :checkoutId WHERE userId = :userId AND checkoutId IS NULL")
    suspend fun checkoutCart(userId: String, checkoutId: String)
}
