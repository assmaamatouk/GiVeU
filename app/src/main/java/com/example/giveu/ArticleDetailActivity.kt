package com.example.giveu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ArticleDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)

        val title = intent.getStringExtra("title") ?: ""
        val description = intent.getStringExtra("description") ?: ""
        val imageUrl = intent.getStringExtra("imageUrl") ?: ""
        val phone = intent.getStringExtra("phone") ?: ""
        val location = intent.getStringExtra("location") ?: ""

        val titleView = findViewById<TextView>(R.id.detailTitle)
        val descriptionView = findViewById<TextView>(R.id.detailDescription)
        val phoneView = findViewById<TextView>(R.id.detailPhone)
        val locationView = findViewById<TextView>(R.id.detailLocation)
        val imageView = findViewById<ImageView>(R.id.detailImage)

        titleView.text = title
        descriptionView.text = description
        phoneView.text = "Chiama: $phone"
        locationView.text = "Luogo: $location"

        Glide.with(this)
            .load(imageUrl)
            .into(imageView)

        // Pulsante indietro
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish()
        }

        // Click sul numero di telefono → chiamata
        phoneView.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phone")
            }
            startActivity(dialIntent)
        }

        // Click sul luogo → Apri Maps con link sicuro
        locationView.setOnClickListener {
            val encodedLocation = Uri.encode(location)
            val uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$encodedLocation")
            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
            try {
                startActivity(mapIntent)
            } catch (e: Exception) {
                Toast.makeText(this, "Nessuna app disponibile per aprire la mappa", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
