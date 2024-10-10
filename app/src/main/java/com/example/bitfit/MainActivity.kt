package com.example.bitfit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
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

        val fragmentManager: FragmentManager = supportFragmentManager

        // define your fragments here
        val bitFitListFragment: Fragment = BitFitListFragment()

        // TODO: Change this to the new ListFragment that will be used for
        val summaryBitFitFragment: Fragment = BitFitListFragment()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        // handle navigation selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.nav_food_items -> fragment = bitFitListFragment
                R.id.nav_summary -> fragment = summaryBitFitFragment
            }
            replaceFragment(fragment)
            true
        }



        // Set up button to navigate to AddEntryActivity
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val intent = Intent(this, AddEntryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun replaceFragment(foodListFragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.BitFit_frame_layout, foodListFragment)
        fragmentTransaction.commit()
    }
}
