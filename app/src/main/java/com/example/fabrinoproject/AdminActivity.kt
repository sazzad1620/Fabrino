package com.example.fabrinoproject

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.content.Intent

class AdminActivity : AppCompatActivity() {

    private lateinit var textViewUser: TextView
    private lateinit var userInfoLayout: LinearLayout
    private val auth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private val adminEmail = "admin@gmail.com" // fixed email

    // Form fields
    private lateinit var etItemName: EditText
    private lateinit var etPrice: EditText
    private lateinit var etDescription: EditText
    private lateinit var etImageUrl: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var cbS: CheckBox
    private lateinit var cbM: CheckBox
    private lateinit var cbL: CheckBox
    private lateinit var cbXL: CheckBox
    private lateinit var cb2XL: CheckBox
    private lateinit var btnAddItem: Button

    private val categories = listOf(
        "Half Sleeve T-shirt",
        "Full Sleeve T-shirt",
        "Polo T-shirt",
        "Jeans",
        "Punjabi",
        "Kurti",
        "T-Shirt",
        "Pants",
        "Wallet",
        "Face Mask"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        // Top bar
        textViewUser = findViewById(R.id.textViewUser)
        userInfoLayout = findViewById(R.id.userInfoLayout)

        if (auth.currentUser != null) {
            if (auth.currentUser?.email == adminEmail) {
                textViewUser.text = "Hi, Admin"
            } else {
                fetchUserName()
            }
        }

        userInfoLayout.setOnClickListener {
            if (auth.currentUser != null) {
                showUserInfoPopup()
            }
        }

        // Initialize form fields
        etItemName = findViewById(R.id.etItemName)
        etPrice = findViewById(R.id.etPrice)
        etDescription = findViewById(R.id.etDescription)
        etImageUrl = findViewById(R.id.etImageUrl)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        cbS = findViewById(R.id.cbS)
        cbM = findViewById(R.id.cbM)
        cbL = findViewById(R.id.cbL)
        cbXL = findViewById(R.id.cbXL)
        cb2XL = findViewById(R.id.cb2XL)
        btnAddItem = findViewById(R.id.btnAddItem)

        // Setup category spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter

        // Handle Add Item button click
        btnAddItem.setOnClickListener {
            addItemToFirestore()
        }
    }

    private fun fetchUserName() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                val firstName = doc.getString("firstName") ?: "Admin"
                textViewUser.text = "Hi, $firstName"
            }
            .addOnFailureListener {
                textViewUser.text = "Hi, Admin"
            }
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
        if (currentUser?.email == adminEmail) {
            tvFullName.text = "Admin"
        } else {
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
            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        popupWindow.showAsDropDown(userInfoLayout, 0, 0, Gravity.START)
    }

    private fun addItemToFirestore() {
        val name = etItemName.text.toString().trim()
        val priceText = etPrice.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val imageUrl = etImageUrl.text.toString().trim()
        val category = spinnerCategory.selectedItem.toString()

        if (name.isEmpty() || priceText.isEmpty() || description.isEmpty() || imageUrl.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val price = try {
            priceText.toDouble()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show()
            return
        }

        // Collect selected sizes
        val sizes = mutableListOf<String>()
        if (cbS.isChecked) sizes.add("S")
        if (cbM.isChecked) sizes.add("M")
        if (cbL.isChecked) sizes.add("L")
        if (cbXL.isChecked) sizes.add("XL")
        if (cb2XL.isChecked) sizes.add("2XL")

        if (sizes.isEmpty()) {
            Toast.makeText(this, "Select at least one size", Toast.LENGTH_SHORT).show()
            return
        }

        val item = Item(
            name = name,
            price = price,
            description = description,
            imageUrl = imageUrl,
            sizes = sizes,
            category = category
        )

        // Add to Firestore
        db.collection("items")
            .add(item)
            .addOnSuccessListener {
                Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show()
                clearForm()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to add item: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearForm() {
        etItemName.text.clear()
        etPrice.text.clear()
        etDescription.text.clear()
        etImageUrl.text.clear()
        cbS.isChecked = false
        cbM.isChecked = false
        cbL.isChecked = false
        cbXL.isChecked = false
        cb2XL.isChecked = false
        spinnerCategory.setSelection(0)
    }
}
