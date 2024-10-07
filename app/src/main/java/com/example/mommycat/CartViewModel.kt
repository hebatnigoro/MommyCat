package com.example.mommycat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CartRepository
    val cartItems: LiveData<List<CartData>>

    init {
        val cartDao = AppDatabase.getDatabase(application).cartDao()
        repository = CartRepository(cartDao)
        cartItems = repository.cartItems
    }

    fun loadCartItems(userId: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.loadCartItems(userId)
    }

    fun updateCartItem(cartData: CartData) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateCartItem(cartData)
    }

    fun deleteCartItem(cartData: CartData) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteCartItem(cartData)
    }

    fun checkout(userId: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.checkout(userId)
    }
}
