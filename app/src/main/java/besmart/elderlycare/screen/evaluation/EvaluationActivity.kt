package besmart.elderlycare.screen.evaluation

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.model.evaluation.EvaluationResponse
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.screen.question.QuestionActivity
import besmart.elderlycare.util.BaseDialog
import besmart.elderlycare.util.SimpleOnItemClick
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class EvaluationActivity : BaseActivity(), SimpleOnItemClick<EvaluationResponse> {

    companion object{
        const val PROFILE = "profile"
    }

    private val viewModel: EvaluationViewModel by viewModel()
    private val profile:ProfileResponce by lazy {
        intent.getParcelableExtra(PROFILE) as ProfileResponce
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evaluation)

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
                layoutManager = LinearLayoutManager(this@EvaluationActivity, RecyclerView.VERTICAL,  false)
                setHasFixedSize(true)
                adapter = EvaluationAdapter(it,this@EvaluationActivity)
            }
        })

        viewModel.getEvaluation()
    }

    override fun onItemClick(item: EvaluationResponse) {
        Intent().apply {
            setClass(this@EvaluationActivity, QuestionActivity::class.java)
            putExtra(QuestionActivity.EVALUATION, item)
            putExtra(QuestionActivity.PROFILE, profile)
            startActivity(this)
        }
    }
}
