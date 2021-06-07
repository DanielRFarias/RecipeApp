package com.example.recipeapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var authlog: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        authlog = FirebaseAuth.getInstance()

        val btnlogin: Button = findViewById(R.id.log)

        //Chamada do botão para login
        //único teste usado é se usuário e senha são vazios
        //Se usuário ou senha não estão cadastrados ocorre falha no login
        btnlogin.setOnClickListener{
            val editText1 = findViewById<EditText>(R.id.username)
            val editText2 = findViewById<EditText>(R.id.password)
            val user:String = editText1.text.toString()
            val pass:String = editText2.text.toString()
            //Realiza login com uma simples verificação se
            //usuário e senha não são vazios
            //Mensagens em inglês devido a mensagens de
            //erro em exception serem também em inglês
            if(user.trim().isNotEmpty() && pass.trim().isNotEmpty()){
                authlog.signInWithEmailAndPassword(user, pass)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                val intent = Intent(this, RecipeActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            }
                            else{
                                Toast.makeText(this.applicationContext, "Error: " + task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
            }
            else{
                Toast.makeText(this.applicationContext, "Error: email and password cannot be empty!", Toast.LENGTH_SHORT).show()
            }
        }

        //Passa para a tela de registro
        val btnregis: Button = findViewById(R.id.regis)

        btnregis.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)

        }

    }

}