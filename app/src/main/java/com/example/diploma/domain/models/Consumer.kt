package com.example.diploma.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.diploma.domain.Constants.TABLE_CONSUMER


@Entity(tableName = TABLE_CONSUMER)
data class Consumer(
    @PrimaryKey(autoGenerate = true) val id:Int=0,
    @ColumnInfo(name = "address") val address:String,
    @ColumnInfo(name = "cargo_volume") val volume:String,
    @ColumnInfo(name = "consumer_id") val consumer:Int?=null
)
