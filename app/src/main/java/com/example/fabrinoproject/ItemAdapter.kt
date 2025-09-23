package com.example.fabrinoproject

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ItemAdapter(private val itemList: List<Item>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]

        // Load image from URL using Glide
        Glide.with(holder.itemView.context)
            .load(item.imageUrl.trim())
            .placeholder(R.drawable.default_image)
            .into(holder.imageView)

        holder.textViewName.text = item.name
        holder.textViewPrice.text = "৳${item.price}" // dynamically add currency

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("ITEM_NAME", item.name)
            intent.putExtra("ITEM_PRICE", item.price)
            intent.putExtra("ITEM_IMAGE_URL", item.imageUrl)
            intent.putStringArrayListExtra("ITEM_SIZES", ArrayList(item.sizes))
            intent.putExtra("ITEM_DESC", item.description)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = itemList.size
}
