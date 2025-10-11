package com.example.fabrinoproject

import android.app.Activity
import android.app.AlertDialog
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ManageItemActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var ivHamburger: ImageView
    private lateinit var textViewUser: TextView
    private lateinit var userInfoLayout: LinearLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinnerCategoryFilter: Spinner
    private lateinit var itemList: MutableList<Item>
    private lateinit var adapter: ManageItemAdapter

    private val auth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private val adminEmail = "admin@gmail.com"

    private val imgbbApiKey = "86c2dd6a00ecb3df8ea2512e91b4d0d8"
    private var selectedImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 101

    // Track current category
    private var selectedCategory: String = "All"

    // Categories
    private val categories = arrayOf(
        "Half Sleeve T-shirt", "Full Sleeve T-shirt", "Polo T-shirt",
        "Jeans", "Punjabi", "Kurti", "T-Shirt", "Pants", "Wallet", "Face Mask"
    )

    private lateinit var currentEditItem: Item
    private var popupWindow: PopupWindow? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_item)

        // Drawer setup
        drawerLayout = findViewById(R.id.drawerLayoutAdmin)
        ivHamburger = findViewById(R.id.ivHamburger)
        ivHamburger.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START)
            else drawerLayout.openDrawer(GravityCompat.START)
        }

        // Sidebar setup
        if (supportFragmentManager.findFragmentById(R.id.adminSidebarFragment) == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.adminSidebarFragment, AdminSidebarFragment())
                .commit()
        }

        // Top bar setup
        textViewUser = findViewById(R.id.textViewUser)
        userInfoLayout = findViewById(R.id.userInfoLayout)
        if (auth.currentUser != null) {
            textViewUser.text =
                if (auth.currentUser?.email == adminEmail) "Hi, Admin" else "Hi, User"
            if (auth.currentUser?.email != adminEmail) fetchUserName()
        }
        userInfoLayout.setOnClickListener {
            if (auth.currentUser != null) showUserInfoPopup()
        }

        // RecyclerView setup
        recyclerView = findViewById(R.id.recyclerViewItems)
        recyclerView.layoutManager = LinearLayoutManager(this)
        itemList = mutableListOf()
        adapter = ManageItemAdapter(
            itemList,
            onEditClick = { item -> showEditPopup(item) },
            onDeleteClick = { item -> deleteItem(item) }
        )
        recyclerView.adapter = adapter

        // Spinner setup
        spinnerCategoryFilter = findViewById(R.id.spinnerCategoryFilter)
        val filterAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            arrayOf("All") + categories
        )
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategoryFilter.adapter = filterAdapter

        spinnerCategoryFilter.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {
                    selectedCategory = spinnerCategoryFilter.selectedItem.toString()
                    if (selectedCategory == "All") loadItems()
                    else loadItemsByCategory(selectedCategory)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        loadItems()
    }

    // -------------------- LOAD ITEMS --------------------
    private fun loadItems() {
        db.collection("items").get()
            .addOnSuccessListener { result ->
                itemList.clear()
                for (doc in result) {
                    val sizesList =
                        (doc.get("sizes") as? List<*>)?.map { it.toString() } ?: listOf()
                    val item = Item(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        price = doc.getDouble("price") ?: 0.0,
                        imageUrl = doc.getString("imageUrl") ?: "",
                        sizes = sizesList,
                        description = doc.getString("description") ?: "",
                        category = doc.getString("category") ?: "",
                        quantity = (doc.getLong("quantity") ?: 0).toInt(),
                        isPopular = doc.getBoolean("isPopular") ?: false
                    )
                    itemList.add(item)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadItemsByCategory(category: String) {
        db.collection("items").whereEqualTo("category", category).get()
            .addOnSuccessListener { result ->
                itemList.clear()
                for (doc in result) {
                    val sizesList =
                        (doc.get("sizes") as? List<*>)?.map { it.toString() } ?: listOf()
                    val item = Item(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        price = doc.getDouble("price") ?: 0.0,
                        imageUrl = doc.getString("imageUrl") ?: "",
                        sizes = sizesList,
                        description = doc.getString("description") ?: "",
                        category = doc.getString("category") ?: "",
                        quantity = (doc.getLong("quantity") ?: 0).toInt(),
                        isPopular = doc.getBoolean("isPopular") ?: false
                    )
                    itemList.add(item)
                }
                adapter.notifyDataSetChanged()
            }
    }

    // -------------------- EDIT ITEM POPUP --------------------
    private fun showEditPopup(item: Item) {
        currentEditItem = item
        val view = LayoutInflater.from(this).inflate(R.layout.popup_edit_item, null)
        popupWindow = PopupWindow(
            view,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        val etName = view.findViewById<EditText>(R.id.etItemName)
        val etPrice = view.findViewById<EditText>(R.id.etPrice)
        val etDescription = view.findViewById<EditText>(R.id.etDescription)
        val etQuantity = view.findViewById<EditText>(R.id.etQuantity)
        val spinnerCategory = view.findViewById<Spinner>(R.id.spinnerCategory)
        val cbPopular = view.findViewById<CheckBox>(R.id.cbAddPopular)
        val btnSelectImage = view.findViewById<Button>(R.id.btnSelectImage)
        val btnUpdate = view.findViewById<Button>(R.id.btnUpdate)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)

        val cbS = view.findViewById<CheckBox>(R.id.cbS)
        val cbM = view.findViewById<CheckBox>(R.id.cbM)
        val cbL = view.findViewById<CheckBox>(R.id.cbL)
        val cbXL = view.findViewById<CheckBox>(R.id.cbXL)
        val cb2XL = view.findViewById<CheckBox>(R.id.cb2XL)
        val cbFree = view.findViewById<CheckBox>(R.id.cbFree)

        etName.setText(item.name)
        etPrice.setText(item.price.toString())
        etDescription.setText(item.description)
        etQuantity.setText(item.quantity.toString())
        cbPopular.isChecked = item.isPopular

        val spinnerAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = spinnerAdapter
        val catIndex = categories.indexOf(item.category)
        if (catIndex >= 0) spinnerCategory.setSelection(catIndex)

        // Restore sizes
        cbS.isChecked = item.sizes.contains("S")
        cbM.isChecked = item.sizes.contains("M")
        cbL.isChecked = item.sizes.contains("L")
        cbXL.isChecked = item.sizes.contains("XL")
        cb2XL.isChecked = item.sizes.contains("2XL")

        btnSelectImage.setOnClickListener { openImageChooser() }

        btnUpdate.setOnClickListener {
            val selectedSizes = mutableListOf<String>()
            if (cbS.isChecked) selectedSizes.add("S")
            if (cbM.isChecked) selectedSizes.add("M")
            if (cbL.isChecked) selectedSizes.add("L")
            if (cbXL.isChecked) selectedSizes.add("XL")
            if (cb2XL.isChecked) selectedSizes.add("2XL")
            if (cbFree.isChecked) selectedSizes.add("Free Size")

            val updatedData = hashMapOf<String, Any>(
                "name" to etName.text.toString().trim(),
                "price" to (etPrice.text.toString().toDoubleOrNull() ?: 0.0),
                "description" to etDescription.text.toString().trim(),
                "category" to spinnerCategory.selectedItem.toString(),
                "quantity" to (etQuantity.text.toString().toIntOrNull() ?: 0),
                "isPopular" to cbPopular.isChecked,
                "sizes" to selectedSizes
            )

            if (selectedImageUri != null)
                uploadImageToImgBB(selectedImageUri!!, updatedData)
            else
                updateItemData(updatedData)
        }

        btnCancel.setOnClickListener { popupWindow?.dismiss() }

        popupWindow?.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0)
    }

    // -------------------- IMAGE PICKING --------------------
    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data?.data != null) {
            selectedImageUri = data.data
            Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show()
        }
    }

    // -------------------- UPLOAD IMAGE + UPDATE --------------------
    private fun uploadImageToImgBB(uri: Uri, updatedData: HashMap<String, Any>) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            val encodedImage = Base64.encodeToString(bytes, Base64.DEFAULT)

            val client = OkHttpClient()
            val formBody = FormBody.Builder()
                .add("key", imgbbApiKey)
                .add("image", encodedImage)
                .build()

            val request =
                Request.Builder().url("https://api.imgbb.com/1/upload").post(formBody).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(this@ManageItemActivity, "Image upload failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val json = response.body?.string()
                        val url = JSONObject(json).getJSONObject("data").getString("url")
                        updatedData["imageUrl"] = url
                        runOnUiThread { updateItemData(updatedData) }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@ManageItemActivity, "Upload failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateItemData(updatedData: HashMap<String, Any>) {
        db.collection("items").document(currentEditItem.id)
            .update(updatedData)
            .addOnSuccessListener {
                Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show()
                popupWindow?.dismiss()
                selectedImageUri = null
                if (selectedCategory == "All") loadItems()
                else loadItemsByCategory(selectedCategory)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
            }
    }

    // -------------------- DELETE ITEM --------------------
    private fun deleteItem(item: Item) {
        AlertDialog.Builder(this)
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete '${item.name}'?")
            .setPositiveButton("Yes") { _, _ ->
                db.collection("items").document(item.id).delete()
                    .addOnSuccessListener {
                        if (selectedCategory == "All") loadItems()
                        else loadItemsByCategory(selectedCategory)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("No", null).show()
    }

    // -------------------- USER INFO --------------------
    private fun fetchUserName() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                textViewUser.text = "Hi, ${doc.getString("firstName") ?: "User"}"
            }
    }

    private fun showUserInfoPopup() {
        val view = LayoutInflater.from(this).inflate(R.layout.popup_user_info, null)
        val popup = PopupWindow(
            view,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )
        val tvFullName = view.findViewById<TextView>(R.id.tvFullName)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        val currentUser = auth.currentUser
        if (currentUser?.email == adminEmail) tvFullName.text = "Admin"
        else {
            val uid = currentUser?.uid
            if (uid != null) {
                db.collection("users").document(uid).get()
                    .addOnSuccessListener {
                        tvFullName.text =
                            "${it.getString("firstName")} ${it.getString("lastName")}"
                    }
            }
        }
        btnLogout.setOnClickListener {
            auth.signOut()
            val i = Intent(this, SignInActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
            finish()
        }
        popup.showAsDropDown(userInfoLayout, 0, 0, Gravity.START)
    }
}
