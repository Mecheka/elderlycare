package besmart.elderlycare.screen.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.databinding.ItemNotificationBinding
import besmart.elderlycare.model.notification.NotificationResponce

class NotificationAdapter(private val list: List<NotificationResponce>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationHolder>() {

    private lateinit var binding: ItemNotificationBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        binding =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        holder.bind(list[position])
    }

    class NotificationHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: NotificationResponce) {
            binding.model = notification
            binding.executePendingBindings()
        }
    }
}