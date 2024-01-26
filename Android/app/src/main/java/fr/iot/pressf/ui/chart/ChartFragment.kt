package fr.iot.pressf.ui.chart

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import fr.iot.pressf.databinding.FragmentChartBinding
import fr.iot.pressf.ui.devices.DevicesViewModel
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChartFragment : Fragment(),
    OnSeekBarChangeListener,
    OnChartValueSelectedListener {

    private var _binding: FragmentChartBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var chartViewModel: ChartViewModel
    private val devicesViewModel: DevicesViewModel by activityViewModels()

    fun getCurrentCaptureTimestamp(): Long? {
        val devices = devicesViewModel.devices.value
        val device = devices?.get(0)
        val index = device?.latest
        val chartData = chartViewModel.data.value
        Log.d("device", chartData.toString())
        val data = index?.let { chartData?.get(it) }
        val timestamp = data?.timestamp
        return timestamp
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChartBinding.inflate(inflater, container, false)
        chartViewModel = ViewModelProvider(this).get(ChartViewModel::class.java)
        val root: View = binding.root
        val indexDevice = devicesViewModel.selected
        chartViewModel.fetchData(indexDevice)
        val entries: ArrayList<Entry> = ArrayList()
        val chart = binding.chart
        chartViewModel.data.observe(viewLifecycleOwner) {
            val currentTimestamp = getCurrentCaptureTimestamp()
            if (it != null && currentTimestamp != null) {
                chart.setBackgroundColor(Color.WHITE)
                chart.description.isEnabled = false
                chart.setTouchEnabled(false)
                entries.clear()
                for ((index, item) in it.withIndex()) {
                    if (item.temperature != null && item.humidity != null && item.timestamp != null) {
                        val dateCapture = Date(item.timestamp)
                        val dateString = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(dateCapture)
                        entries.add(Entry(dateCapture.time.toFloat() - currentTimestamp, item.temperature))
                    }
                }
            }
            val dataSet = LineDataSet(entries, "Capteur")
            Log.d("chart", dataSet.toString())
            dataSet.color = Color.BLUE
            val lineData = LineData(dataSet)
            chart.data = lineData
            chart.invalidate()
        }

        /*val seekBarX = binding.seekBarX
        val seekBarY = binding.seekBarY

        seekBarX.setOnSeekBarChangeListener(this)

        seekBarY.max = 100
        seekBarY.setOnSeekBarChangeListener(this)*/

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
        TODO("Not yet implemented")
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
        TODO("Not yet implemented")
    }

    override fun onValueSelected(p0: Entry?, p1: Highlight?) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected() {
        TODO("Not yet implemented")
    }
}