package com.example.ubikeapp.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.SearchView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ubikeapp.R
import com.example.ubikeapp.StartActivity
import com.example.ubikeapp.data.data
import com.example.ubikeapp.adapter.CardAdapter
import com.example.ubikeapp.adapter.DropdownAdapter
import com.example.ubikeapp.adapter.SearchListAdapter
import com.example.ubikeapp.auth.AuthManager
import com.example.ubikeapp.rtdb.rtdbManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    val Taipei_Administrative_District = listOf("士林區","北投區","內湖區","中山區","大同區","松山區","信義區","南港區","文山區","大安區","中正區","萬華區","全部")
    private lateinit var recyclerView: RecyclerView
    private lateinit var card_adapter: CardAdapter
    private lateinit var spinner: Spinner
    private lateinit var searchView: SearchView
    private lateinit var searchListView: ListView
    private lateinit var oftenUseList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        oftenUseList = arrayListOf()
        recyclerView = findViewById<RecyclerView>(R.id.card_list)
        spinner = findViewById<Spinner>(R.id.spinner)
        searchView = findViewById<SearchView>(R.id.searchView)
        searchListView = findViewById<ListView>(R.id.search_listView)

        startMainServices()

    }

    private fun startMainServices() {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("frequent")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 當數據發生變化時調用
                setOftenUseList()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 當監聽被取消時調用
            }
        })


        card_adapter = CardAdapter(data.getCurrentDataList(), oftenUseList)
        var llm = LinearLayoutManager(this)
        var nowSelectedArea = "士林區"
        var nowKeyword = ""
        recyclerView.layoutManager = llm
        recyclerView.adapter = card_adapter

        searchView.setOnSearchClickListener {
            searchListView.visibility = View.VISIBLE
        }
        searchView.setOnCloseListener {
            searchListView.visibility = View.GONE
            false
        }

        findViewById<Button>(R.id.button_top).setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }
        findViewById<Button>(R.id.button_signout).setOnClickListener {
            AuthManager.signOut()
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
        }

        var isOftenUseClicked = false
        val button_often = findViewById<Button>(R.id.button_often)
        button_often.setOnClickListener {
            if (isOftenUseClicked == false) {
                data.oftenUseFilter(oftenUseList)
                card_adapter.setItems(data.getCurrentDataList())
                button_often.setBackground(resources.getDrawable(R.drawable.text_border))
                button_often.setTextColor(Color.WHITE)
                isOftenUseClicked = true
            } else {
                data.AreaAndKeywordFilter(nowSelectedArea, nowKeyword)
                card_adapter.setItems(data.getCurrentDataList())
                button_often.setBackground(resources.getDrawable(R.drawable.transparent_background))
                button_often.setTextColor(Color.BLACK)
                isOftenUseClicked = false
            }
        }

        spinner.adapter = DropdownAdapter(this, Taipei_Administrative_District)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                nowSelectedArea = parent.getItemAtPosition(position).toString()
                data.AreaAndKeywordFilter(nowSelectedArea, nowKeyword)
                card_adapter.setItems(data.getCurrentDataList())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(this@MainActivity, "請選擇一個項目", Toast.LENGTH_SHORT).show()
            }
        }

        var searchListViewAdapter = SearchListAdapter(this, data.getCurrentSnaList())
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                nowKeyword = newText
                data.AreaAndKeywordFilter(nowSelectedArea, nowKeyword)
                searchListViewAdapter.clear()
                searchListViewAdapter.addAll(data.getCurrentSnaList())
                searchListViewAdapter.filter(nowKeyword)
                searchListView.adapter = searchListViewAdapter
                return true
            }
        })

        searchListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            nowKeyword = data.getCurrentSnaList()[position]
            searchView.setQuery(nowKeyword, true)
            data.AreaAndKeywordFilter(nowSelectedArea, nowKeyword)
            card_adapter.setItems(data.getCurrentDataList())
        }
    }

    private fun setOftenUseList() {
        val path = "frequent"

        rtdbManager.getData(path,
            onSuccess = { dataSnapshot ->
                val arrayList: ArrayList<String> = ArrayList()
                for (childSnapshot in dataSnapshot.children) {
                    val value = childSnapshot.getValue(String::class.java)
                    value?.let { arrayList.add(it) }
                }
                oftenUseList = arrayList
                card_adapter.setOftenUseItems(oftenUseList)
            },
            onFailure = { errorMessage ->
                println("Error: $errorMessage")
            }
        )
    }

}