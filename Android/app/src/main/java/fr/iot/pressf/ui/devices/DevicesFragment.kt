package fr.iot.pressf.ui.devices

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fr.iot.pressf.databinding.FragmentDevicesBinding

class DevicesFragment : Fragment() {

    private var _binding: FragmentDevicesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val devicesViewModel: DevicesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDevicesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        devicesViewModel.fetchDevices()

        val recyclerView = binding.devicesRecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        devicesViewModel.devices.observe(viewLifecycleOwner) {
            if (it != null) {
                recyclerView.adapter = DevicesAdapter(it)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}