package fr.iot.pressf.datasource

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Device(val name: String? = null)

@IgnoreExtraProperties
data class Data(val temperature: Float, val humidity: Float)

