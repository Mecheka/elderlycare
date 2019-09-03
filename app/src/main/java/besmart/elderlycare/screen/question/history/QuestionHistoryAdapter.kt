package besmart.elderlycare.screen.question.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.model.evaluation.Answer
import besmart.elderlycare.model.evaluation.UserEvaluarion
import kotlinx.android.synthetic.main.item_question_history.view.*

class QuestionHistoryAdapter(private val list: List<UserEvaluarion>) :
    RecyclerView.Adapter<QuestionHistoryAdapter.QuestionHistoryViewHolder>() {

    var onItemClick:(UserEvaluarion) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionHistoryViewHolder {
        return QuestionHistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_question_history,
                parent,
                false
            ), onItemClick
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: QuestionHistoryViewHolder, position: Int) {
        holder.bind(list[position])
    }

    class QuestionHistoryViewHolder(
        private val view: View,
        private val onItemClick: (UserEvaluarion) -> Unit
    ) :
        RecyclerView.ViewHolder(view) {

        @SuppressLint("SetTextI18n")
        fun bind(userEvaluarion: UserEvaluarion) {
            val result = Answer.getResult(userEvaluarion.score!!)
            view.textScore.text = "${userEvaluarion.score} คะแนน $result"
            view.textDate.text = userEvaluarion.createAt
            view.setOnClickListener {
                onItemClick(userEvaluarion)
            }
        }
    }
}