package besmart.elderlycare.screen.question

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.model.evaluation.EvaluationResponse
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.util.BaseDialog
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class QuestionActivity : BaseActivity() {

    companion object {
        const val EVALUATION = "evaluation"
    }

    private val evaluation: EvaluationResponse by lazy {
        intent.getParcelableExtra(EVALUATION) as EvaluationResponse
    }
    private val viewModel: QuestionViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

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

    private fun observerViewModel(){
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
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@QuestionActivity, RecyclerView.VERTICAL,  false)
                setHasFixedSize(true)
                adapter = QuestionAdapter(it)
            }
        })

        viewModel.getQuestion(evaluation.id!!)
    }
}
