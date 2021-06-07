package com.example.recipeapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class FullRecipe : AppCompatActivity() {
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_recipe)

        //Atividade que só recebe os valores da atividade anterior
        //Em seguida exibe essas informações na tela

        val info = intent.extras

        //var info: Bundle? = intent.getBundleExtra(savedInstanceState.toString())
        if(info!!.getString("image") == ""){
            val image: ImageView = findViewById(R.id.full_image)
            image.setImageResource(R.drawable.ic_baseline)
        }
        else{
            //change to put recipe image
            val imageLoad: ImageView = findViewById(R.id.full_image)
            Picasso.with(this).load(info.getString("image")).into(imageLoad)
        }

        val naming: TextView = findViewById(R.id.full_name)
        naming.text = info.getString("title")

        val ingred: TextView = findViewById(R.id.full_ingredients)
        ingred.text = info.getString("ingredients")

        val btnorig: Button = findViewById(R.id.full_url)
        btnorig.setOnClickListener {
            //Toast.makeText(this, "YOU CLICKED!!!", Toast.LENGTH_SHORT).show()
            val url = Intent(Intent.ACTION_VIEW)
            url.data = Uri.parse(info.getString("href"))
            startActivity(url)
        }
    }
}