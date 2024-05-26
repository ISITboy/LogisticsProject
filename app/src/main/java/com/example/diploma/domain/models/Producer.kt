package com.example.diploma.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.diploma.domain.Constants.TABLE_PRODUCER

@Entity(tableName = TABLE_PRODUCER)
data class Producer(
    @PrimaryKey(autoGenerate = true) val id:Int=0,
    @ColumnInfo(name = "address") val address:String
)