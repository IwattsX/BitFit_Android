package com.example.bitfit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private val food_item = mutableListOf<DisplayBitFit>()
    private val foodAdapter = FoodAdapter(this, food_item)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val BitFitRecyclerViewer : RecyclerView = findViewById(R.id.bitfitItems)
        // TODO: Set up ArticleAdapter with articles

        BitFitRecyclerViewer.layoutManager = LinearLayoutManager(this).also {
            val dividerItemDecoration = DividerItemDecoration(this, it.orientation)
            BitFitRecyclerViewer.addItemDecoration(dividerItemDecoration)


            BitFitRecyclerViewer.adapter = foodAdapter
        }

        lifecycleScope.launch(IO) {
            (application as BitFitApplication).db.articleDao().getAll().collect { databaseList ->
                databaseList.map { entity ->
                    DisplayBitFit(
                        entity.food,
                        entity.calories,
                    )
                }.also { mappedList ->
                    // Use withContext to update the UI on the main thread
                    withContext(Dispatchers.Main) {
                        food_item.clear()
                        food_item.addAll(mappedList)
                        foodAdapter.notifyDataSetChanged()

                        // Log data to check if anything is retrieved from the database
                        mappedList.forEach {
                            Log.d("MainActivityLog", "Food: ${it.food}, Calories: ${it.calories}")
                        }
                    }
                }
            }
        }



        val button = findViewById<Button>(R.id.button)


        button.setOnClickListener{
            val intent = Intent(this, AddEntryActivity::class.java)
            startActivity(intent)
        }


    }
}