package fr.iot.pressf.ui.control

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import fr.iot.pressf.databinding.FragmentControlBinding
import fr.iot.pressf.ui.devices.DevicesViewModel

class ControlFragment : Fragment() {
    private var _binding: FragmentControlBinding? = null
    private val devicesViewModel: DevicesViewModel by activityViewModels()

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentControlBinding.inflate(inflater, container, false)
        val root = binding.root
        return root
    }
}