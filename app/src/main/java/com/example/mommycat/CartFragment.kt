package com.example.mommycat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class CartFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var database: AppDatabase
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        recyclerView = view.findViewById(R.id.cartRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        database = AppDatabase.getDatabase(requireContext())
        userId = "current_user_id"

        loadCartData()

        view.findViewById<Button>(R.id.checkoutButton).setOnClickListener {
            onCheckoutClicked()
        }

        return view
    }

    private fun loadCartData() {
        lifecycleScope.launch {
            val cartItems = withContext(Dispatchers.IO) {
                database.cartDao().getCartItemsForUser(userId)
            }
            cartAdapter = CartAdapter(cartItems, ::onUpdateClicked, ::onDeleteClicked)
            recyclerView.adapter = cartAdapter
        }
    }

    private fun onUpdateClicked(cartItem: CartData) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                database.cartDao().updateCartItemQuantity(cartItem.id, cartItem.quantity)
            }
            loadCartData()
        }
    }

    private fun onDeleteClicked(cartItem: CartData) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                database.cartDao().deleteCartItem(cartItem.id)
            }
            loadCartData()
        }
    }

    private fun onCheckoutClicked() {
        lifecycleScope.launch {
            val checkoutId = UUID.randomUUID().toString()
            withContext(Dispatchers.IO) {
                database.cartDao().checkoutCart(userId, checkoutId)
            }
            Toast.makeText(requireContext(), "Checkout complete", Toast.LENGTH_SHORT).show()
            loadCartData()
        }
    }
}
