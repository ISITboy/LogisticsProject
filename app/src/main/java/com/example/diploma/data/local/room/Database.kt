package com.example.diploma.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.diploma.domain.models.Consumer
import com.example.diploma.domain.models.Producer
import com.example.diploma.domain.models.Transport
import com.example.mapkitresultproject.data.local.room.ConsumerDao

@Database(entities = [Consumer::class, Producer::class, Transport::class],version = 1)
abstract class Database :RoomDatabase(){
    abstract val producerDao: ProducerDao
    abstract val consumerDao: ConsumerDao
    abstract val transportDao: TransportDao
}