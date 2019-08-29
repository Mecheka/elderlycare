package besmart.elderlycare.screen.bodymasshistory

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.databinding.ItemBodyMassHistoryBinding
import besmart.elderlycare.model.bodymass.BodyMassResponce
import besmart.elderlycare.util.convertDate

class BodyMassHistoryAdapter(private val list: MutableList<BodyMassResponce>) :
    RecyclerView.Adapter<BodyMassHistoryAdapter.BodyMassHistoryHolder>() {

    private lateinit var binding: ItemBodyMassHistoryBinding

    fun getItemByPosition(position: Int): BodyMassResponce {
        return list[position]
    }

    fun removeItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BodyMassHistoryHolder {
        binding =
            ItemBodyMassHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BodyMassHistoryHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: BodyMassHistoryHolder, position: Int) {
        holder.bind(list[position])
    }

    class BodyMassHistoryHolder(private val binding: ItemBodyMassHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bodyMassResponce: BodyMassResponce) {
            binding.bmiLayout.setBackgroundColor(getResultColorByBMI(bodyMassResponce.bMI!!))
            binding.textBmiResult.text = bodyMassResponce.bMI.toString()
            binding.textTitle.text = bodyMassResponce.getWeigthResultByBMI()
            binding.textDate.text = bodyMassResponce.date?.convertDate()
        }

        private fun getResultColorByBMI(bMI: Double): Int {
            return when {
                bMI >= 40 -> Color.parseColor("#DE0101")
                bMI >= 30 -> Color.parseColor("#FF4E00")
                bMI >= 25 -> Color.parseColor("#FF9900")
                bMI >= 18.5 -> Color.parseColor("#00C857")
                else -> Color.parseColor("#00B1C8")
            }
        }
    }
}