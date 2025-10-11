package com.example.fabrinoproject

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object UserPopupHelper {

    private val auth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    fun showUserPopup(
        context: Context,
        anchorView: View
    ) {
        // Inflate the popup layout
        val inflater = LayoutInflater.from(context)
        val popupView: View = inflater.inflate(R.layout.popup_user_info, null)

        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        // Dim background
        val dimBackground = View(context).apply {
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.black))
            alpha = 0.5f
            visibility = View.GONE
        }

        if (context is android.app.Activity) {
            context.addContentView(
                dimBackground,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
            )
        }

        dimBackground.visibility = View.VISIBLE
        popupWindow.setOnDismissListener {
            dimBackground.visibility = View.GONE
        }

        // Setup full name
        val tvFullName = popupView.findViewById<TextView>(R.id.tvFullName)
        val btnLogout = popupView.findViewById<Button>(R.id.btnLogout)
        val uid = auth.currentUser?.uid

        if (uid != null) {
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val firstName = document.getString("firstName") ?: ""
                        val lastName = document.getString("lastName") ?: ""
                        tvFullName.text = "$firstName $lastName"
                    }
                }
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            context.startActivity(Intent(context, SignInActivity::class.java))
            if (context is android.app.Activity) {
                context.finish()
            }
        }

        // Show popup
        popupWindow.showAsDropDown(anchorView, 0, 0, Gravity.START)
    }
}
