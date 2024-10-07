package com.example.mommycat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    private var items: List<CartData>,
    private val onUpdate: (CartData) -> Unit,
    private val onDelete: (CartData) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val catName: TextView = itemView.findViewById(R.id.catNameTextView)
        val catPrice: TextView = itemView.findViewById(R.id.catPriceTextView)
        val quantity: EditText = itemView.findViewById(R.id.quantityEditText)
        val subtotal: TextView = itemView.findViewById(R.id.subtotalTextView)
        val updateButton: Button = itemView.findViewById(R.id.updateButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item_layout, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]
        holder.catName.text = item.catName
        holder.catPrice.text = item.catPrice.toString()
        holder.quantity.setText(item.quantity.toString())
        holder.subtotal.text = (item.catPrice * item.quantity).toString()

        holder.updateButton.setOnClickListener {
            val newQuantity = holder.quantity.text.toString().toIntOrNull()
            if (newQuantity != null && newQuantity > 0) {
                item.quantity = newQuantity
                onUpdate(item)
            } else {
                Toast.makeText(holder.itemView.context, "Invalid quantity", Toast.LENGTH_SHORT).show()
            }
        }

        holder.deleteButton.setOnClickListener {
            onDelete(item)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<CartData>) {
        this.items = newItems
        notifyDataSetChanged()
    }
}
