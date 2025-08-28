package com.example.fabrinoproject

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.view.GravityCompat

class MainActivity : AppCompatActivity() {

    private lateinit var hamburger: ImageView
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)        //card view recycler
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val items = listOf(
            ItemRepository.getItemByName("Half Sleeve - Black"),
            ItemRepository.getItemByName("Polo - Skyblue"),
            ItemRepository.getItemByName("Full Sleeve - Navy"),
            ItemRepository.getItemByName("Punjabi - Blue"),
            ItemRepository.getItemByName("Kurti - Golden"),
            ItemRepository.getItemByName("Wallet - Brown")
        ).filterNotNull()


        recyclerView.adapter = ItemAdapter(items)

        drawerLayout = findViewById(R.id.drawerLayout)
        hamburger = findViewById(R.id.ivHamburger)

        hamburger.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Load sidebar fragment if not already added
        if (supportFragmentManager.findFragmentById(R.id.sidebarFragment) == null) {
            val sidebarFragment = SidebarFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.sidebarFragment, sidebarFragment)
                .commit()
        }
    }


    fun closeSidebar() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
