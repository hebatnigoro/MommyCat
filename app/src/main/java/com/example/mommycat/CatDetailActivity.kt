package com.example.mommycat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_cat_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CatDetailActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cat_detail)

        database = AppDatabase.getDatabase(this)
        userId = "current_user_id" // Replace with actual user ID retrieval logic

        val cat = intent.getParcelableExtra<Cat>("cat")

        cat?.let {
            catNameTextView.text = it.name
            catDescriptionTextView.text = it.description
            catPriceTextView.text = getString(R.string.price_format, it.price)
            Glide.with(this).load(it.imageUrl).into(catImageView)
        }

        addToCartButton.setOnClickListener {
            cat?.let {
                addToCart(it)
            }
        }
    }

    private fun addToCart(cat: Cat) {
        val cartData = CartData(
            id = generateCartItemId(),
            userId = userId,
            catId = cat.id,
            catName = cat.name,
            catPrice = cat.price,
            quantity = 1,
            checkoutId = null
        )

        GlobalScope.launch(Dispatchers.IO) {
            database.cartDao().insertCartItem(cartData)
            runOnUiThread {
                Toast.makeText(this@CatDetailActivity, "Added to cart", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateCartItemId(): String {
        return java.util.UUID.randomUUID().toString()
    }
}
