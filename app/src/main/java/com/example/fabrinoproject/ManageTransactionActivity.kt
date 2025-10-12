package com.example.fabrinoproject

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class Transaction(
    var transId: String = "",
    var userName: String = "",
    var userEmail: String = "",
    var items: List<CartItem> = emptyList(),
    var totalPrice: Double = 0.0,
    var status: String = "Order received"
)

class ManageTransactionActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var ivHamburger: ImageView
    private lateinit var textViewUser: TextView
    private lateinit var userInfoLayout: LinearLayout
    private lateinit var dimBackground: View
    private lateinit var rvTransactions: RecyclerView

    private val auth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private val adminEmail = "admin@gmail.com"

    private val transactionList = mutableListOf<Transaction>()
    private lateinit var adapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_transaction)

        // --- Dim background setup ---
        dimBackground = View(this).apply {
            setBackgroundColor(0x99000000.toInt())
            visibility = View.GONE
        }
        addContentView(
            dimBackground,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        )

        // --- Drawer setup ---
        drawerLayout = findViewById(R.id.drawerLayoutAdmin)
        ivHamburger = findViewById(R.id.ivHamburger)
        ivHamburger.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START)
            else drawerLayout.openDrawer(GravityCompat.START)
        }

        // Load Sidebar Fragment
        if (supportFragmentManager.findFragmentById(R.id.adminSidebarFragment) == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.adminSidebarFragment, AdminSidebarFragment())
                .commit()
        }

        // --- Top Bar & User Info ---
        textViewUser = findViewById(R.id.textViewUser)
        userInfoLayout = findViewById(R.id.userInfoLayout)

        if (auth.currentUser != null) {
            if (auth.currentUser?.email == adminEmail) {
                textViewUser.text = "Hi, Admin"
            } else fetchUserName()
        }

        userInfoLayout.setOnClickListener { if (auth.currentUser != null) showUserInfoPopup() }

        // --- RecyclerView for transactions ---
        rvTransactions = findViewById(R.id.rvTransactions)
        rvTransactions.layoutManager = LinearLayoutManager(this)
        adapter = TransactionAdapter(transactionList)
        rvTransactions.adapter = adapter

        loadTransactions()
    }

    private fun fetchUserName() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                val firstName = doc.getString("firstName") ?: "Admin"
                textViewUser.text = "Hi, $firstName"
            }
            .addOnFailureListener { textViewUser.text = "Hi, Admin" }
    }

    private fun showUserInfoPopup() {
        val inflater = LayoutInflater.from(this)
        val popupView = inflater.inflate(R.layout.popup_user_info, null)
        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        val tvFullName = popupView.findViewById<TextView>(R.id.tvFullName)
        val btnLogout = popupView.findViewById<Button>(R.id.btnLogout)
        val currentUser = auth.currentUser
        if (currentUser?.email == adminEmail) tvFullName.text = "Admin"
        else {
            val uid = currentUser?.uid
            if (uid != null) {
                db.collection("users").document(uid).get()
                    .addOnSuccessListener { doc ->
                        val firstName = doc.getString("firstName") ?: ""
                        val lastName = doc.getString("lastName") ?: ""
                        tvFullName.text = "$firstName $lastName"
                    }
            }
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            finish()
        }

        dimBackground.visibility = View.VISIBLE
        popupWindow.setOnDismissListener { dimBackground.visibility = View.GONE }
        popupWindow.showAsDropDown(userInfoLayout, 0, 0, Gravity.START)
    }

    private fun loadTransactions() {
        db.collection("transactions").get()
            .addOnSuccessListener { docs ->
                transactionList.clear()
                for (doc in docs) {
                    val trans = doc.toObject(Transaction::class.java)
                    trans.transId = doc.id

                    // Fix total price
                    val totalAmount = doc.getDouble("totalAmount") ?: 0.0
                    trans.totalPrice = totalAmount

                    transactionList.add(trans)
                }
                adapter.notifyDataSetChanged()
            }
    }


    fun closeSidebar() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START)
        else super.onBackPressed()
    }

    // --- Adapter ---
    inner class TransactionAdapter(private val list: List<Transaction>) :
        RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

        inner class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvTransId: TextView = view.findViewById(R.id.tvTransId)
            val tvUserName: TextView = view.findViewById(R.id.tvUserName)
            val tvUserEmail: TextView = view.findViewById(R.id.tvUserEmail)
            val btnViewDetails: Button = view.findViewById(R.id.btnViewDetails)
            val spinnerStatus: Spinner = view.findViewById(R.id.spinnerStatus)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_transaction_row, parent, false)
            return TransactionViewHolder(view)
        }

        override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
            val item = list[position]
            holder.tvTransId.text = "Order ID: ${item.transId}"
            holder.tvUserName.text = item.userName
            holder.tvUserEmail.text = item.userEmail

            // Status Spinner
            val statusOptions = listOf(
                "Order received", "Canceled", "Packed", "Ready to ship",
                "Shipped", "Out for Delivery", "Delivered"
            )
            val spinnerAdapter = ArrayAdapter(
                this@ManageTransactionActivity,
                android.R.layout.simple_spinner_item,
                statusOptions
            )
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            holder.spinnerStatus.adapter = spinnerAdapter
            holder.spinnerStatus.setSelection(statusOptions.indexOf(item.status))
            holder.spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, pos: Int, id: Long
                ) {
                    val newStatus = statusOptions[pos]
                    if (newStatus != item.status) {
                        db.collection("transactions").document(item.transId)
                            .update("status", newStatus)
                        item.status = newStatus
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            // View details popup
            holder.btnViewDetails.setOnClickListener {
                showTransactionDetailsPopup(item)
            }
        }

        override fun getItemCount(): Int = list.size
    }

    private fun showTransactionDetailsPopup(transaction: Transaction) {
        val inflater = LayoutInflater.from(this)
        val popupView = inflater.inflate(R.layout.popup_transaction_details, null)
        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        val container = popupView.findViewById<LinearLayout>(R.id.llTransactionItems)
        container.removeAllViews()
        transaction.items.forEach { cartItem ->
            val tv = TextView(this)
            tv.text = "${cartItem.productName} | Size: ${cartItem.size} | Qty: ${cartItem.quantity} | Price: ৳${cartItem.productPrice}"
            tv.setPadding(8, 8, 8, 8)
            container.addView(tv)
        }

        val tvTotal = popupView.findViewById<TextView>(R.id.tvTotalPricePopup)
        tvTotal.text = "Total: ৳${transaction.totalPrice}"

        val btnClose = popupView.findViewById<Button>(R.id.btnClosePopup)
        btnClose.setOnClickListener { popupWindow.dismiss() }

        dimBackground.visibility = View.VISIBLE
        popupWindow.setOnDismissListener { dimBackground.visibility = View.GONE }
        popupWindow.showAtLocation(rvTransactions, Gravity.CENTER, 0, 0)
    }
}
