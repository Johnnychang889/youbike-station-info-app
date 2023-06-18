package com.example.ubikeapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ubikeapp.R
import com.example.ubikeapp.data.Request
import com.example.ubikeapp.rtdb.rtdbManager

class CardAdapter(private var data: List<Request>, private var oftenUseList: ArrayList<String>)
    : RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], oftenUseList)
    }

    override fun getItemCount() = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Request, oftenUseList: ArrayList<String>) {
            itemView.findViewById<TextView>(R.id.card_title).text = item.sna
            itemView.findViewById<TextView>(R.id.card_sbi).text = item.sbi.toString()
            itemView.findViewById<TextView>(R.id.card_bemp).text = item.bemp.toString()
            val star = itemView.findViewById<ImageView>(R.id.star)
            if (oftenUseList.contains(item.sna)) {
                star.setImageResource(android.R.drawable.btn_star_big_on)
            } else {
                star.setImageResource(android.R.drawable.btn_star_big_off)
            }
            star.setOnClickListener {
                val path = "frequent"
                if (star.drawable.constantState == ContextCompat.getDrawable(itemView.context, android.R.drawable.btn_star_big_on)?.constantState) {
                    // 圖片是 "star"，執行相應的操作
                    rtdbManager.findKeyForValue(path, item.sna,
                        onSuccess = { key ->
                            rtdbManager.deleteData(path, key,
                                onSuccess = {
                                    getFrequent()
                                },
                                onFailure = { errorMessage ->
                                    println("Error: $errorMessage")
                                }
                            )
                        },
                        onFailure = { errorMessage ->
                            println("Error: $errorMessage")
                        }
                    )
                    star.setImageResource(android.R.drawable.btn_star_big_off)
                } else {
                    // 圖片不是 "star"，執行其他操作
                    rtdbManager.addData(path, item.sna,
                        onSuccess = {
                            getFrequent()
                        },
                        onFailure = { errorMessage ->
                            println("Error: $errorMessage")
                        }
                    )
                    star.setImageResource(android.R.drawable.btn_star_big_on)
                }
            }
        }

        private fun getFrequent() {
            rtdbManager.getData("frequent",
                onSuccess = { dataSnapshot ->
                    val arrayList: ArrayList<String> = ArrayList()
                    for (childSnapshot in dataSnapshot.children) {
                        val value = childSnapshot.getValue(String::class.java)
                        value?.let { arrayList.add(it) }
                    }
                },
                onFailure = { errorMessage ->
                    println("Error: $errorMessage")
                }
            )
        }
    }

    fun setItems(newItems: List<Request>) {
        data = newItems
        notifyDataSetChanged()
    }

    fun setOftenUseItems(newItems: ArrayList<String>) {
        oftenUseList = newItems
        notifyDataSetChanged()
    }
}
