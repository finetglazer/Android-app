package com.map.hung.myapplication.test2

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.map.hung.myapplication.R
import com.map.hung.myapplication.model.Tour
import com.map.hung.test2.adapter.TourAdapter
import com.map.hung.test2.adapter.TransportSpinnerAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var itineraryEditText: EditText
    private lateinit var timeEditText: EditText
    private lateinit var addButton: Button
    private lateinit var updateButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var tourAdapter: TourAdapter
    private var selectedPosition = -1

    private val transportImages = listOf(R.drawable.ic_plane, R.drawable.ic_morto, R.drawable.ic_car)
    private val tourList = mutableListOf<Tour>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        // Initialize views
        spinner = findViewById(R.id.spinner)
        itineraryEditText = findViewById(R.id.lich_trinh)
        timeEditText = findViewById(R.id.thoi_gian)
        addButton = findViewById(R.id.searchButton)
        updateButton = findViewById(R.id.UpdateButton)
        recyclerView = findViewById(R.id.recyclerViewTours)

        // Set up spinner with images
        val spinnerAdapter = TransportSpinnerAdapter(this, transportImages)
        spinner.adapter = spinnerAdapter

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        tourAdapter = TourAdapter(tourList) { tour, position ->
            // Handle item click
            itineraryEditText.setText(tour.itinerary)
            timeEditText.setText(tour.duration)
            spinner.setSelection(transportImages.indexOf(tour.transportType))
            selectedPosition = position
        }
        recyclerView.adapter = tourAdapter

        // Add tour button click listener
        addButton.setOnClickListener {
            val itinerary = itineraryEditText.text.toString()
            val duration = timeEditText.text.toString()
            val transport = transportImages[spinner.selectedItemPosition]

            if (itinerary.isNotEmpty() && duration.isNotEmpty()) {
                val newTour = Tour(itinerary, duration, transport)
                tourList.add(newTour)
                tourAdapter.notifyDataSetChanged()

                // Clear input fields
                itineraryEditText.text.clear()
                timeEditText.text.clear()
                spinner.setSelection(0)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Update tour button click listener
        updateButton.setOnClickListener {
            if (selectedPosition != -1) {
                val updatedItinerary = itineraryEditText.text.toString()
                val updatedDuration = timeEditText.text.toString()
                val updatedTransport = transportImages[spinner.selectedItemPosition]

                if (updatedItinerary.isNotEmpty() && updatedDuration.isNotEmpty()) {
                    val updatedTour = Tour(updatedItinerary, updatedDuration, updatedTransport)
                    tourList[selectedPosition] = updatedTour
                    tourAdapter.notifyItemChanged(selectedPosition)

                    // Clear input fields
                    itineraryEditText.text.clear()
                    timeEditText.text.clear()
                    spinner.setSelection(0)
                    selectedPosition = -1
                } else {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please select a tour to update", Toast.LENGTH_SHORT).show()
            }
        }
    }
}