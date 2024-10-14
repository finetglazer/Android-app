package com.map.hung.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class IconSpinnerAdapter(
    private val context: Context,
    private val icons: List<Int>,
    private val nameIcons: List<String> // List of names aligned with icons
) : BaseAdapter() {

    override fun getCount(): Int = icons.size

    override fun getItem(position: Int): Any = icons[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item_icon, parent, false)

        // Set the icon image
        val imageView: ImageView = view.findViewById(R.id.icon_image)
        imageView.setImageResource(icons[position])

        // Set the text for the corresponding icon
        val textView: TextView = view.findViewById(R.id.icon_text)
        textView.text = nameIcons[position] // Set text from the nameIcons list

        return view
    }
}
