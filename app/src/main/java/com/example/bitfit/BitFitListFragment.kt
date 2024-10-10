package com.example.bitfit

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.window.application
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 * Use the [BitFitListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BitFitListFragment : Fragment() {
    private val food_item = mutableListOf<DisplayBitFit>()
    private lateinit var foodRecyclerView: RecyclerView
    private lateinit var foodAdapter: FoodAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun fetchFoods(){
        lifecycleScope.launch(IO) {
            // Correctly access the application through the Activity
            val app = requireActivity().application as BitFitApplication

            // Proceed with fetching data
            app.db.articleDao().getAll().collect { databaseList ->
                val mappedList = databaseList.map { entity ->
                    DisplayBitFit(
                        entity.food,
                        entity.calories
                    )
                }
                withContext(Dispatchers.Main) {
                    food_item.clear()
                    food_item.addAll(mappedList)
                    foodAdapter.notifyDataSetChanged()

                    mappedList.forEach {
                        Log.d("MainActivityLog", "Food: ${it.food}, Calories: ${it.calories}")
                    }
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Change this statement to store the view in a variable instead of a return statement
        val view = inflater.inflate(R.layout.fragment_bit_fit_list, container, false)

        // Add these configurations for the recyclerView and to configure the adapter
        val layoutManager = LinearLayoutManager(context)
        foodRecyclerView = view.findViewById<RecyclerView>(R.id.BitFit_recycler_view)

        foodRecyclerView.layoutManager = layoutManager
        foodRecyclerView.setHasFixedSize(true)

        foodAdapter = FoodAdapter(view.context, food_item)
        foodRecyclerView.adapter = foodAdapter

        // Update the return statement to return the inflated view from above
        return view
    }

    companion object {
        fun newInstance(): BitFitListFragment {
            return BitFitListFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Call the new method within onViewCreated
        fetchFoods()
    }
}