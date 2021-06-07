package com.example.recipeapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var authreg: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        authreg = FirebaseAuth.getInstance()
        val btnreg: Button = findViewById(R.id.login2)

        //Função que executa o registro de um usuário
        //Também são realizados testes básicos
        //Se usuário e senha são vazios e se usuário é válido
        //Para o segundo é chamada outra função mais abaixo
        btnreg.setOnClickListener{
            val editTextr1 = findViewById<EditText>(R.id.usernamereg)
            val editTextr2 = findViewById<EditText>(R.id.passwordreg)
            val userreg:String = editTextr1.text.toString()
            val passreg:String = editTextr2.text.toString()
            if(userreg.trim().isNotEmpty() && passreg.trim().isNotEmpty()){
                if (isEmailValid(userreg)){
                    authreg.createUserWithEmailAndPassword(userreg, passreg)
                            .addOnCompleteListener { task ->
                                if(task.isSuccessful){
                                    firebaseUserID = authreg.currentUser!!.uid
                                    refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)
                                    val userHashMap = HashMap<String, Any>()
                                    userHashMap["uid"] = firebaseUserID
                                    userHashMap["email"] = userreg
                                    userHashMap["password"] = passreg

                                    refUsers.updateChildren(userHashMap)
                                            .addOnCompleteListener { task ->
                                                if(task.isSuccessful){
                                                    val intent = Intent(this, MainActivity::class.java)
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                                    startActivity(intent)
                                                }
                                            }
                                }
                                else{
                                    Toast.makeText(this.applicationContext, "Error: " + task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                                }
                            }
                }
                else{
                    Toast.makeText(this.applicationContext, "Error: invalid mail adress", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this.applicationContext, "Error: email and password cannot be empty!", Toast.LENGTH_SHORT).show()
            }

        }

    }
    //Verificação de endereço válido
    //Exemplo x@z.com pode ser usado
    private fun isEmailValid(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}