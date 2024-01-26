package fr.iot.pressf.ui.devices

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.iot.pressf.R
import fr.iot.pressf.datasource.Device

class DevicesAdapter(
    private val devices: List<Device>,
    private val onItemClicked: (Int) -> Unit
) :
    RecyclerView.Adapter<DevicesAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.device_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.device_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = devices.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val device = devices[position]
        holder.name.text = device.name
        holder.itemView.setOnClickListener { onItemClicked(position) }
    }

}