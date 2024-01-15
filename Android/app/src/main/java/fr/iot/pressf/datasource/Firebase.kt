package fr.iot.pressf.datasource

import com.google.firebase.database.IgnoreExtraProperties
import java.security.Timestamp

@IgnoreExtraProperties
data class Device(val fan: Boolean? = null, val name: String? = null, val latest: Int? = null)

@IgnoreExtraProperties
data class Data(val temperature: Float? = null, val humidity: Float? = null, val timestamp: Long? = null)
