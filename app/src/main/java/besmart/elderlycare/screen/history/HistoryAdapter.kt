package besmart.elderlycare.screen.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.databinding.ItemElderlyHistoryBinding
import besmart.elderlycare.model.history.HistoryResponce
import besmart.elderlycare.util.SimpleOnItemClick
import besmart.elderlycare.util.getStringByItem
import besmart.elderlycare.util.loadImageResource
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(
    private val list: List<HistoryResponce>,
    private val listener: SimpleOnItemClick<HistoryResponce>
) : RecyclerView.Adapter<HistoryAdapter.HistoryHolder>() {

    private lateinit var binding: ItemElderlyHistoryBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        binding =
            ItemElderlyHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryHolder(binding, listener)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        when (position) {
            0 -> holder.bind(list[position], first = true)
            list.size - 1 -> holder.bind(list[position], last = true)
            else -> holder.bind(list[position])
        }

    }

    class HistoryHolder(
        private val binding: ItemElderlyHistoryBinding,
        private val listener: SimpleOnItemClick<HistoryResponce>
    ) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun bind(history: HistoryResponce, first: Boolean = false, last: Boolean = false) {
            binding.executePendingBindings()
            setTextByType(history)
            setImageByType(history)
            binding.layoutRoot.setBackgroundResource(setBackgroundByPosition(first, last))
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                val outputFormat = SimpleDateFormat(" dd MMM yyyy HH:mm", Locale("TH"))
                val input = inputFormat.parse(history.createAt)
                val output = outputFormat.format(input)
                binding.textDate.text = "วันที่ $output น."
            } catch (e:ParseException){

            }

        }

        private fun setTextByType(history: HistoryResponce) {
            when (history.typeID) {
                1 -> {
                    binding.textTitle.getStringByItem(R.string.menu_body_mass)
                }
                2 -> {
                    binding.textTitle.getStringByItem(R.string.menu_pressure)
                }
                3 -> {
                    binding.textTitle.getStringByItem(R.string.menu_sugar)
                }
            }
        }

        private fun setImageByType(history: HistoryResponce) {
            when (history.typeID) {
                1 -> {
                    binding.imageView.loadImageResource(R.drawable.baseline_accessibility)
                }
                2 -> {
                    binding.imageView.loadImageResource(R.drawable.baseline_timeline)
                }
                3 -> {
                    binding.imageView.loadImageResource(R.drawable.baseline_invert_colors)
                }
            }
        }

        private fun setBackgroundByPosition(
            first: Boolean = false,
            last: Boolean = false
        ): Int {
            if (first) {
                return R.drawable.shape_bg_item_history_first
            }

            if (last) {
                return R.drawable.shape_bg_item_history_last
            }
            return R.drawable.shape_bg_item_history_normal
        }

    }
}