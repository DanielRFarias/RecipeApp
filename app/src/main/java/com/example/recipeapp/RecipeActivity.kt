package com.example.recipeapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.recipeapp.api.JsonApi
import com.example.recipeapp.api.SearchIngApi
import com.example.recipeapp.api.SearchNameApi
import com.example.recipeapp.data.ExampleItem
import com.example.recipeapp.data.FavorDatabase
import com.example.recipeapp.data.Results
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton
import kotlin.collections.ArrayList


class RecipeActivity : AppCompatActivity(), Adapter.OnItemClickListener{

    //recipeList recebe as receitas que estão em tela,
    //devido a função on click usar esta variável
    //por isso é usado clear sempre que é recebido novo
    //valor, caso contrário valores antigos serão usados
    private lateinit var recipeList: ArrayList<Results>
    private lateinit var favList: ArrayList<Results>
    private lateinit var db: FavorDatabase


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        //allowMainThreadQueries apenas em caso de testes
        //Em outros aplicativos usar coreutines
        @Singleton
        db = Room.databaseBuilder(
            this@RecipeActivity,
            FavorDatabase::class.java, "favorbase"
        )
            .allowMainThreadQueries()
            .enableMultiInstanceInvalidation()
            .build()

        favList = db.favorDao().getFavourites() as ArrayList<Results>

        //Inicializa a lista
        //evita lateinit val recipelist não é inicializada
        dummyList()

        if(favList.isEmpty()){
            recipeList.clear()
            generateRecipeList()
        }
        else{
            recipeList.clear()
            configureList(favList, 1)
            recipeList = favList
        }

    }

    

    //Chamado quando uma receita especifica é tocada,
    //mostra tela com informações adicionais
    override fun onItemClick(position: Int) {

        val intent = Intent(this, FullRecipe::class.java)
        val info = Bundle()
        info.putString("title", recipeList[position].title)
        info.putString("href", recipeList[position].href)
        info.putString("ingredients", recipeList[position].ingredients)
        info.putString("image", recipeList[position].thumbnail)
        intent.putExtras(info)
        startActivity(intent)

    }


    //Lista exemplo para testar outras funcionalidades
    //Mantido para possiveis testes
   private fun dummyList(){
        val size = 10
        val list =  ArrayList<Results>(size)

        for (i in 0 until size) {
            val item = Results("Vegetable-Pasta Oven Omelet",
                "https://www.google.com",
                "tomato, onions, red pepper, garlic, olive oil, zucchini, cream cheese, vermicelli, eggs, parmesan cheese, milk, italian seasoning, salt, black pepper",
                "https://www.kitano.com.br/wp-content/uploads/2019/07/SSP_1993-Omelete-de-pizza-mussarela-ore%E2%95%A0%C3%BCgano-e-tomate.jpg")
            list.add(item)
        }
        recipeList = list
    }








    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.imeOptions = EditorInfo.IME_ACTION_DONE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String?): Boolean {

                recipeList.clear()
                if(name==""){
                    recipeList.clear()
                    generateRecipeList()
                }
                else if(name!!.contains(" ") || name.contains(",")){
                    generateSearchbyIng(name.replace("\\s".toRegex(), ",").toLowerCase(Locale.ROOT))
                }
                else{
                    generateSearchbyName(name.toLowerCase(Locale.ROOT))
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {

            R.id.action_favoritos -> {
                favList = db.favorDao().getFavourites() as ArrayList<Results>
                configureList(favList, 1)
                recipeList.clear()
                recipeList = favList

                true
            }
            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }







    //Recebe o json e aloca seus valores em uma lista para trabalhar
    private fun generateRecipeList() {
        var item= Results("", "", "", "")
        var recget: ExampleItem

        val retrofit= Retrofit.Builder()
                .baseUrl("http://www.recipepuppy.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(JsonApi::class.java)

        retrofit.getresults().enqueue(object : Callback<ExampleItem> {

            override fun onFailure(call: Call<ExampleItem>, t: Throwable) {
                Toast.makeText(this@RecipeActivity, t.message, Toast.LENGTH_LONG).show()
                return
            }


            override fun onResponse(call: Call<ExampleItem>, response: Response<ExampleItem>) {
                recget = response.body()!!
                val trueList= ArrayList<Results>(recget.results.size)//lista com os valores do json

                for (i in 0 until recget.results.size){
                    item.title = recget.results[i].title
                    item.href = recget.results[i].href
                    item.ingredients = recget.results[i].ingredients
                    item.thumbnail = recget.results[i].thumbnail

                    trueList.add(item)

                    item= Results("", "", "", "")

                }
                recipeList.clear()
                recipeList = trueList
                configureList(recipeList, 0)
            }
        })

    }







    //Função que cria a lista com as receitas obtidas em pesquisa
    //Recebe o nome do prato que é usado para busca
    private fun generateSearchbyName(nome: String) {
        var item= Results("", "", "", "")
        var recget: ExampleItem

        val retrofit= Retrofit.Builder()
                .baseUrl("http://www.recipepuppy.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SearchNameApi::class.java)

        retrofit.getsearchbyname(nome).enqueue(object : Callback<ExampleItem> {

            override fun onFailure(call: Call<ExampleItem>, t: Throwable) {
                Toast.makeText(this@RecipeActivity, t.message, Toast.LENGTH_LONG).show()
                return
            }


            override fun onResponse(call: Call<ExampleItem>, response: Response<ExampleItem>) {
                recget = response.body()!!
                val trueList= ArrayList<Results>(recget.results.size)//lista com os valores do json

                for (i in 0 until recget.results.size){
                    item.title = recget.results[i].title
                    item.href = recget.results[i].href
                    item.ingredients = recget.results[i].ingredients
                    item.thumbnail = recget.results[i].thumbnail

                    trueList.add(item)

                    item= Results("", "", "", "")

                }
                recipeList.clear()
                recipeList = trueList
                configureList(recipeList, 0)
            }
        })

    }





    //Função que cria a lista com as receitas obtidas em pesquisa
    //Recebe lista de ingredientes usada para busca
    private fun generateSearchbyIng(ingred: String) {
        var item= Results("", "", "", "")
        var recget: ExampleItem


        val retrofit= Retrofit.Builder()
                .baseUrl("http://www.recipepuppy.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SearchIngApi::class.java)

        retrofit.getsearchbyingredients(ingred).enqueue(object : Callback<ExampleItem> {

            override fun onFailure(call: Call<ExampleItem>, t: Throwable) {
                Toast.makeText(this@RecipeActivity, t.message, Toast.LENGTH_LONG).show()
                return
            }


            override fun onResponse(call: Call<ExampleItem>, response: Response<ExampleItem>) {
                recget = response.body()!!
                val trueList= ArrayList<Results>(recget.results.size)//lista com os valores do json

                for (i in 0 until recget.results.size){
                    item.title = recget.results[i].title
                    item.href = recget.results[i].href
                    item.ingredients = recget.results[i].ingredients
                    item.thumbnail = recget.results[i].thumbnail

                    trueList.add(item)

                    item= Results("", "", "", "")

                }
                recipeList.clear()
                recipeList = trueList
                configureList(recipeList, 0)
            }
        })

    }














    //Cria a lista mostrada na tela com as receitas passadas na List
    //checking usado para caso estar na lista de favoritos
    //nesse caso se a receita na lista deixa de ser favorita
    //ela é removida da tela
    private fun configureList(List: ArrayList<Results>, checking: Int) {
        val recyclerview: RecyclerView = findViewById(R.id.recipe_list)


        recyclerview.adapter = Adapter(List, this, favList, db, checking)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.setHasFixedSize(false)

    }

}
