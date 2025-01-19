package com.israelaguilar.prohiringandroid.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ColorSpinnerAdapter(
    context: Context,
    private val colors: List<String>,
    private val colorCodes: List<String>
) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, colors) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createColorView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createColorView(position, convertView, parent)
    }

    private fun createColorView(position: Int, convertView: View?, parent: ViewGroup): View {
        val textView = super.getView(position, convertView, parent) as TextView
        textView.text = colors[position]
        textView.setBackgroundColor(Color.parseColor(colorCodes[position]))
        textView.setTextColor(Color.WHITE)
        return textView
    }
}
