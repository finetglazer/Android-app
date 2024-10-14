package com.map.hung.test2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.map.hung.myapplication.R


class TransportSpinnerAdapter(
    context: Context,
    private val transportImages: List<Int>
) : ArrayAdapter<Int>(context, 0, transportImages) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.vehicle_item, parent, false)
        val imageView = view.findViewById<ImageView>(R.id.imgTransportSpinner)
        imageView.setImageResource(transportImages[position])
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.vehicle_item, parent, false)
        val imageView = view.findViewById<ImageView>(R.id.imgTransportSpinner)
        imageView.setImageResource(transportImages[position])
        return view
    }
}
