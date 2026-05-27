package com.example.giveu

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import com.example.giveu.model.Article
import androidx.activity.viewModels
import com.example.giveu.viewmodel.ArticleViewModel

class AggiungiArticoloActivity : AppCompatActivity() {

    private lateinit var cameraIcon: ImageView
    private lateinit var backButton: ImageButton
    private lateinit var saveButton: Button
    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var phoneInput: EditText
    private lateinit var locationInput: EditText
    private var imageUrl: String = ""
    private val viewModel: ArticleViewModel by viewModels()

    private val PERMISSION_REQUEST_CODE = 1001

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK && it.data != null) {
            val selectedImageUri: Uri? = it.data!!.data
            imageUrl = selectedImageUri.toString()
            cameraIcon.setImageURI(selectedImageUri)
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK && it.data != null) {
            val photo = it.data!!.extras?.get("data") as? Bitmap
            cameraIcon.setImageBitmap(photo)
            imageUrl = "" // Gestisci come preferisci il salvataggio della foto
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.aggiungi_articolo)

        cameraIcon = findViewById(R.id.cameraIcon)
        backButton = findViewById(R.id.backButton)
        saveButton = findViewById(R.id.saveButton)
        titleInput = findViewById(R.id.titleInput)
        descriptionInput = findViewById(R.id.descriptionInput)
        categorySpinner = findViewById(R.id.categorySpinner)
        phoneInput = findViewById(R.id.phoneInput)
        locationInput = findViewById(R.id.locationInput)


        // Lista etichette visibili all'utente
        val spinnerLabels = listOf(
            "Seleziona categoria...", "Casa", "Elettronica", "Vestiti", "Makeup",
            "Accessori auto", "Accessori sport", "Accessori animali", "Libri"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerLabels)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        backButton.setOnClickListener { onBackPressed() }
        saveButton.setOnClickListener { saveItem() }
        cameraIcon.setOnClickListener { checkAndRequestPermissions() }
    }

    private fun checkAndRequestPermissions() {
        val permissionsNeeded = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.CAMERA)
        }

        val readPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, readPermission) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(readPermission)
        }

        if (permissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsNeeded.toTypedArray(), PERMISSION_REQUEST_CODE)
        } else {
            showImageSourceOptions()
        }
    }

    private fun showImageSourceOptions() {
        val options = arrayOf("Apri galleria", "Scatta foto")
        AlertDialog.Builder(this)
            .setTitle("Scegli immagine")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openGallery()
                    1 -> openCamera()
                }
            }
            .show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            showImageSourceOptions()
        } else {
            Toast.makeText(this, "Permessi richiesti non concessi", Toast.LENGTH_SHORT).show()
        }
    }

    // Mappa etichetta selezionata nel formato categoria usato nel DB
    private fun mapSpinnerToDbCategory(categoryLabel: String): String {
        return when(categoryLabel.lowercase()) {
            "libri" -> "books"
            "elettronica" -> "laptops"
            "vestiti" -> "mens-shirts"
            "casa" -> "home-decoration"
            "makeup" -> "skincare"
            "accessori animali" -> "groceries"
            "accessori sport" -> "sports"
            "accessori auto" -> "automotive"
            else -> categoryLabel.lowercase()
        }
    }

    private fun saveItem() {
        val title = titleInput.text.toString()
        val description = descriptionInput.text.toString()
        val categoryLabel = categorySpinner.selectedItem.toString()

        if (title.isBlank() || description.isBlank() || categoryLabel == "Seleziona categoria...") {
            Toast.makeText(this, "Per favore, completa tutti i campi!", Toast.LENGTH_SHORT).show()
            return
        }

        val dbCategory = mapSpinnerToDbCategory(categoryLabel)

        val article = Article(
            title = title,
            description = description,
            imageUrl = imageUrl,
            phoneNumber = phoneInput.text.toString(), // Puoi sostituire con numero reale
            location = locationInput.text.toString(), // Puoi gestire la geolocalizzazione
            category = dbCategory
        )

        viewModel.insertArticle(article)
        Toast.makeText(this, "Articolo salvato con successo!", Toast.LENGTH_SHORT).show()
        finish()
    }
}
