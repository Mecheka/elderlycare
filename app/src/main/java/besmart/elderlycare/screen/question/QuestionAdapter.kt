package besmart.elderlycare.screen.question

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.databinding.ItemQuestionChoinBinding
import besmart.elderlycare.databinding.ItemQuestionHeaderBinding
import besmart.elderlycare.databinding.ItemQuestionHeaderChoinBinding
import besmart.elderlycare.model.evaluation.QuestItem
import besmart.elderlycare.model.evaluation.QuestType

class QuestionAdapter(private val list: List<QuestItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemViewType(position: Int): Int {
        return list[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            QuestType.HEADER -> {
                HeaderQuestion(ItemQuestionHeaderBinding.inflate(layoutInflater, parent, false))
            }
            QuestType.HEADERWITHCHOINCE -> {
                HeaderChoinQuestion(
                    ItemQuestionHeaderChoinBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                )
            }
            else -> {
                ChoinQuestion(ItemQuestionChoinBinding.inflate(layoutInflater, parent, false))
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (list[position].type) {
            QuestType.HEADER -> {
                holder as HeaderQuestion
                holder.bind(list[position])
            }
            QuestType.HEADERWITHCHOINCE -> {
                holder as HeaderChoinQuestion
                holder.bind(list[position])
            }
            else -> {
                holder as ChoinQuestion
                holder.bind(list[position])
            }
        }
    }

    class HeaderQuestion(private val binding: ItemQuestionHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(questItem: QuestItem) {
            binding.model = questItem
            binding.executePendingBindings()
        }
    }

    class HeaderChoinQuestion(private val binding: ItemQuestionHeaderChoinBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(questItem: QuestItem) {
            binding.model = questItem
            binding.executePendingBindings()
            if (binding.rgChoin.childCount > 0) {
                binding.rgChoin.removeAllViews()
            }
            questItem.question.choices?.forEachIndexed { index, choice ->
                val button = RadioButton(binding.rgChoin.context)
                button.id = index
                button.text = choice.text
                binding.rgChoin.addView(button)
            }
        }
    }

    class ChoinQuestion(private val binding: ItemQuestionChoinBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(questItem: QuestItem) {
            binding.model = questItem
            binding.executePendingBindings()
            if (binding.rgChoin.childCount > 0) {
                binding.rgChoin.removeAllViews()
            }
            questItem.question.choices?.forEachIndexed { index, choice ->
                val button = RadioButton(binding.rgChoin.context)
                button.id = index
                button.text = choice.text
                binding.rgChoin.addView(button)
            }
        }
    }
}