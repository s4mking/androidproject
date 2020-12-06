package com.supinternet.aqi.ui.screens.main.tabs.aroundme

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.supinternet.aqi.R
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlinx.android.synthetic.main.fragment_maps_search_card.*
import kotlinx.android.synthetic.main.fragment_maps_station_card.*

class MapsTab : Fragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private var ignoreNextMove = false
    private val markers = mutableMapOf<Marker, Any>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (childFragmentManager.findFragmentById(R.id.maps_tab_gmap) as SupportMapFragment).getMapAsync(
            this
        )

        // Clic sur la flèche dans le champ de recherche
        maps_tab_search_field_arrow.setOnClickListener {

            // On cache le clavier
            val imm: InputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            maps_tab_search_field_arrow.requestFocus()

            // La valeur saisie par l'utilisateur
            val search = maps_tab_search_field.text.toString()

            // TODO 2) Appeler la requête pour la recherche textuelle
        }

        // Dans le champ de recherche, écoute du bouton Enter pour lancer la recherche
        // Appele en réalité le clic sur la flèche (défini au-dessus)
        maps_tab_search_field.setOnKeyListener { _, keyCode, event ->
            if ((event?.action == KeyEvent.ACTION_DOWN) &&
                (keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                maps_tab_search_field_arrow.performClick()
            }

            true
        }

        // Clic sur le bouton Rechercher
        maps_tab_search_zone_button.setOnClickListener {
            // TODO 3) Récupérer la zone de la carte actuellement affichée

            map.projection.visibleRegion.latLngBounds.northeast
        }
    }

    private fun hideDetailsCard() {
        maps_tab_station.visibility = View.GONE
        map.setPadding(0, 0, resources.getDimensionPixelSize(R.dimen.maps_station_card_height), 0)
    }

    // Center camera = recentre la caméra sur la liste des markers
    // (à utiliser pour la recherche textuelle uniquement)
    private fun addMarkers(markers: List<Any>, centerCamera: Boolean = false) {
        map.clear()

        // Limiter à 20 marqueurs
        val stations = if (markers.size > 20) {
            markers.subList(0, 20)
        } else {
            markers
        }

        if (stations.isEmpty()) {
            AlertDialog.Builder(requireActivity())
                .setTitle(getString(R.string.maps_tab_zone_no_station_title))
                .setMessage(getString(R.string.maps_tab_zone_no_station_message))
                .setPositiveButton("Ok", null)
                .show()
        } else {
            val builder = LatLngBounds.Builder()

            for (data in stations) {
//                val marker = MarkerOptions().position(
//                    LatLng(
//                        data.lat,
//                        data.lon
//                    )
//                ).icon(
//                    BitmapDescriptorFactory.fromBitmap(
//                        GoogleMapUtils.getBitmap(
//                            requireContext(),
//                            R.drawable.ic_map_marker
//                        )
//                    )
//                )
//
//                this.markers[map.addMarker(marker)] = data
            }

            if (centerCamera) {
                ignoreNextMove = true
                maps_tab_search_zone_button.visibility = View.INVISIBLE
                map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100))
            }
        }
    }


    private fun showLoading() {
        maps_tab_search_zone_button.visibility = View.INVISIBLE
        maps_tab_search_field.isClickable = false
        maps_tab_search_field_arrow.isClickable = false
        maps_tab_search_field_progress.visibility = View.VISIBLE
        maps_tab_loading_overlay.visibility = View.VISIBLE
        maps_tab_loading_overlay.requestFocus()
        maps_tab_station.visibility = View.GONE
        hideDetailsCard()
    }

    private fun hideLoading() {
        maps_tab_search_zone_button.visibility = View.VISIBLE
        maps_tab_search_field.isClickable = true
        maps_tab_search_field_arrow.isClickable = true
        maps_tab_search_field_progress.visibility = View.INVISIBLE
        maps_tab_loading_overlay.visibility = View.GONE
    }

    private fun showDetailsCard(title: String, date: Long, aqi: Int?) {
        maps_tab_station.visibility = View.VISIBLE

        maps_tab_station_name.text = title
        maps_tab_station_aqi_value.text = aqi?.toString() ?: "?"

        maps_tab_station_aqi_value.setTextColor(
            ContextCompat.getColor(
                requireContext(), when (aqi) {
                    in 0..50 -> R.color.aqi_good
                    in 51..100 -> R.color.aqi_moderate
                    in 101..150 -> R.color.aqi_unhealthy_sensitive
                    in 151..200 -> R.color.aqi_unhealthy
                    in 201..300 -> R.color.aqi_very_unhealthy
                    else -> R.color.aqi_hazardous
                }
            )
        )

        maps_tab_station_date_value.text = DateUtils.getRelativeTimeSpanString(date)
        map.setPadding(0, 0, 0, resources.getDimensionPixelSize(R.dimen.maps_station_card_height))
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        maps_tab_search_zone_button.visibility = View.INVISIBLE
        maps_tab_loading_overlay.requestFocus()

        googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(), R.raw.map_style
            )
        )

        googleMap.setOnCameraMoveStartedListener {
            if (!ignoreNextMove)
                maps_tab_search_zone_button.visibility = View.VISIBLE
        }

        // TODO 6) Lors du clic sur un marqueur
        googleMap.setOnMarkerClickListener { marker ->
            val data = markers[marker]

            // TODO Appeler showDetailsMarker

            true
        }
    }

}