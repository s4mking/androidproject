package com.supinternet.aqi.ui.screens.main.tabs.travel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.supinternet.aqi.data.network.AQIAPI
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TravelTab : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        GlobalScope.launch {
            val countries = AQIAPI.getInstance().listCountries().await()
            countries.toString()
        }

        // TODO 1) Modifier pour donner le layout contenant une recherche + RecyclerView
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO 2) Faire la requête pour récupérer les données par pays

        // TODO 3) Ordonner les résultats reçus

        // TODO 4) Créer un Adapter pour la RecyclerView afin d'afficher dans la liste les résultats

        // TODO 5) Ajouter un listener sur la recherche pour filtrer les résultats en fonction de la saisie
    }
}