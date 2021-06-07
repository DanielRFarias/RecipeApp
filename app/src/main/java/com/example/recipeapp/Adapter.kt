package com.example.recipeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.data.FavorDatabase
import com.example.recipeapp.data.Results
import com.squareup.picasso.Picasso

//Lista de favoritos usada para evitar
//acesso ao banco de dados, pelo menos
//quando está sendo modificado
class Adapter(
    private val recipeList: ArrayList<Results>,
    private val listener: OnItemClickListener,
    private val favorList: ArrayList<Results>,
    private val database: FavorDatabase,
    private val check: Int
    ):
    RecyclerView.Adapter<Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout
            .example_item, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = recipeList[position]

        if(currentItem.thumbnail == "") holder.imageView.setImageResource(R.drawable.ic_baseline)
        else{
            //Imagem de uma url
            val imageLoad: ImageView = holder.imageView.findViewById(R.id.recipe_view)
            Picasso.with(holder.imageView.context).load(currentItem.thumbnail).into(imageLoad)
        }
        holder.textView.text = currentItem.title

        if(checkfavor(recipeList[position])){
            holder.btnfav.setImageResource(R.drawable.ic_baseline_favor)
        }
        else{
            holder.btnfav.setImageResource(R.drawable.ic_baseline_favornot)
        }

    }

    override fun getItemCount()= recipeList.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener{
        val imageView: ImageView = itemView.findViewById(R.id.recipe_view)
        val textView: TextView = itemView.findViewById(R.id.name_recipe)

        var btnfav: ImageButton = itemView.findViewById(R.id.button_favor)

        init {
            btnfav.setOnClickListener {
                val rc: Results = recipeList[adapterPosition]
                if(checkfavor(recipeList[adapterPosition])){
                    //Se é favorito deixa de ser se clicado novamente
                    btnfav.setImageResource(R.drawable.ic_baseline_favornot)
                    removefavor(rc)
                    database.favorDao().delete(rc)
                    if(check==1){
                        notifyItemRemoved(adapterPosition)
                    }

                }
                else{
                    //Adicionado aos favoritos
                    btnfav.setImageResource(R.drawable.ic_baseline_favor)
                    addfavor(rc)
                    database.favorDao().insert(rc)
                }

            }
            itemView.setOnClickListener(this)
        }



        override fun onClick(v: View?) {
            val position = adapterPosition

            if (position!= RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

    }


    //Funções criadas para manter a lista do adaptador atualizada
    private fun addfavor(results: Results) {
        favorList.add(results)
    }

    private fun removefavor(results: Results) {
        favorList.remove(results)
    }

    //Verifica se é favorito
    private fun checkfavor(recip: Results): Boolean {
        val found: Int = if(favorList.contains(recip)){
            1
        } else{
            0
        }

        return found != 0
    }
    //!database.favorDao().findRecipe(recip.title).equals("")
    interface OnItemClickListener{
        fun onItemClick(position: Int)

    }


}