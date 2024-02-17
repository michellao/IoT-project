package fr.iot.pressf.ui.control

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.database.*
import kotlinx.coroutines.launch

class ControlViewModel : ViewModel() {
    val switchFan = MutableLiveData<Boolean?>()
    val fanActivationThreshold = MutableLiveData<Float?>()
    val fanAutomaticToggle = MutableLiveData<Boolean?>()

    fun fetchSwitchFan(idDevice: Int) {
        viewModelScope.launch {
            val database = Firebase.database.reference
            val switchFanRef = database.child("devices").child(idDevice.toString()).child("fan")
            switchFanRef.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("database_control", "Data fetched: $snapshot")
                    val value = snapshot.getValue<Boolean>()
                    switchFan.postValue(value)
                }

                override fun onCancelled(db: DatabaseError) {
                    Log.e("database", "Error while fetching data: ", db.toException())
                }
            })
        }
    }

    fun fetchFanActivation(idDevice: Int) {
        viewModelScope.launch {
            val database = Firebase.database.reference
            val fanActivationThresholdRef = database.child("devices").child(idDevice.toString()).child("activation_threshold")
            fanActivationThresholdRef.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("database_control", "Data fetched: $snapshot")
                    val value = snapshot.getValue<Float>()
                    fanActivationThreshold.postValue(value)
                }

                override fun onCancelled(db: DatabaseError) {
                    Log.e("database", "Error while fetching data: ", db.toException())
                }
            })
        }
    }

    fun fetchFanAutomaticToggle(idDevice: Int) {
        viewModelScope.launch {
            val database = Firebase.database.reference
            val fanAutomaticToggleRef = database.child("devices").child(idDevice.toString()).child("manual_control_fan")
            fanAutomaticToggleRef.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue<Boolean>()
                    fanAutomaticToggle.postValue(value)
                }

                override fun onCancelled(db: DatabaseError) {
                    Log.e("database", "Error while fetching data: ", db.toException())
                }
            })
        }
    }
}