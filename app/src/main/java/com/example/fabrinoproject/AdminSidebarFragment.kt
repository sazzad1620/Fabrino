package com.example.fabrinoproject

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class AdminSidebarFragment : Fragment() {

    private lateinit var tvAddItem: TextView
    private lateinit var tvManageItem: TextView
    private lateinit var tvManageTransaction: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_admin_sidebar, container, false)

        tvAddItem = view.findViewById(R.id.tvAddItem)
        tvManageItem = view.findViewById(R.id.tvManageItem)
        tvManageTransaction = view.findViewById(R.id.tvManageTransaction)

        tvAddItem.setOnClickListener {
            // Close drawer first
            (activity as? AdminActivity)?.closeSidebar()

            // Only start AdminActivity if we are not already on it
            if (activity !is AdminActivity) {
                val intent = Intent(requireContext(), AdminActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
            }
        }

        tvManageItem.setOnClickListener {
            (activity as? AdminActivity)?.closeSidebar()
            startActivity(Intent(requireContext(), ManageItemActivity::class.java))
        }

        tvManageTransaction.setOnClickListener {
            (activity as? AdminActivity)?.closeSidebar()
            startActivity(Intent(requireContext(), ManageTransactionActivity::class.java))
        }

        return view
    }
}
