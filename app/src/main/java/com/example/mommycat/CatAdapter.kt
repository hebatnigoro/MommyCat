package com.example.mommycat

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CatAdapter : RecyclerView.Adapter<CatAdapter.CatViewHolder>() {

    private val cats = mutableListOf<Cat>()

    fun submitList(catList: List<Cat>) {
        cats.clear()
        cats.addAll(catList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cat_item, parent, false)
        return CatViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        holder.bind(cats[position])
    }

    override fun getItemCount(): Int = cats.size

    class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val catName: TextView = itemView.findViewById(R.id.text_view_cat_name)
        private val catDescription: TextView = itemView.findViewById(R.id.text_view_cat_description)
        private val catPrice: TextView = itemView.findViewById(R.id.text_view_cat_price)
        private val catImage: ImageView = itemView.findViewById(R.id.image_view_cat)

        fun bind(cat: Cat) {
            catName.text = cat.name
            catDescription.text = cat.description
            catPrice.text = "$${cat.price}"
            Glide.with(itemView.context).load(cat.imageUrl).into(catImage)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, CatDetailActivity::class.java).apply {
                    putExtra("cat", cat)
                }
                itemView.context.startActivity(intent)
            }
        }
    }
}
