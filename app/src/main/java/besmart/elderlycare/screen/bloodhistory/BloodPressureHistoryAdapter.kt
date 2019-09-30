package besmart.elderlycare.screen.bloodhistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.databinding.ItemBloodPressureHistoryBinding
import besmart.elderlycare.model.blood.BloodPressuresResponse
import besmart.elderlycare.util.convertDate

class BloodPressureHistoryAdapter(private val list: MutableList<BloodPressuresResponse>) :
    RecyclerView.Adapter<BloodPressureHistoryAdapter.EvaluationHistoryHolder>() {

    private lateinit var binding: ItemBloodPressureHistoryBinding

    fun getItemByPosition(position: Int): BloodPressuresResponse {
        return list[position]
    }

    fun removeItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EvaluationHistoryHolder {
        binding =
            ItemBloodPressureHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EvaluationHistoryHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: EvaluationHistoryHolder, position: Int) {
        holder.bind(list[position])
    }

    class EvaluationHistoryHolder(private val binding: ItemBloodPressureHistoryBinding) :
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