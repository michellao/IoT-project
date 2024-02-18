package fr.iot.pressf.ui.control

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.database.*
import fr.iot.pressf.R
import fr.iot.pressf.databinding.FragmentControlBinding
import fr.iot.pressf.ui.devices.DevicesViewModel

class ControlFragment : Fragment() {
    private var _binding: FragmentControlBinding? = null
    private val devicesViewModel: DevicesViewModel by activityViewModels()
    private lateinit var controlViewModel: ControlViewModel

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentControlBinding.inflate(inflater, container, false)
        controlViewModel = ViewModelProvider(this)[ControlViewModel::class.java]
        val root = binding.root
        val database = Firebase.database.reference
        val indexDevice = devicesViewModel.selected
        val switchFan = binding.fanToggleValue

        controlViewModel.fetchSwitchFan(indexDevice)
        controlViewModel.fetchFanActivation(indexDevice)
        controlViewModel.fetchFanAutomaticToggle(indexDevice)

        controlViewModel.switchFan.observe(viewLifecycleOwner) {
            switchFan.isChecked = it ?: false
        }

        controlViewModel.fanAutomaticToggle.observe(viewLifecycleOwner) {
            binding.fanAutomaticToggle.isChecked = if (it == null) true else !it
        }

        controlViewModel.fanActivationThreshold.observe(viewLifecycleOwner) {
            binding.fanThresholdValue.setText(it.toString())
        }

        switchFan.setOnCheckedChangeListener { _, isChecked ->
            database.child("devices").child(indexDevice.toString()).child("fan").setValue(isChecked)
        }

        val fanActivationThreshold = binding.fanThresholdValue
        fanActivationThreshold.setOnEditorActionListener { _, action, _ ->
            if (action == EditorInfo.IME_ACTION_DONE) {
                val threshold = fanActivationThreshold.text.toString().toFloat()
                database.child("devices").child(indexDevice.toString()).child("activation_threshold").setValue(threshold)
            }
            return@setOnEditorActionListener true
        }

        binding.fanAutomaticToggle.setOnCheckedChangeListener { _, isChecked ->
            database.child("devices").child(indexDevice.toString()).child("manual_control_fan").setValue(!isChecked)
        }

        displayLiveData()
        return root
    }

    fun displayLiveData() {
        val indexDevice = devicesViewModel.selected
        val temperatureTextView = binding.temperatureNow
        val humidityTextView = binding.humidityNow

        val database = Firebase.database.reference
        val latestDataId = database.child("devices").child(indexDevice.toString()).child("latest")
        latestDataId.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataId = snapshot.getValue<Int>()
                val dataRef = database.child("data").child(indexDevice.toString()).child(dataId.toString())
                dataRef.child("temperature").get().addOnSuccessListener {
                    temperatureTextView.text = resources.getString(R.string.temperature) + " " + it.value.toString()
                }
                dataRef.child("humidity").get().addOnSuccessListener {
                    humidityTextView.text = resources.getString(R.string.humidity) + " " + it.value.toString()
                }
            }

            override fun onCancelled(db: DatabaseError) {
                Log.e("database", "Error while fetching data: ", db.toException())
            }

        })
    }
}