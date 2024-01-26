package fr.iot.pressf.ui.devices

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.database.*
import fr.iot.pressf.datasource.Device
import kotlinx.coroutines.launch

class DevicesViewModel : ViewModel() {
    var selected: Int = 0
    val devices = MutableLiveData<List<Device>?>()
    fun fetchDevices() {
        viewModelScope.launch {
            val database = Firebase.database
            val devicesRef = database.getReference("/devices")
            devicesRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val values = dataSnapshot.getValue<List<Device>>()
                    if (values != null) {
                        devices.postValue(values)
                    }
                    Log.d("database", "onDataChange: $values")
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w("Database", "Failed to read value.", error.toException())
                }
            })
        }
    }
}