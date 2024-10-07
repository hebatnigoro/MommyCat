package com.example.mommycat
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CartDataDao {
    @Insert
    suspend fun insert(cartData: CartData)

    @Query("SELECT * FROM cart WHERE userId = :userId AND checkoutId IS NULL")
    suspend fun getCartByUserId(userId: String): List<CartData>
}
