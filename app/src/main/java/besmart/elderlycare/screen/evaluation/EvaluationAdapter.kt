package besmart.elderlycare.screen.evaluation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.databinding.ItemEvaluationBinding
import besmart.elderlycare.model.evaluation.EvaluationResponse
import besmart.elderlycare.util.SimpleOnItemClick

class EvaluationAdapter(
    private val list: List<EvaluationResponse>,
    private val listener: SimpleOnItemClick<EvaluationResponse>
) :
    RecyclerView.Adapter<EvaluationAdapter.EvaluationHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EvaluationHolder {
        val binding =
            ItemEvaluationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EvaluationHolder(binding, listener)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: EvaluationHolder, position: Int) {
        holder.bind(list[position])
    }

    class EvaluationHolder(
        private val binding: ItemEvaluationBinding,
        private val listener: SimpleOnItemClick<EvaluationResponse>
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(evaluationResponse: EvaluationResponse) {
            binding.model = evaluationResponse
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                listener.onItemClick(evaluationResponse)
            }
        }
    }
}