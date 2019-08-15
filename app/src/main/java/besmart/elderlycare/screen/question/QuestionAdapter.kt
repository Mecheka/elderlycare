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
import besmart.elderlycare.model.evaluation.UserEvaluarion
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class QuestionAdapter(
    private val list: List<QuestItem>,
    userEvaluation: UserEvaluarion?,
    private val listener: QuestionClick
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var answer = mutableMapOf<String, Int>()

    init {
        userEvaluation?.json?.let {
            val type = object : TypeToken<Map<String, Int>>() {}.type
            answer = Gson().fromJson(it, type)
        }
    }

    fun getAnswer() {
        var result = 0
        val gson = Gson()

        if (answer.isEmpty()){
            listener.onError()
            return
        }

        answer.forEach {
            result += it.value
        }
        listener.onSuccess(Answer(gson.toJson(answer), result))
    }

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
                if (answer[list[position].question.questionNo] != null) {
                    holder.setRadioButton(answer[list[position].question.questionNo])
                }
            }
            else -> {
                holder as ChoinQuestion
                holder.bind(list[position])
                if (answer[list[position].question.questionNo] != null) {
                    val id = answer[list[position].question.questionNo]
                    holder.setRadioButton(id)
                }
            }
        }
    }

    inner class HeaderQuestion(private val binding: ItemQuestionHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(questItem: QuestItem) {
            binding.model = questItem
            binding.executePendingBindings()
        }
    }

    inner class HeaderChoinQuestion(private val binding: ItemQuestionHeaderChoinBinding) :
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
            binding.rgChoin.setOnCheckedChangeListener { group, checkedId ->
                answer[questItem.question.questionNo!!] = checkedId + 1
            }
        }

        fun setRadioButton(id: Int?) {
            binding.rgChoin.check(id!! - 1)
        }
    }

    inner class ChoinQuestion(private val binding: ItemQuestionChoinBinding) :
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
            binding.rgChoin.setOnCheckedChangeListener { group, checkedId ->
                answer[questItem.question.questionNo!!] = checkedId + 1
            }
        }

        fun setRadioButton(id: Int?) {
            binding.rgChoin.check(id!! - 1)
        }
    }

    data class Answer(val answer: String, val result: Int) {
        fun getResult(): String {
            return when (result) {
                in 0..4 -> "ภาวะพึ่งพาโดยสมบูรณ์"
                in 5..8 -> "ภาวะพึ่งพารุนแรง"
                in 9..11 -> "ภาวะพึ่งพาปานกลาง"
                else -> "ไม่เป็นการพึ่งพา"
            }
        }
    }

    interface QuestionClick{
        fun onError()
        fun onSuccess(answer: Answer)
    }
}
