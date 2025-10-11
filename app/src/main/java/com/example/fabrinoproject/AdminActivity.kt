package com.example.fabrinoproject

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class AdminActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var ivHamburger: ImageView

    private lateinit var textViewUser: TextView
    private lateinit var userInfoLayout: LinearLayout
    private val auth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private val adminEmail = "admin@gmail.com"

    // Form fields
    private lateinit var etItemName: EditText
    private lateinit var etPrice: EditText
    private lateinit var etDescription: EditText
    private lateinit var etQuantity: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var cbS: CheckBox
    private lateinit var cbM: CheckBox
    private lateinit var cbL: CheckBox
    private lateinit var cbXL: CheckBox
    private lateinit var cb2XL: CheckBox
    private lateinit var cbFree: CheckBox
    private lateinit var cbAddPopular: CheckBox
    private lateinit var btnAddItem: Button
    private lateinit var btnSelectImage: Button

    private var selectedImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 100
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

    private val imgbbApiKey = "86c2dd6a00ecb3df8ea2512e91b4d0d8"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        // --- Drawer ---
        drawerLayout = findViewById(R.id.drawerLayoutAdmin)
        ivHamburger = findViewById(R.id.ivHamburger)

        ivHamburger.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        // Load AdminSidebarFragment
        if (supportFragmentManager.findFragmentById(R.id.adminSidebarFragment) == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.adminSidebarFragment, AdminSidebarFragment())
                .commit()
        }

        // --- Top bar ---
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

        // --- Form initialization ---
        etItemName = findViewById(R.id.etItemName)
        etPrice = findViewById(R.id.etPrice)
        etDescription = findViewById(R.id.etDescription)
        etQuantity = findViewById(R.id.etQuantity)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        cbS = findViewById(R.id.cbS)
        cbM = findViewById(R.id.cbM)
        cbL = findViewById(R.id.cbL)
        cbXL = findViewById(R.id.cbXL)
        cb2XL = findViewById(R.id.cb2XL)
        cbFree = findViewById(R.id.cbFree)

        cbAddPopular = findViewById(R.id.cbAddPopular)
        btnAddItem = findViewById(R.id.btnAddItem)
        btnSelectImage = findViewById(R.id.btnSelectImage)
        cbAddPopular.isChecked = false

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter

        btnSelectImage.setOnClickListener { openImageChooser() }
        btnAddItem.setOnClickListener {
            if (selectedImageUri == null) {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            uploadImageToImgBB(selectedImageUri!!)
        }
    }

    // --- Drawer helper ---
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

    // --- Existing functions unchanged ---
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

    private fun openImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImageToImgBB(uri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            val encodedImage = Base64.encodeToString(bytes, Base64.DEFAULT)

            val client = OkHttpClient()
            val formBody = FormBody.Builder()
                .add("key", imgbbApiKey)
                .add("image", encodedImage)
                .build()

            val request = Request.Builder()
                .url("https://api.imgbb.com/1/upload")
                .post(formBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(this@AdminActivity, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val json = response.body?.string()
                        val url = JSONObject(json).getJSONObject("data").getString("url")
                        runOnUiThread { addItemToFirestore(url) }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@AdminActivity, "Upload failed: ${response.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addItemToFirestore(imageUrl: String) {
        val name = etItemName.text.toString().trim()
        val priceText = etPrice.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val quantityText = etQuantity.text.toString().trim()
        val category = spinnerCategory.selectedItem.toString()
        val isPopular = cbAddPopular.isChecked

        if (name.isEmpty() || priceText.isEmpty() || description.isEmpty() || quantityText.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val price = priceText.toDoubleOrNull()
        if (price == null) {
            Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show()
            return
        }

        val quantity = quantityText.toIntOrNull()
        if (quantity == null || quantity < 0) {
            Toast.makeText(this, "Invalid quantity", Toast.LENGTH_SHORT).show()
            return
        }

        val sizes = mutableListOf<String>()
        if (cbS.isChecked) sizes.add("S")
        if (cbM.isChecked) sizes.add("M")
        if (cbL.isChecked) sizes.add("L")
        if (cbXL.isChecked) sizes.add("XL")
        if (cb2XL.isChecked) sizes.add("2XL")
        if (cbFree.isChecked) sizes.add("Free Size")

        if (sizes.isEmpty()) {
            Toast.makeText(this, "Select at least one size", Toast.LENGTH_SHORT).show()
            return
        }

        val item = hashMapOf(
            "name" to name,
            "price" to price,
            "description" to description,
            "imageUrl" to imageUrl,
            "sizes" to sizes,
            "category" to category,
            "isPopular" to isPopular,
            "quantity" to quantity
        )

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
        etQuantity.text.clear()
        cbS.isChecked = false
        cbM.isChecked = false
        cbL.isChecked = false
        cbXL.isChecked = false
        cb2XL.isChecked = false
        cbFree.isChecked = false
        cbAddPopular.isChecked = false
        spinnerCategory.setSelection(0)
        selectedImageUri = null
    }
}
