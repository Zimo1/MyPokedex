package ru.yodata.mypokedex.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_menu.view.*
import ru.yodata.mypokedex.R

// Фрагмент главного меню
class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Меню из 3 кнопок
        val menuLayout = inflater.inflate(R.layout.fragment_menu, container, false)
        with (menuLayout) {
            // Поиск по набранному пользователем имени
            searchBtn.setOnClickListener { view ->
                val bundle = bundleOf("searchMode" to "MANUAL")
                view.findNavController().
                    navigate(R.id.action_menuFragment_to_pokeSearchFragment, bundle)
            }
            // Случайный поиск
            randomBtn.setOnClickListener { view ->
                val bundle = bundleOf("searchMode" to "RANDOM")
                view.findNavController().
                    navigate(R.id.action_menuFragment_to_pokeSearchFragment, bundle)
            }
            // Избранное
            pokedexBtn.setOnClickListener { view ->
                view.findNavController().navigate(R.id.action_menuFragment_to_pokedexFragment)
            }
        }
        return menuLayout
    }
}