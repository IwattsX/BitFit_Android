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
    // List to hold display items
    private val food_item = mutableListOf<DisplayBitFit>()
    private val foodAdapter = FoodAdapter(this, food_item)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // RecyclerView setup
        val bitFitRecyclerViewer: RecyclerView = findViewById(R.id.bitfitItems)
        bitFitRecyclerViewer.layoutManager = LinearLayoutManager(this).also {
            val dividerItemDecoration = DividerItemDecoration(this, it.orientation)
            bitFitRecyclerViewer.addItemDecoration(dividerItemDecoration)
        }
        bitFitRecyclerViewer.adapter = foodAdapter

        // Fetch data from the database in a coroutine
        lifecycleScope.launch(IO) {
            // Collect items from the database as a Flow
            (application as BitFitApplication).db.articleDao().getAll().collect { databaseList ->
                databaseList.map { entity ->
                    // Map the database entity to a display-friendly model
                    DisplayBitFit(
                        entity.food,
                        entity.calories
                    )
                }.also { mappedList ->
                    // Switch to the Main thread to update UI
                    withContext(Dispatchers.Main) {
                        food_item.clear() // Clear current list
                        food_item.addAll(mappedList) // Add new items
                        foodAdapter.notifyDataSetChanged() // Notify adapter

                        // Log to check retrieved data
                        mappedList.forEach {
                            Log.d("MainActivityLog", "Food: ${it.food}, Calories: ${it.calories}")
                        }
                    }
                }
            }
        }

        // Set up button to navigate to AddEntryActivity
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val intent = Intent(this, AddEntryActivity::class.java)
            startActivity(intent)
        }
    }
}
