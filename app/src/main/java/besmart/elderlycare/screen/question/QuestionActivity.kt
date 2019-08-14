package besmart.elderlycare.screen.question

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.model.evaluation.EvaluationResponse
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.util.BaseDialog
import kotlinx.android.synthetic.main.activity_main.recyclerView
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_question.*
import kotlinx.android.synthetic.main.dialog_score.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class QuestionActivity : BaseActivity(), QuestionAdapter.QuestionClick {

    companion object {
        const val EVALUATION = "evaluation"
        const val PROFILE = "profile"
    }

    private val evaluation: EvaluationResponse by lazy {
        intent.getParcelableExtra(EVALUATION) as EvaluationResponse
    }
    private val profile: ProfileResponce by lazy {
        intent.getParcelableExtra(PROFILE) as ProfileResponce
    }
    private val viewModel: QuestionViewModel by viewModel()
    private lateinit var questionAdapter: QuestionAdapter

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
        btnSave.setOnClickListener {
            questionAdapter.getAnswer()
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
            questionAdapter = QuestionAdapter(it, this)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@QuestionActivity, RecyclerView.VERTICAL,  false)
                setHasFixedSize(true)
                adapter = questionAdapter
            }
        })

        viewModel.getQuestion(evaluation.id!!)
    }

    override fun onError() {
        showScoreDialog(0, "ภาวะพึ่งพาโดยสมบูรณ์")
    }

    override fun onSuccess(answer: QuestionAdapter.Answer) {
//        viewModel.addAnswer(answer, profile.cardID, evaluation.id)
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun showScoreDialog(score: Int, result: String) {
        val view = layoutInflater.inflate(R.layout.dialog_score, null)
        val dialog = AlertDialog.Builder(this)
        dialog.setView(view)

        view.textScore.text = "$score คะแนแ"
        view.textResult.text = result

        val alertDialog = dialog.create()
        alertDialog.show()

        view.btnOK.setOnClickListener {
            alertDialog.dismiss()
        }
    }
}
