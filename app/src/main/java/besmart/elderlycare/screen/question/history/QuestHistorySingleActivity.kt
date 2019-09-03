package besmart.elderlycare.screen.question.history

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.model.evaluation.UserEvaluarion
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.screen.question.QuestionAdapter
import besmart.elderlycare.util.BaseDialog
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_quest_history_single.*
import org.koin.android.viewmodel.ext.android.viewModel

class QuestHistorySingleActivity : BaseActivity() {

    companion object {
        const val USER_EVALUATION = "userevaluation"
    }

    private val viewModel: QuestHistorySingleViewModel by viewModel()
    private val userEvaluation: UserEvaluarion by lazy {
        intent.getParcelableExtra(USER_EVALUATION) as UserEvaluarion
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest_history_single)

        initInstance()
        observerViewModel()
    }

    private fun initInstance() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun observerViewModel() {
        viewModel.errorLiveData.observe(this, Observer {
            BaseDialog.warningDialog(this, it)
        })

        viewModel.loadingLiveData.observe(this, Observer {
            if (it) {
                showLoadingDialog(this)
            } else {
                dismissDialog()
            }
        })

        viewModel.evaluationLiveData.observe(this, Observer {
            //            questionAdapter = QuestionAdapter(it.item!!, it.userEvaluarion, this, isPerson)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(
                    this@QuestHistorySingleActivity,
                    RecyclerView.VERTICAL,
                    false
                )
                setHasFixedSize(true)
                adapter = QuestionAdapter(it.item!!, it.userEvaluarion)
            }
        })

        viewModel.getQuestion(userEvaluation)
    }
}
