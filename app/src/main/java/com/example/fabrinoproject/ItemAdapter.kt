package com.example.fabrinoproject

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(private val itemList: List<Item>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {  //hold ui element of card
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder { //create new card view from xml
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {  //add card element
        val item = itemList[position]
        holder.imageView.setImageResource(item.imageResId)
        holder.textViewName.text = item.name
        holder.textViewPrice.text = item.price


        holder.itemView.setOnClickListener {  //make card clickable
            val context = holder.itemView.context
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("ITEM_NAME", item.name)
            intent.putExtra("ITEM_PRICE", item.price)
            intent.putExtra("ITEM_IMAGE", item.imageResId)
            intent.putStringArrayListExtra("ITEM_SIZES", ArrayList(item.sizes))
            intent.putExtra("ITEM_DESC", item.description)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = itemList.size
}
