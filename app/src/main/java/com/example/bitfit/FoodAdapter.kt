package com.example.bitfit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoodAdapter(private val context: Context, private val foodItem: List<DisplayBitFit>) :
    RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodTextView: TextView = itemView.findViewById(R.id.FoodItem)
        val calorieCountTextView: TextView = itemView.findViewById(R.id.NumCal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.food_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodAdapter.ViewHolder, position: Int) {
        val current = foodItem[position]
        holder.foodTextView.text = current.food
        holder.calorieCountTextView.text = current.calories.toString()
    }

    override fun getItemCount(): Int {
        return foodItem.size
    }
}
