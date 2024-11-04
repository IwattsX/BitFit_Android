package com.example.bitfit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SummaryFragment : Fragment() {
    // TextViews to display average, min, and max calories
    private lateinit var avgCalResView: TextView
    private lateinit var minCalResView: TextView
    private lateinit var maxCalResView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout and find TextViews by ID
        val view = inflater.inflate(R.layout.fragment_summary, container, false)
        avgCalResView = view.findViewById(R.id.AvgCalRes)
        minCalResView = view.findViewById(R.id.MinCalRes)
        maxCalResView = view.findViewById(R.id.MaxCalRes)

        fetchAndDisplayCalories()
        return view
    }

    private fun fetchAndDisplayCalories() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val app = requireActivity().application as BitFitApplication

                // Collect data directly from the flow
                app.db.articleDao().getAll().collect { databaseList ->
                    // Map database entities to DisplayBitFit objects and extract calorie values as a list of Ints
                    val caloriesList = databaseList.mapNotNull { it.calories } // Ensure non-null calories only

                    // Calculate average, min, and max
                    val avgCalories = if (caloriesList.isNotEmpty()) caloriesList.average().toInt() else 0
                    val minCalories = caloriesList.minOrNull() ?: 0
                    val maxCalories = caloriesList.maxOrNull() ?: 0

                    withContext(Dispatchers.Main) {
                        // Display the calculated results
                        avgCalResView.text = avgCalories.toString()
                        minCalResView.text = minCalories.toString()
                        maxCalResView.text = maxCalories.toString()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



    companion object {
        // Factory method to create a new instance of this fragment
        fun newInstance(): SummaryFragment {
            return SummaryFragment()
        }
    }
}
