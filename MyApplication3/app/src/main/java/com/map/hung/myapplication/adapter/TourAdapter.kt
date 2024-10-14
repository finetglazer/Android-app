package com.map.hung.test2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.map.hung.myapplication.R
import com.map.hung.myapplication.model.Tour



class TourAdapter(
    private val tourList: List<Tour>,
    private val onItemClicked: (Tour, Int) -> Unit
) : RecyclerView.Adapter<TourAdapter.TourViewHolder>() {

    class TourViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgTransport: ImageView = itemView.findViewById(R.id.imgTransport)
        val tvItinerary: TextView = itemView.findViewById(R.id.tvItinerary)
        val tvDuration: TextView = itemView.findViewById(R.id.tvDuration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return TourViewHolder(view)
    }

    override fun onBindViewHolder(holder: TourViewHolder, position: Int) {
        val tour = tourList[position]
        holder.imgTransport.setImageResource(tour.transportType)
        holder.tvItinerary.text = tour.itinerary
        holder.tvDuration.text = tour.duration

        // Set click listener on the item
        holder.itemView.setOnClickListener {
            onItemClicked(tour, position)
        }
    }

    override fun getItemCount(): Int {
        return tourList.size
    }
}

