package com.example.bitfit

import java.io.Serializable

data class DisplayBitFit (
    val food : String?,
    val calories : Int?,
) : java.io.Serializable

/**    @PrimaryKey(autoGenerate = true) val id: Long = 0,
@ColumnInfo(name = "articleAbstract") val food: String?,
@ColumnInfo(name = "headline") val calories: Int?,*/
