package besmart.elderlycare.screen.register

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.databinding.WidgetTextspinnerBinding

class SpinerAdapter(
    private val list: List<String>,
    private val viewRoot:View,
    private val onSpinnerItemClick: OnSpinnerItemClick
): RecyclerView.Adapter<SpinerAdapter.SpinerHolder>() {
    private lateinit var binding: WidgetTextspinnerBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpinerHolder {
        binding = WidgetTextspinnerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SpinerHolder()
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: SpinerHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class SpinerHolder: RecyclerView.ViewHolder(binding.root) {

        fun bind(gender: String) {
            binding.textSpinner.text = gender
            binding.executePendingBindings()
            binding.textSpinner.setOnClickListener {
                val position = adapterPosition
                onSpinnerItemClick.onSpinnerItemClick(gender, viewRoot)
            }
        }
    }

    interface OnSpinnerItemClick {
        fun onSpinnerItemClick(text: String, view: View)
    }
}