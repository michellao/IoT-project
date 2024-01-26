package fr.iot.pressf.ui.chart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.database.*
import fr.iot.pressf.datasource.Data
import kotlinx.coroutines.launch

class ChartViewModel : ViewModel() {
    val data = MutableLiveData<List<Data>?>()

    fun fetchData(idDevice: Int) {
        viewModelScope.launch {
            val dataRef = Firebase.database.getReference("/data/$idDevice")
            dataRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val value = dataSnapshot.getValue<List<Data>>()
                    data.postValue(value)
                    Log.d("database", "$value")
                }

                override fun onCancelled(db: DatabaseError) {
                    Log.e("database", "Error while fetching data: ", db.toException())
                }
            })
        }
    }
}