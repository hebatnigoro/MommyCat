package com.example.mommycat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.UUID

class CartRepository(private val cartDao: CartDao) {

    private val _cartItems = MutableLiveData<List<CartData>>()
    val cartItems: LiveData<List<CartData>> get() = _cartItems

    suspend fun loadCartItems(userId: String) {
        val items = cartDao.getCartItemsForUser(userId)
        _cartItems.postValue(items)
    }

    suspend fun updateCartItem(cartData: CartData) {
        cartDao.updateCartItemQuantity(cartData.id, cartData.quantity)
    }

    suspend fun deleteCartItem(cartData: CartData) {
        cartDao.deleteCartItem(cartData.id)
    }

    suspend fun checkout(userId: String) {
        val items = cartDao.getCartItemsForUser(userId)
        val checkoutId = UUID.randomUUID().toString()
        items.forEach { it.checkoutId = checkoutId }
        items.forEach { cartDao.updateCartItemQuantity(it.id, it.quantity) }
        cartDao.checkoutCart(userId, checkoutId)
    }
}
