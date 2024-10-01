package com.example.bitfit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

public class AddEntryActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_item)


        val food_item = findViewById<EditText>(R.id.AddFood)
        val calories_item = findViewById<EditText>(R.id.AddCal)



        val submit_btn = findViewById<Button>(R.id.AddNewBtn)


        submit_btn.setOnClickListener {
            if (food_item.text.isEmpty() || calories_item.text.isEmpty()) {
                Toast.makeText(it.context, "One or both text items are blank", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch(IO) {
                    (application as BitFitApplication).db.articleDao().insert(
                        BitFitEntity(
                            food = food_item.text.toString(),
                            calories = calories_item.text.toString().toInt()
                        )
                    )
                    // Switch to the main thread after inserting to navigate back to MainActivity
                    withContext(Dispatchers.Main) {
                        val intent = Intent(this@AddEntryActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }




    }
}
