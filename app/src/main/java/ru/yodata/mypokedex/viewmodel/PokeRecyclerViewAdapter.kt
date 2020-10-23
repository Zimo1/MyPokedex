package ru.yodata.mypokedex.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.pokecard.view.*
import ru.yodata.mypokedex.R
import ru.yodata.mypokedex.model.room.entity.PokeCard
import ru.yodata.mypokedex.view.AVATAR_HEIGTH
import ru.yodata.mypokedex.view.AVATAR_WIDTH

// Поддержка работы элемента RecyclerView, в котором отображается список покемонов (Избранное)
class PokeRecyclerViewAdapter(val pokemons: List<PokeCard>):
        RecyclerView.Adapter<PokeRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : PokeRecyclerViewAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.pokecard, parent, false)
        return PokeRecyclerViewAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pokemons.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with (pokemons[position]) {
            holder.id.text = "#" + pokeId.toString()
            holder.name.text = pokeName.capitalize()
            holder.exp.text = pokeExperience.toString()
            holder.height.text = pokeHeight.toString()
            holder.weight.text = pokeWeight.toString()
            Picasso.get()
                .load("${(pokeAnimationURL ?: pokeImageURL) ?: R.drawable.pokesiluet96}")
                //.placeholder(R.drawable.pokesiluet96)
                .error(R.drawable.pokesiluet96)
                .resize(AVATAR_WIDTH, AVATAR_HEIGTH)
                .into(holder.avatar)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id = itemView.pokeIdTV
        val name = itemView.pokeNameTV
        val exp = itemView.pokeExpTV
        val height = itemView.pokeHeightTV
        val weight = itemView.pokeWeightTV
        val avatar = itemView.pokeAvatarIV
    }
}