package com.example.ubikeapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SearchListAdapter(context: Context, private var snaList: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, snaList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        val item = getItem(position)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = item

        return view
    }

    fun filter(nowKeyword: String) {
        snaList = snaList.filter { it.contains(nowKeyword) }
        notifyDataSetChanged()
    }
}
