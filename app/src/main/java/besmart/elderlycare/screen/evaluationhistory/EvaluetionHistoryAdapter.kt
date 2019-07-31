package besmart.elderlycare.screen.evaluationhistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.databinding.ItemEvaluationHistoryBinding
import besmart.elderlycare.model.blood.BloodPressuresResponse
import besmart.elderlycare.util.convertDate

class EvaluetionHistoryAdapter(private val list: List<BloodPressuresResponse>) :
    RecyclerView.Adapter<EvaluetionHistoryAdapter.EvaluationHistoryHolder>() {

    private lateinit var binding: ItemEvaluationHistoryBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EvaluationHistoryHolder {
        binding =
            ItemEvaluationHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EvaluationHistoryHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: EvaluationHistoryHolder, position: Int) {
        holder.bind(list[position])
    }

    class EvaluationHistoryHolder(private val binding: ItemEvaluationHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bloodPressuresResponse: BloodPressuresResponse) {
            binding.bmiLayout.setBackgroundColor(bloodPressuresResponse.getResultColor())
            binding.textSys.text = bloodPressuresResponse.sys.toString()
            binding.textDia.text = bloodPressuresResponse.dia.toString()
            binding.textTitle.text = bloodPressuresResponse.getResult()
            binding.textDate.text = bloodPressuresResponse.createAt?.convertDate()
        }
    }
}