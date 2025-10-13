package com.example.fabrinoproject

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.LinearLayout

object BottomBarHelper {

    fun setupBottomBar(context: Context, bottomBarView: View, isLoggedIn: Boolean) {
        val homeBtn = bottomBarView.findViewById<LinearLayout>(R.id.llHome)
        val cartBtn = bottomBarView.findViewById<LinearLayout>(R.id.llCart)

        homeBtn.setOnClickListener {
            // Go to MainActivity, clear other activities on top
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            context.startActivity(intent)
        }

        cartBtn.setOnClickListener {
            if (isLoggedIn) {
                context.startActivity(Intent(context, CartActivity::class.java))
            } else {
                context.startActivity(Intent(context, SignInActivity::class.java))
            }
        }
    }
}
