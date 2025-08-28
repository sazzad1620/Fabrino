package com.example.fabrinoproject

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class SidebarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sidebar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle category clicks
        fun openCategory(categoryName: String) {
            val intent = Intent(requireContext(), CategoryActivity::class.java)
            intent.putExtra("CATEGORY_NAME", categoryName)
            startActivity(intent)

            (activity as? MainActivity)?.let { mainActivity ->
                mainActivity.closeSidebar()
            }
        }


        view.findViewById<TextView>(R.id.halfSleeveTshirt).setOnClickListener {
            openCategory("Half Sleeve T-shirt")
        }
        view.findViewById<TextView>(R.id.fullSleeveTshirt).setOnClickListener {
            openCategory("Full Sleeve T-shirt")
        }
        view.findViewById<TextView>(R.id.poloTshirt).setOnClickListener {
            openCategory("Polo T-shirt")
        }
        view.findViewById<TextView>(R.id.jeans).setOnClickListener {
            openCategory("Jeans")
        }
        view.findViewById<TextView>(R.id.punjabi).setOnClickListener {
            openCategory("Punjabi")
        }

        view.findViewById<TextView>(R.id.kurti).setOnClickListener {
            openCategory("Kurti")
        }
        view.findViewById<TextView>(R.id.tshirt_women).setOnClickListener {
            openCategory("T-Shirt")
        }
        view.findViewById<TextView>(R.id.pants).setOnClickListener {
            openCategory("Pants")
        }

        view.findViewById<TextView>(R.id.wallet).setOnClickListener {
            openCategory("Wallet")
        }
        view.findViewById<TextView>(R.id.facemask).setOnClickListener {
            openCategory("Face Mask")
        }
    }
}
