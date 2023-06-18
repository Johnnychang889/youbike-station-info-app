package com.example.ubikeapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.ubikeapp.R

class DropdownAdapter(private val context: Context, private val options: List<String>) : BaseAdapter() {
    override fun getCount(): Int {
        return options.size
    }

    override fun getItem(position: Int): Any {
        return options[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)
        }

        val textView = view!!.findViewById<TextView>(R.id.spinner_item_text)
        textView.text = options[position]

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.spinner_dropdown_item, parent, false)
        }

        val textView = view!!.findViewById<TextView>(R.id.spinner_dropdown_item_text)
        textView.text = options[position]

        return view
    }
}
