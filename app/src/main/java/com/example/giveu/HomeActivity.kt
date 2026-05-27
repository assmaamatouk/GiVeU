package com.example.giveu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Pulsante per aggiungere un nuovo articolo
        val addButton: ImageButton = findViewById(R.id.addButton)
        addButton.setOnClickListener {
            val intent = Intent(this, AggiungiArticoloActivity::class.java)
            startActivity(intent)
        }

        // Categorie con mapping coerente al DB / filtro
        findViewById<LinearLayout>(R.id.categoryBook).setOnClickListener {
            openArticlesActivity("books")
        }

        findViewById<LinearLayout>(R.id.categoryComputer).setOnClickListener {
            openArticlesActivity("laptops")
        }

        findViewById<LinearLayout>(R.id.categoryDress).setOnClickListener {
            openArticlesActivity("mens-shirts", "womens-dresses")
        }

        findViewById<LinearLayout>(R.id.categoryHome).setOnClickListener {
            openArticlesActivity("home-decoration")
        }

        findViewById<LinearLayout>(R.id.categoryMakeup).setOnClickListener {
            openArticlesActivity("skincare")
        }

        findViewById<LinearLayout>(R.id.categoryCar).setOnClickListener {
            openArticlesActivity("automotive")
        }

        findViewById<LinearLayout>(R.id.categorySport).setOnClickListener {
            openArticlesActivity("sports") // o altro, in base a cosa hai nel DB
        }

        findViewById<LinearLayout>(R.id.categoryPet).setOnClickListener {
            openArticlesActivity("groceries") // esempio: alimenti animali, se usi "groceries"
        }
    }

    private fun openArticlesActivity(vararg categories: String) {
        if (categories.isEmpty()) {
            Toast.makeText(this, "Categoria non disponibile.", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, ArticlesActivity::class.java)
        intent.putExtra("categories", categories)
        startActivity(intent)
    }
}
