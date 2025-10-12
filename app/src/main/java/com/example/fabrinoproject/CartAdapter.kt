package com.example.fabrinoproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CartAdapter(
    private val context: Context,
    private var cartList: MutableList<CartItem>,
    private val onCartUpdated: () -> Unit  // Callback to notify total price update
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivImage: ImageView = view.findViewById(R.id.ivCartProductImage)
        val tvName: TextView = view.findViewById(R.id.tvCartProductName)
        val tvPrice: TextView = view.findViewById(R.id.tvCartProductPrice)
        val tvSize: TextView = view.findViewById(R.id.tvCartProductSize)
        val tvQuantity: TextView = view.findViewById(R.id.tvQuantity)
        val tvIncrease: TextView = view.findViewById(R.id.tvIncrease)
        val tvDecrease: TextView = view.findViewById(R.id.tvDecrease)
        val ivDelete: ImageView = view.findViewById(R.id.ivDeleteCartItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartList[position]
        val currentUser = auth.currentUser ?: return
        val uid = currentUser.uid

        holder.tvName.text = item.productName
        holder.tvPrice.text = "৳${item.productPrice}"
        holder.tvSize.text = "Size: ${item.size}"
        holder.tvQuantity.text = item.quantity.toString()

        Glide.with(context)
            .load(item.productImageUrl)
            .placeholder(R.drawable.default_image)
            .into(holder.ivImage)

        // Increase quantity
        holder.tvIncrease.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                val currentItem = cartList[pos]
                val newQty = currentItem.quantity + 1
                db.collection("users").document(uid)
                    .collection("cart").document(currentItem.documentId)
                    .update("quantity", newQty)
                    .addOnSuccessListener {
                        currentItem.quantity = newQty
                        notifyItemChanged(pos)
                        onCartUpdated()  // Notify activity to update total
                    }
            }
        }

        // Decrease quantity
        holder.tvDecrease.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                val currentItem = cartList[pos]
                if (currentItem.quantity > 1) {
                    val newQty = currentItem.quantity - 1
                    db.collection("users").document(uid)
                        .collection("cart").document(currentItem.documentId)
                        .update("quantity", newQty)
                        .addOnSuccessListener {
                            currentItem.quantity = newQty
                            notifyItemChanged(pos)
                            onCartUpdated()  // Notify activity to update total
                        }
                }
            }
        }

        // Delete item
        holder.ivDelete.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                val currentItem = cartList[pos]
                db.collection("users").document(uid)
                    .collection("cart").document(currentItem.documentId)
                    .delete()
                    .addOnSuccessListener {
                        if (pos < cartList.size) {
                            cartList.removeAt(pos)
                            notifyItemRemoved(pos)
                            onCartUpdated()  // Notify activity to update total
                        }
                    }
            }
        }
    }

    override fun getItemCount(): Int = cartList.size
}
