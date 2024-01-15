package fr.iot.pressf.datasource

import com.google.firebase.database.IgnoreExtraProperties
import java.security.Timestamp

@IgnoreExtraProperties
data class Device(val name: String? = null)

@IgnoreExtraProperties
data class Data(val temperature: Float? = null, val humidity: Float? = null, val timestamp: Long? = null)
