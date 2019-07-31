package besmart.elderlycare.screen.evaluationhistory

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.util.BaseDialog
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class EvaluationHistoryActivity : BaseActivity() {

    companion object {
        const val PROFILE = "profile"
    }

    private val profile: ProfileResponce by lazy {
        intent.getParcelableExtra(PROFILE) as ProfileResponce
    }
    private val viewModel: EvaluationHistoryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evaluation_history)
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
            recyclerView.apply {
                this.layoutManager =
                    LinearLayoutManager(
                        this@EvaluationHistoryActivity,
                        RecyclerView.VERTICAL,
                        false
                    )
                this.hasFixedSize()
                this.adapter = EvaluetionHistoryAdapter(it)
            }
        })

        viewModel.getEvaluationHistory(profile.cardID!!)
    }
}
