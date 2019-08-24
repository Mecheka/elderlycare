package besmart.elderlycare.screen.calendar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.databinding.ItemCalendarBinding
import besmart.elderlycare.model.schedule.Schedule

class CalendarAdapter(private val list: MutableList<Schedule>) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    fun setScheduleData(schedule: List<Schedule>) {
        list.clear()
        list.addAll(schedule)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        return CalendarViewHolder(
            ItemCalendarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(list[position])
    }

    class CalendarViewHolder(private val binding: ItemCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(schedule: Schedule) {
            binding.model = schedule
            binding.executePendingBindings()
            binding.textTimeStart.text = addZeroToString(schedule.hour.toString()) + ".00"
            binding.textTimeEnd.text = addZeroToString((schedule.hour + 1).toString()) + ".00"
            binding.line.setBackgroundColor(schedule.categoryColor)
        }

        private fun addZeroToString(text:String): String {
            if(text.length == 1){
                return "0$text"
            }
            return text
        }
    }
}