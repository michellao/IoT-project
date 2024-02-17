package fr.iot.pressf.ui.control

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.database.database
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
                val threshold = fanActivationThreshold.text.toString().toInt()
                database.child("devices").child(indexDevice.toString()).child("activation_threshold").setValue(threshold)
            }
            return@setOnEditorActionListener true
        }

        binding.fanAutomaticToggle.setOnCheckedChangeListener { _, isChecked ->
            database.child("devices").child(indexDevice.toString()).child("manual_control_fan").setValue(!isChecked)
        }

        return root
    }
}